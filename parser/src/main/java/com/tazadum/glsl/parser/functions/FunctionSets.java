package com.tazadum.glsl.parser.functions;

import com.tazadum.glsl.language.function.BuiltInFunctionRegistry;

/**
 * Created by erikb on 2018-10-23.
 */
public class FunctionSets {
    public static BuiltInFunctionRegistry applyFunctions(BuiltInFunctionRegistry registry) {
        new ConstructorsFunctionSet().generate(registry);
        new CommonFunctionSet().generate(registry);
        new ExponentialFunctionSet().generate(registry);
        new IntegerFunctionSet().generate(registry);
        new TextureFunctionSet().generate(registry);
        new TrigonometryFunctionSet().generate(registry);
        new VectorAndMatrixFunctionSet().generate(registry);

        return registry;
    }
}
