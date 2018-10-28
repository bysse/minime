package com.tazadum.glsl.optimizer.constants;

import com.tazadum.glsl.optimizer.BranchRegistry;
import com.tazadum.glsl.optimizer.BranchingOptimizer;
import com.tazadum.glsl.optimizer.OptimizationDecider;
import com.tazadum.glsl.optimizer.OptimizerVisitor;
import com.tazadum.glsl.parser.ParserContext;

/**
 * Simplifies redundant constant expressions like vector constructions and swizzle operations.
 * Created by Erik on 2016-10-20.
 */
public class ConstantFolding extends BranchingOptimizer {
    @Override
    public String name() {
        return "constant folding";
    }

    @Override
    protected OptimizerVisitor createVisitor(ParserContext context, BranchRegistry branchRegistry, OptimizationDecider decider) {
        return new ConstantFoldingVisitor(context, branchRegistry, decider);
    }
}
