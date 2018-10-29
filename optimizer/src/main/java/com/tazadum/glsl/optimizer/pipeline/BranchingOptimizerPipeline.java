package com.tazadum.glsl.optimizer.pipeline;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.output.OutputConfig;
import com.tazadum.glsl.language.output.OutputRenderer;
import com.tazadum.glsl.optimizer.*;
import com.tazadum.glsl.parser.ParserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public Branch optimize(OptimizerContext optimizerContext, Node shaderNode, boolean showOutput) {
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
                logger.info(String.format("Iteration #%d: %d branches, %d bytes", iteration++, acceptedBranches.size(), minSize));
            }

            for (Optimizer optimizer : optimizers) {
                int branchCount = 0;
                int batchMinSize = Integer.MAX_VALUE;
                Branch batchMinBranch = null;

                for (Branch branch : acceptedBranches) {
                    Node node = branch.getNode();
                    ParserContext context = branch.getContext();

                    // don't act on the input branch
                    if (!inputBranch) {
                        // check the size of this branch
                        int branchSize = output.render(node, outputConfig).length();
                        int sizeDifference = branchSize - previousSize;
                        minSize = Math.min(minSize, branchSize);

                        // keep track of the smallest in the batch
                        if (branchSize < batchMinSize) {
                            batchMinSize = branchSize;
                            batchMinBranch = branch;
                        }

                        if (treePruner.prune(iteration, sizeDifference)) {
                            // this tree is pruned and excluded from further exploration
                            continue;
                        }
                    }

                    final Optimizer.OptimizerResult result = optimizer.run(context, branchRegistry, decider, node);
                    int changes = result.getChanges();
                    if (changes <= 0) {
                        continue;
                    }
                    totalChanges += changes;

                    List<Branch> branches = result.getBranches();
                    branchCount += branches.size();
                    discoveredBranches.addAll(branches);

                    inputBranch = false;
                }

                if (showOutput || logger.isDebugEnabled()) {
                    int branches = Math.max(0, branchCount);
                    logger.debug(String.format("  - %s: %d changes and +%d branches", optimizer.name(), totalChanges, branches));
                }

                if (batchMinBranch != null) {
                    // propagate the smallest branch to the next batch
                    discoveredBranches.add(batchMinBranch);
                }

                // enforce branch uniqueness and add all of them to the list of accepted nodes
                acceptedBranches.clear();
                acceptedBranches.addAll(Branch.unique(discoveredBranches));
            }

            previousSize = minSize;
        } while (totalChanges > 0);

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
}
