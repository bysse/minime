package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.GLSLOptimizerContext;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.output.Output;
import com.tazadum.glsl.output.OutputConfig;
import com.tazadum.glsl.output.OutputSizeDecider;
import com.tazadum.glsl.parser.ParserContext;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OptimizerTreePipeline {
    private final TreePruner treePruner;
    private final OutputConfig outputConfig;
    private final Output output;
    private final List<Optimizer> optimizers;

    public OptimizerTreePipeline(TreePruner treePruner, OutputConfig outputConfig, OptimizerType... types) {
        this.treePruner = treePruner;
        this.outputConfig = outputConfig;
        this.output = new Output();
        this.optimizers = Stream.of(types)
                .map(OptimizerType::instantiate)
                .collect(Collectors.toList());
    }

    public OptimizerTreePipeline(TreePruner treePruner, OutputConfig outputConfig, EnumSet<OptimizerType> types) {
        this.treePruner = treePruner;
        this.outputConfig = outputConfig;
        this.output = new Output();
        this.optimizers = types.stream()
                .map(OptimizerType::instantiate)
                .collect(Collectors.toList());
    }

    public Node optimize(GLSLOptimizerContext optimizerContext, Node shaderNode, boolean showOutput) {
        final OutputSizeDecider decider = new OutputSizeDecider();
        final ParserContext parserContext = optimizerContext.parserContext();

        // create a list of branches to explore
        List<Optimizer.OptimizerBranch> acceptedBranches = new ArrayList<>();
        acceptedBranches.add(new Optimizer.OptimizerBranch(parserContext, shaderNode));

        int minSize = output.render(shaderNode, outputConfig).length();
        int totalChanges, iteration = 0, previousSize = minSize;
        do {
            List<Optimizer.OptimizerBranch> discoveredBranches = new ArrayList<>();
            totalChanges = 0;

            if (showOutput) {
                System.out.println(String.format("Iteration #%d: %d branches, %d bytes", iteration++, acceptedBranches.size(), minSize));
            }

            for (Optimizer optimizer : optimizers) {
                int branchCount = 0;
                int batchMinSize = Integer.MAX_VALUE;
                Optimizer.OptimizerBranch batchMinBranch = null;

                for (Optimizer.OptimizerBranch branch : acceptedBranches) {
                    Node node = branch.getNode();
                    ParserContext context = branch.getContext();

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

                    final Optimizer.OptimizerResult result = optimizer.run(context, decider, node);
                    int changes = result.getChanges();
                    if (changes <= 0) {
                        continue;
                    }
                    totalChanges += changes;

                    List<Optimizer.OptimizerBranch> branches = result.getBranches();
                    branchCount += branches.size();
                    discoveredBranches.addAll(branches);
                }

                if (showOutput) {
                    System.out.println(String.format("  - %s => %d branches", optimizer.name(), branchCount));
                }

                if (batchMinBranch != null) {
                    // propagate the smallest branch to the next batch
                    discoveredBranches.add(batchMinBranch);
                }

                acceptedBranches.clear();
                acceptedBranches.addAll(discoveredBranches);
            }

            previousSize = minSize;
        } while (totalChanges > 0);

        if (acceptedBranches.isEmpty()) {
            throw new IllegalStateException("No branches found after optimization!");
        }

        if (acceptedBranches.size() == 1) {
            return acceptedBranches.get(0).getNode();
        }

        minSize = Integer.MAX_VALUE;
        Node minNode = null;
        for (Optimizer.OptimizerBranch branch : acceptedBranches) {
            int size = output.render(branch.getNode(), outputConfig).length();

            if (size < minSize) {
                minSize = size;
                minNode = branch.getNode();
            }
        }

        return minNode;
    }

}
