package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.parser.ParserContext;

import java.util.ArrayList;
import java.util.List;

public abstract class BranchingOptimizer implements Optimizer {

    abstract OptimizerVisitor createVisitor(BranchRegistry branchRegistry, ParserContext context, OptimizationDecider decider);

    @Override
    public OptimizerResult run(BranchRegistry branchRegistry, ParserContext parserContext, OptimizationDecider optimizationDecider, Node node) {
        List<OptimizerBranch> branches = new ArrayList<>();
        OptimizerVisitor visitor = createVisitor(branchRegistry, parserContext, optimizationDecider);

        int changes, totalChanges = 0;
        do {
            visitor.reset();
            Node accept = node.accept(visitor);
            changes = visitor.getChanges();
            totalChanges += changes;

            if (accept != null) {
                node = accept;
                branches.addAll(visitor.getBranches());
            }
        } while (changes > 0);

        branches.add(0, new OptimizerBranch(parserContext, node));

        return new OptimizerResult(totalChanges, branches);
    }
}
