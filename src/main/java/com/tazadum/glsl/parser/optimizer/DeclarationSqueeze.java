package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.parser.ParserContext;

/**
 * Created by Erik on 2016-10-23.
 */
public class DeclarationSqueeze extends BranchingOptimizer {
    @Override
    public String name() {
        return "declarations squeezes";
    }

    @Override
    OptimizerVisitor createVisitor(BranchRegistry branchRegistry, ParserContext context, OptimizationDecider decider) {
        return new DeclarationSqueezeVisitor(context);
    }
}
