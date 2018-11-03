package com.tazadum.glsl.optimizer.inline;

import com.tazadum.glsl.optimizer.BranchRegistry;
import com.tazadum.glsl.optimizer.BranchingOptimizer;
import com.tazadum.glsl.optimizer.OptimizationDecider;
import com.tazadum.glsl.optimizer.OptimizerVisitor;
import com.tazadum.glsl.parser.ParserContext;

/**
 * Find appropriate functions to inline.
 * Created by Erik on 2016-10-20.
 */
public class FunctionInline extends BranchingOptimizer {
    @Override
    public String name() {
        return "inlining";
    }

    @Override
    protected OptimizerVisitor createVisitor(ParserContext context, BranchRegistry branchRegistry, OptimizationDecider decider) {
        return new FunctionInlineVisitor(context, branchRegistry);
    }
}
