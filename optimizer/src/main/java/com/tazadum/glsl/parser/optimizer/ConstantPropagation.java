package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.parser.ParserContext;

/**
 * Created by Erik on 2016-10-23.
 */
public class ConstantPropagation extends BranchingOptimizer {
    @Override
    public String name() {
        return "constant propagations";
    }

    @Override
    OptimizerVisitor createVisitor(ParserContext context, OptimizationDecider decider) {
        return new ConstantPropagationVisitor(context, decider);
    }
}
