package com.tazadum.glsl.parser.functions;

import com.tazadum.glsl.language.function.BuiltInFunctionRegistry;
import com.tazadum.glsl.preprocessor.language.GLSLProfile;

import static com.tazadum.glsl.language.type.GenTypes.GenFType;

/**
 * Angle and Trigonometry Functions
 * Created by erikb on 2018-10-22.
 */
public class TrigonometryFunctionSet implements FunctionSet {

    public void generate(BuiltInFunctionRegistry registry, GLSLProfile profile) {
        BuiltInFunctionRegistry.FunctionDeclarator declarator = registry.getFunctionDeclarator();

        declarator.function("radians", GenFType, GenFType);
        declarator.function("degrees", GenFType, GenFType);
        declarator.function("sin", GenFType, GenFType);
        declarator.function("cos", GenFType, GenFType);
        declarator.function("tan", GenFType, GenFType);
        declarator.function("asin", GenFType, GenFType);
        declarator.function("acos", GenFType, GenFType);
        declarator.function("atan", GenFType, GenFType);
        declarator.function("atan", GenFType, GenFType, GenFType);
        declarator.function("sinh", GenFType, GenFType);
        declarator.function("cosh", GenFType, GenFType);
        declarator.function("tanh", GenFType, GenFType);
        declarator.function("asinh", GenFType, GenFType);
        declarator.function("acosh", GenFType, GenFType);
        declarator.function("atanh", GenFType, GenFType);
    }
}
