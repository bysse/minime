package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.parser.ParserContext;

/**
 * Created by Erik on 2016-10-23.
 */
public class DeadCodeElimination implements Optimizer {
    @Override
    public String name() {
        return "dead code eliminations";
    }

    @Override
    public OptimizerResult run(ParserContext parserContext, OptimizationDecider optimizationDecider, Node node) {
        final DeadCodeEliminationVisitor visitor = new DeadCodeEliminationVisitor(parserContext);
        return runWithOptimizer(visitor, parserContext, node);
    }
}
