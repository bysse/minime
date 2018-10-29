package com.tazadum.glsl.optimizer.squeeze;

import com.tazadum.glsl.optimizer.BranchRegistry;
import com.tazadum.glsl.optimizer.BranchingOptimizer;
import com.tazadum.glsl.optimizer.OptimizationDecider;
import com.tazadum.glsl.optimizer.OptimizerVisitor;
import com.tazadum.glsl.parser.ParserContext;

/**
 * Squeezes multiple variable declarations into a single line.
 * Created by Erik on 2016-10-23.
 */
public class DeclarationSqueeze extends BranchingOptimizer {
    @Override
    public String name() {
        return "declarations squeezes";
    }

    @Override
    protected OptimizerVisitor createVisitor(ParserContext context, BranchRegistry branchRegistry, OptimizationDecider decider) {
        return new DeclarationSqueezeVisitor(context);
    }
}
