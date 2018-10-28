package com.tazadum.glsl.optimizer;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.parser.ParserContext;

import java.util.ArrayList;
import java.util.List;

public abstract class BranchingOptimizer implements Optimizer {

    protected abstract OptimizerVisitor createVisitor(ParserContext context, BranchRegistry branchRegistry, OptimizationDecider decider);

    @Override
    public OptimizerResult run(ParserContext context, BranchRegistry branchRegistry, OptimizationDecider optimizationDecider, Node node) {
        List<Branch> branches = new ArrayList<>();
        OptimizerVisitor visitor = createVisitor(context, branchRegistry, optimizationDecider);

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

        branches.add(0, new Branch(context, node));

        return new OptimizerResult(totalChanges, branches);
    }
}