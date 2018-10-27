package com.tazadum.glsl.optimizer.constants;

import com.tazadum.glsl.optimizer.BranchRegistry;
import com.tazadum.glsl.optimizer.BranchingOptimizer;
import com.tazadum.glsl.optimizer.OptimizationDecider;
import com.tazadum.glsl.optimizer.OptimizerVisitor;
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
    protected OptimizerVisitor createVisitor(ParserContext context, BranchRegistry branchRegistry, OptimizationDecider decider) {
        return new ConstantPropagationVisitor(context, branchRegistry, decider);
    }
}
