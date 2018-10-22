package com.tazadum.glsl.parser.functions;

import com.tazadum.glsl.language.function.FunctionRegistry;

import static com.tazadum.glsl.language.type.GenTypes.GenFType;

/**
 * Angle and Trigonometry Functions
 * Created by erikb on 2018-10-22.
 */
public class TrigonometryFunctionSet extends FunctionSet {
    public TrigonometryFunctionSet(FunctionRegistry functionRegistry) {
        super(functionRegistry);
    }

    @Override
    public void generate() {
        function("radians", GenFType, GenFType);
        function("degrees", GenFType, GenFType);
        function("sin", GenFType, GenFType);
        function("cos", GenFType, GenFType);
        function("tan", GenFType, GenFType);
        function("asin", GenFType, GenFType);
        function("acos", GenFType, GenFType);
        function("atan", GenFType, GenFType);
        function("atan", GenFType, GenFType, GenFType);
        function("sinh", GenFType, GenFType);
        function("cosh", GenFType, GenFType);
        function("tanh", GenFType, GenFType);
        function("asinh", GenFType, GenFType);
        function("acosh", GenFType, GenFType);
        function("atanh", GenFType, GenFType);
    }
}
