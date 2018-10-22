package com.tazadum.glsl.parser.functions;

import com.tazadum.glsl.language.function.FunctionRegistry;
import com.tazadum.glsl.language.type.GenTypes;

import static com.tazadum.glsl.language.type.GenTypes.*;
import static com.tazadum.glsl.language.type.PredefinedType.*;

/**
 * Common Functions
 * Created by erikb on 2018-10-22.
 */
public class CommonFunctionSet extends FunctionSet {
    public CommonFunctionSet(FunctionRegistry functionRegistry) {
        super(functionRegistry);
    }

    @Override
    public void generate() {
        function("abs", GenFType, GenFType);
        function("abs", GenDType, GenDType);
        function("abs", GenIType, GenIType);
        function("sign", GenFType, GenFType);
        function("sign", GenDType, GenDType);
        function("sign", GenIType, GenIType);
        function("floor", GenFType, GenFType);
        function("floor", GenDType, GenDType);
        function("trunc", GenFType, GenFType);
        function("trunc", GenDType, GenDType);
        function("round", GenFType, GenFType);
        function("round", GenDType, GenDType);
        function("roundEven", GenFType, GenFType);
        function("roundEven", GenDType, GenDType);
        function("ceil", GenFType, GenFType);
        function("ceil", GenDType, GenDType);
        function("fract", GenFType, GenFType);
        function("fract", GenDType, GenDType);

        function("mod", GenFType, GenFType, FLOAT);
        function("mod", GenFType, GenFType, GenFType);
        function("mod", GenDType, GenDType, DOUBLE);
        function("mod", GenDType, GenDType, GenDType);
        function("modf", GenFType, GenFType, GenFType);
        function("modf", GenDType, GenDType, GenDType);

        function("min", GenFType, GenFType, GenFType);
        function("min", GenFType, GenFType, FLOAT);
        function("min", GenDType, GenDType, GenDType);
        function("min", GenDType, GenDType, DOUBLE);
        function("min", GenIType, GenIType, GenIType);
        function("min", GenIType, GenIType, INT);
        function("min", GenUType, GenUType, GenUType);
        function("min", GenUType, GenUType, UINT);

        function("max", GenFType, GenFType, GenFType);
        function("max", GenFType, GenFType, FLOAT);
        function("max", GenDType, GenDType, GenDType);
        function("max", GenDType, GenDType, DOUBLE);
        function("max", GenIType, GenIType, GenIType);
        function("max", GenIType, GenIType, INT);
        function("max", GenUType, GenUType, GenUType);
        function("max", GenUType, GenUType, UINT);

        function("clamp", GenFType, GenFType, GenFType, GenFType);
        function("clamp", GenFType, GenFType, GenFType, FLOAT);
        function("clamp", GenDType, GenDType, GenDType, GenDType);
        function("clamp", GenDType, GenDType, GenDType, DOUBLE);
        function("clamp", GenIType, GenIType, GenIType, GenIType);
        function("clamp", GenIType, GenIType, GenIType, INT);
        function("clamp", GenUType, GenUType, GenUType, GenUType);
        function("clamp", GenUType, GenUType, GenUType, UINT);

        function("mix", GenFType, GenFType, GenFType, GenFType);
        function("mix", GenFType, GenFType, GenFType, FLOAT);
        function("mix", GenDType, GenDType, GenDType, GenDType);
        function("mix", GenDType, GenDType, GenDType, DOUBLE);
        function("mix", GenIType, GenIType, GenIType, GenIType);
        function("mix", GenIType, GenIType, GenIType, INT);
        function("mix", GenUType, GenUType, GenUType, GenUType);
        function("mix", GenUType, GenUType, GenUType, UINT);

        function("step", GenFType, GenFType, GenFType);
        function("step", GenFType, FLOAT, GenFType);
        function("step", GenDType, GenDType, GenDType);
        function("step", GenDType, DOUBLE, GenDType);

        function("smoothstep", GenFType, GenFType, GenFType, GenFType);
        function("smoothstep", GenFType, FLOAT, FLOAT, GenFType);
        function("smoothstep", GenDType, GenDType, GenDType, GenDType);
        function("smoothstep", GenDType, DOUBLE, DOUBLE, GenDType);

        walk("isnan", GenBType, GenFType);
        walk("isnan", GenBType, GenDType);
        walk("isinf", GenBType, GenFType);
        walk("isinf", GenBType, GenDType);

        walk("floatBitsToInt", GenIType, GenFType);
        walk("floatBitsToUint", GenUType, GenIType);
        walk("intBitsToFloat", GenFType, GenIType);
        walk("uintBitsToFloat", GenFType, GenUType);

        function("fma", GenFType, GenFType, GenFType, GenFType);
        function("fma", GenDType, GenDType, GenDType, GenDType);

        // TODO: genFType frexp( genFType x, out genIType exp)
        // TODO: genDType frexp(genDType x, out genIType exp)
        // TODO: genFType ldexp( genFType x, genIType exp)
        // TODO: genDType ldexp(genDType x, genIType exp)
    }

    private void walk(String identifier, GenTypes returnValues, GenTypes parameterValues) {
        assert returnValues.types.length == parameterValues.types.length : "Wrong number of return and parameter types";
        for (int i = 0; i < parameterValues.types.length; i++) {
            function(identifier, returnValues.types[i], parameterValues.types[i]);
        }
    }
}
