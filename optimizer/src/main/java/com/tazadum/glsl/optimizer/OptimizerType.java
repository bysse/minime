package com.tazadum.glsl.optimizer;

import com.tazadum.glsl.optimizer.constants.ConstantFolding;
import com.tazadum.glsl.optimizer.constants.ConstantPropagation;

public enum OptimizerType {
    //DeadCodeEliminationType(DeadCodeElimination.class),
    /**
     * Simplifies redundant constant expressions like vector constructions and swizzle operations.
     */
    ConstantFoldingType(ConstantFolding.class),
    /**
     * Propagates constants and resolves constant expressions.
     */
    ConstantPropagationType(ConstantPropagation.class),
    //DeclarationSqueezeType(DeclarationSqueeze.class),
    //ArithmeticOptimizerType(RuleOptimizer.class),
    //FunctionInline(FunctionInlineOptimizer.class)
    ;

    private Class<? extends Optimizer> optimizer;

    OptimizerType(Class<? extends Optimizer> optimizerType) {
        this.optimizer = optimizerType;
    }

    public Optimizer instantiate() {
        try {
            return optimizer.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to instantiate optimizer", e);
        }
    }
}
