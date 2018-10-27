package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.optimizer.BranchingOptimizer;
import com.tazadum.glsl.optimizer.OptimizationDecider;
import com.tazadum.glsl.optimizer.OptimizerVisitor;
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
    OptimizerVisitor createVisitor(ParserContext context, OptimizationDecider decider) {
        return new DeclarationSqueezeVisitor(context);
    }
}
