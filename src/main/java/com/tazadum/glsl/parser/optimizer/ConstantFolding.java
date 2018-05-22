package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.parser.ParserContext;

/**
 * Created by Erik on 2016-10-20.
 */
public class ConstantFolding implements Optimizer {
    @Override
    public String name() {
        return "constant folding replacements";
    }

    @Override
    public OptimizerResult run(ParserContext parserContext, OptimizationDecider decider, Node node) {
        final ConstantFoldingVisitor visitor = new ConstantFoldingVisitor(parserContext, decider);
        return runWithOptimizer(visitor, parserContext, node);
    }
}
