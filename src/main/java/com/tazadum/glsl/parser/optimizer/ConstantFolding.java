package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Node;

/**
 * Created by Erik on 2016-10-20.
 */
public class ConstantFolding implements Optimizer {
    @Override
    public OptimizerResult run(Node node) {
        ConstantFoldingVisitor visitor = new ConstantFoldingVisitor();
        final Node accept = node.accept(visitor);
        final int changes = visitor.getChanges();

        return new OptimizerResult(changes, accept == null ? node : accept);
    }
}
