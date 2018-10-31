package com.tazadum.glsl.optimizer.pipeline;

import com.tazadum.glsl.cli.OptimizerReport;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.output.OutputConfig;
import com.tazadum.glsl.language.output.OutputRenderer;
import com.tazadum.glsl.optimizer.*;
import com.tazadum.glsl.parser.ParserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BranchingOptimizerPipeline implements OptimizerPipeline {
    private final Logger logger = LoggerFactory.getLogger(BranchingOptimizerPipeline.class);
    private final TreePruner treePruner;
    private final OutputConfig outputConfig;
    private final OutputRenderer output;
    private final List<Optimizer> optimizers;

    public BranchingOptimizerPipeline(TreePruner treePruner, OutputConfig outputConfig, OptimizerType... types) {
        this.treePruner = treePruner;
        this.outputConfig = outputConfig;
        this.output = new OutputRenderer();
        this.optimizers = Stream.of(types)
            .map(OptimizerType::instantiate)
            .collect(Collectors.toList());
    }

    public BranchingOptimizerPipeline(TreePruner treePruner, OutputConfig outputConfig, EnumSet<OptimizerType> types) {
        this.treePruner = treePruner;
        this.outputConfig = outputConfig;
        this.output = new OutputRenderer();
        this.optimizers = types.stream()
            .map(OptimizerType::instantiate)
            .collect(Collectors.toList());
    }

    public Branch optimize(OptimizerContext optimizerContext, Node shaderNode, boolean showOutput, OptimizerReport report) {
        final OutputSizeDecider decider = new OutputSizeDecider(outputConfig.getFormatter().getSignificantDigits());
        final ParserContext parserContext = optimizerContext.parserContext();
        final BranchRegistry branchRegistry = optimizerContext.branchRegistry();

        // create a list of branches to explore
        List<Branch> acceptedBranches = new ArrayList<>();
        acceptedBranches.add(new Branch(parserContext, shaderNode));

        boolean inputBranch = true;
        int minSize = output.render(shaderNode, outputConfig).length();
        int totalChanges, iteration = 0, previousSize = minSize;
        do {
            List<Branch> discoveredBranches = new ArrayList<>();
            totalChanges = 0;

            if (showOutput) {
                logger.info(String.format("* Iteration %d: branches=%d", iteration++, acceptedBranches.size()));
            }

            if (report != null) {
                report.addBranches(acceptedBranches.size());
            }

            Branch batchMinBranch = null;
            int batchMinSize = Integer.MAX_VALUE;
            int removedBranches = 0;

            // run through all optimizers one branch at a time
            int branchIndex = 0;
            for (Branch branch : acceptedBranches) {
                if (showOutput) {
                    logger.info("   * Branch {}:", branchIndex++);
                }
                Node node = branch.getNode();
                ParserContext context = branch.getContext();

                int branchChanges = 0;
                int branchesDiscovered = 0;
                int branchSizeBefore = output.render(node, outputConfig).length();

                for (Optimizer optimizer : optimizers) {
                    final Optimizer.OptimizerResult result = optimizer.run(context, branchRegistry, decider, node);
                    int changes = result.getChanges();
                    totalChanges += changes;
                    branchChanges += changes;

                    List<Branch> branches = result.getBranches();
                    branchesDiscovered += branches.size() - 1;
                    discoveredBranches.addAll(branches);

                    if (changes > 0) {
                        logger.info(String.format("      - %-12s %d", optimizer.name(), changes));
                    }
                }

                // check the size of this branch
                int branchSizeAfter = output.render(node, outputConfig).length();
                int sizeDifference = minSize - branchSizeAfter;
                minSize = Math.min(minSize, branchSizeAfter);

                if (treePruner.prune(iteration, sizeDifference)) {
                    // this tree is pruned and excluded from further exploration
                    logger.info("      + branch summary: removed");
                    removedBranches++;
                    continue;
                }

                // keep track of the smallest in the batch
                if (branchSizeAfter < batchMinSize) {
                    batchMinSize = branchSizeAfter;
                    batchMinBranch = branch;
                }

                if (showOutput) {
                    if (branchChanges == 0 && branchSizeAfter - branchSizeBefore == 0 && branchesDiscovered == 0) {
                        logger.info("      - no modifications");
                    } else {
                        logger.info("      - branch summary:");
                        logger.info("         - modifications:   {}", branchChanges);
                        logger.info("         - size difference: {}", branchSizeAfter - branchSizeBefore);
                        logger.info("         - new branches:    {}", branchesDiscovered);
                    }
                }

                if (batchMinBranch != null) {
                    // propagate the smallest branch to the next batch
                    discoveredBranches.add(batchMinBranch);
                }
            }

            // after all branches has been processed enforce branch uniqueness and add all of them to the list of accepted nodes
            acceptedBranches.clear();
            acceptedBranches.addAll(Branch.unique(discoveredBranches));


            if (showOutput) {
                logger.info("   * Summary:");
                logger.info("      - pruned branches: {}", removedBranches);
                logger.info("      - size difference: {}", minSize - previousSize);
            }

            if (report != null) {
                report.addChanges(totalChanges);
            }

            previousSize = minSize;
        } while (totalChanges > 0);

        if (report != null) {
            report.setIterations(iteration);
        }

        if (acceptedBranches.isEmpty()) {
            if (!inputBranch) {
                throw new IllegalStateException("No branches found after optimization!");
            }

            // the input shader couldn't be optimized further
            return new Branch(parserContext, shaderNode);
        }

        if (acceptedBranches.size() == 1) {
            return acceptedBranches.get(0);
        }

        minSize = Integer.MAX_VALUE;
        Branch minBranch = null;
        for (Branch branch : acceptedBranches) {
            int size = output.render(branch.getNode(), outputConfig).length();

            if (size < minSize) {
                minSize = size;
                minBranch = branch;
            }
        }

        return minBranch;
    }

    private String pad(String name, int n) {
        int diff = n - name.length();
        if (diff > 0) {
            name += CharBuffer.allocate(diff).toString().replace('\0', ' ');
        }
        return name;
    }
}
