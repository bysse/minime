package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.optimizer.BranchingOptimizer;
import com.tazadum.glsl.optimizer.OptimizationDecider;
import com.tazadum.glsl.optimizer.OptimizerVisitor;
import com.tazadum.glsl.parser.ParserContext;

/**
 * Created by Erik on 2016-10-20.
 */
public class FunctionInlineOptimizer extends BranchingOptimizer {
    @Override
    public String name() {
        return "function inlines";
    }

    @Override
    OptimizerVisitor createVisitor(ParserContext context, OptimizationDecider decider) {
        return new FunctionInlineVisitor(context, decider);
    }
}
