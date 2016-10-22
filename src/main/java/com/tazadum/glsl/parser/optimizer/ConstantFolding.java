package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Node;

/**
 * Created by Erik on 2016-10-20.
 */
public class ConstantFolding implements Optimizer {
    @Override
    public OptimizerResult run(OptimizationDecider decider, Node node) {
        ConstantFoldingVisitor visitor = new ConstantFoldingVisitor(decider);

        int changes;
        do {
            visitor.reset();
            Node accept = node.accept(visitor);
            changes = visitor.getChanges();

            if (accept != null) {
                node = accept;
            }
        } while (changes > 0);

        return new OptimizerResult(changes, node);
    }
}
