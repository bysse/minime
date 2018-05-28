package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.parser.ParserContext;

/**
 * Created by Erik on 2016-10-20.
 */
public class ConstantFolding extends BranchingOptimizer {
    @Override
    public String name() {
        return "constant folding replacements";
    }

    @Override
    OptimizerVisitor createVisitor(BranchRegistry branchRegistry, ParserContext context, OptimizationDecider decider) {
        return new ConstantFoldingVisitor(context, decider);
    }
}
