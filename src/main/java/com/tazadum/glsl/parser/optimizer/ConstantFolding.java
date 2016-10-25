package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.parser.ParserContext;

/**
 * Created by Erik on 2016-10-20.
 */
public class ConstantFolding implements Optimizer {
    @Override
    public OptimizerResult run(ParserContext parserContext, OptimizationDecider decider, Node node) {
        final ConstantFoldingVisitor visitor = new ConstantFoldingVisitor(parserContext, decider);

        int changes, totalChanges = 0;
        do {
            visitor.reset();
            Node accept = node.accept(visitor);
            changes = visitor.getChanges();
            totalChanges += changes;

            if (accept != null) {
                node = accept;
            }
        } while (changes > 0);

        return new OptimizerResult(totalChanges, node);
    }
}
