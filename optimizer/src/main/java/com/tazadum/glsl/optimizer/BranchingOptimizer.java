package com.tazadum.glsl.optimizer;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.parser.ParserContext;

import java.util.ArrayList;
import java.util.List;

public abstract class BranchingOptimizer implements Optimizer {

    protected abstract OptimizerVisitor createVisitor(ParserContext context, BranchRegistry branchRegistry, OptimizationDecider decider);

    @Override
    public OptimizerResult run(BranchRegistry branchRegistry, OptimizationDecider optimizationDecider, Branch branch) {
        List<Branch> branches = new ArrayList<>();
        OptimizerVisitor visitor = createVisitor(branch.getContext(), branchRegistry, optimizationDecider);

        int changes, totalChanges = 0;
        Node node = branch.getNode();
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

        return new OptimizerResult(totalChanges, new Branch(branch.getContext(), node), branches);
    }
}
