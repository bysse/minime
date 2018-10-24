package com.tazadum.glsl.parser.functions;

import com.tazadum.glsl.language.function.BuiltInFunctionRegistry;
import com.tazadum.glsl.preprocessor.language.GLSLProfile;

import static com.tazadum.glsl.language.type.GenTypes.GenFType;

/**
 * Exponential Functions
 * Created by erikb on 2018-10-22.
 */
public class ExponentialFunctionSet implements FunctionSet {
    @Override
    public void generate(BuiltInFunctionRegistry registry, GLSLProfile profile) {
        BuiltInFunctionRegistry.FunctionDeclarator declarator = registry.getFunctionDeclarator();

        declarator.function("pow", GenFType, GenFType, GenFType);
        declarator.function("exp", GenFType, GenFType);
        declarator.function("log", GenFType, GenFType);
        declarator.function("exp2", GenFType, GenFType);
        declarator.function("log2", GenFType, GenFType);
        declarator.function("sqrt", GenFType, GenFType);
        declarator.function("inversesqrt", GenFType, GenFType);
    }
}
