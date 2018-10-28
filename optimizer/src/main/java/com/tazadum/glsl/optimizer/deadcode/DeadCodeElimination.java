package com.tazadum.glsl.optimizer.deadcode;

import com.tazadum.glsl.optimizer.BranchRegistry;
import com.tazadum.glsl.optimizer.BranchingOptimizer;
import com.tazadum.glsl.optimizer.OptimizationDecider;
import com.tazadum.glsl.optimizer.OptimizerVisitor;
import com.tazadum.glsl.parser.ParserContext;

/**
 * Removes dead code or declarations from the shader.
 * Created by Erik on 2016-10-23.
 */
public class DeadCodeElimination extends BranchingOptimizer {
    @Override
    public String name() {
        return "dead code";
    }

    @Override
    protected OptimizerVisitor createVisitor(ParserContext context, BranchRegistry branchRegistry, OptimizationDecider decider) {
        return new DeadCodeEliminationVisitor(context);
    }
}
