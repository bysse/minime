package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.parser.ParserContext;

/**
 * Created by Erik on 2016-10-23.
 */
public class ConstantPropagation implements Optimizer {
    @Override
    public String name() {
        return "constant propagations";
    }

    @Override
    public OptimizerResult run(ParserContext parserContext, OptimizationDecider optimizationDecider, Node node) {
        final ConstantPropagationVisitor visitor = new ConstantPropagationVisitor(parserContext, optimizationDecider);
        return runWithOptimizer(visitor, parserContext, node);
    }
}
