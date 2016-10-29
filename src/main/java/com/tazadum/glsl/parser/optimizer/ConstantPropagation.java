package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.parser.ParserContext;

/**
 * Created by Erik on 2016-10-23.
 */
public class ConstantPropagation implements Optimizer {
    @Override
    public OptimizerResult run(ParserContext parserContext, OptimizationDecider optimizationDecider, Node node) {
        final ConstantPropagationVisitor visitor = new ConstantPropagationVisitor(parserContext);

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
