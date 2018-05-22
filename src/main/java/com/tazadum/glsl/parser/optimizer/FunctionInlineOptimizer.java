package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.parser.ParserContext;

/**
 * Created by Erik on 2016-10-20.
 */
public class FunctionInlineOptimizer implements Optimizer {
    @Override
    public String name() {
        return "function inlines";
    }

    @Override
    public OptimizerResult run(ParserContext parserContext, OptimizationDecider decider, Node node) {
        final FunctionInlineVisitor visitor = new FunctionInlineVisitor(parserContext, decider);
        return runWithOptimizer(visitor, parserContext, node);
    }
}
