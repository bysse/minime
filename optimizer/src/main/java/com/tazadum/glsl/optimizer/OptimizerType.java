package com.tazadum.glsl.optimizer;

import com.tazadum.glsl.optimizer.constants.ConstantFolding;
import com.tazadum.glsl.optimizer.constants.ConstantPropagation;
import com.tazadum.glsl.optimizer.deadcode.DeadCodeElimination;
import com.tazadum.glsl.optimizer.simplification.RuleOptimizer;
import com.tazadum.glsl.optimizer.squeeze.DeclarationSqueeze;

import java.lang.reflect.InvocationTargetException;

public enum OptimizerType {
    /**
     * Removes dead code or declarations from the shader.
     */
    DeadCodeEliminationType(DeadCodeElimination.class),
    /**
     * Simplifies redundant constant expressions like vector constructions and swizzle operations.
     */
    ConstantFoldingType(ConstantFolding.class),
    /**
     * Propagates constants and resolves constant expressions.
     */
    ConstantPropagationType(ConstantPropagation.class),
    /**
     * Squeezes multiple variable declarations into a single line.
     */
    DeclarationSqueezeType(DeclarationSqueeze.class),
    /**
     * Pattern matching based arithmetic simplifications.
     */
    ArithmeticOptimizerType(RuleOptimizer.class),
    /**
     * Find appropriate functions to inline.
     */
    FunctionInline(com.tazadum.glsl.optimizer.inline.FunctionInline.class);

    private final Class<? extends Optimizer> optimizer;

    OptimizerType(Class<? extends Optimizer> optimizerType) {
        this.optimizer = optimizerType;
    }

    public Optimizer instantiate() {
        try {
            return optimizer.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Failed to instantiate optimizer", e);
        }
    }
}
