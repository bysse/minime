package com.tazadum.glsl.parser.functions;

import com.tazadum.glsl.language.function.FunctionRegistry;

import static com.tazadum.glsl.language.type.GenTypes.GenFType;

/**
 * Exponential Functions
 * Created by erikb on 2018-10-22.
 */
public class ExponentialFunctionSet extends FunctionSet {
    public ExponentialFunctionSet(FunctionRegistry functionRegistry) {
        super(functionRegistry);
    }

    @Override
    public void generate() {
        function("pow", GenFType, GenFType, GenFType);
        function("exp", GenFType, GenFType);
        function("log", GenFType, GenFType);
        function("exp2", GenFType, GenFType);
        function("log2", GenFType, GenFType);
        function("sqrt", GenFType, GenFType);
        function("inversesqrt", GenFType, GenFType);
    }
}
