package com.tazadum.glsl.parser.functions;

import com.tazadum.glsl.language.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.language.function.BuiltInFunctionRegistry;
import com.tazadum.glsl.language.function.FunctionPrototype;
import com.tazadum.glsl.language.model.StorageQualifier;
import com.tazadum.glsl.language.type.FullySpecifiedType;
import com.tazadum.glsl.language.type.GenTypes;
import com.tazadum.glsl.language.type.PredefinedType;
import com.tazadum.glsl.util.SourcePosition;

import static com.tazadum.glsl.language.model.StorageQualifier.IN;
import static com.tazadum.glsl.language.model.StorageQualifier.OUT;
import static com.tazadum.glsl.language.type.GenTypes.*;
import static com.tazadum.glsl.language.type.PredefinedType.*;

/**
 * Common Functions
 * Created by erikb on 2018-10-22.
 */
public class CommonFunctionSet implements FunctionSet {
    @Override
    public void generate(BuiltInFunctionRegistry registry) {
        BuiltInFunctionRegistry.FunctionDeclarator declarator = registry.getFunctionDeclarator();

        declarator.function("abs", GenFType, GenFType);
        declarator.function("abs", GenDType, GenDType);
        declarator.function("abs", GenIType, GenIType);
        declarator.function("sign", GenFType, GenFType);
        declarator.function("sign", GenDType, GenDType);
        declarator.function("sign", GenIType, GenIType);
        declarator.function("floor", GenFType, GenFType);
        declarator.function("floor", GenDType, GenDType);
        declarator.function("trunc", GenFType, GenFType);
        declarator.function("trunc", GenDType, GenDType);
        declarator.function("round", GenFType, GenFType);
        declarator.function("round", GenDType, GenDType);
        declarator.function("roundEven", GenFType, GenFType);
        declarator.function("roundEven", GenDType, GenDType);
        declarator.function("ceil", GenFType, GenFType);
        declarator.function("ceil", GenDType, GenDType);
        declarator.function("fract", GenFType, GenFType);
        declarator.function("fract", GenDType, GenDType);

        declarator.function("mod", GenFType, GenFType, FLOAT);
        declarator.function("mod", GenFType, GenFType, GenFType);
        declarator.function("mod", GenDType, GenDType, DOUBLE);
        declarator.function("mod", GenDType, GenDType, GenDType);
        declarator.function("modf", GenFType, GenFType, GenFType);
        declarator.function("modf", GenDType, GenDType, GenDType);

        declarator.function("min", GenFType, GenFType, GenFType);
        declarator.function("min", GenFType, GenFType, FLOAT);
        declarator.function("min", GenDType, GenDType, GenDType);
        declarator.function("min", GenDType, GenDType, DOUBLE);
        declarator.function("min", GenIType, GenIType, GenIType);
        declarator.function("min", GenIType, GenIType, INT);
        declarator.function("min", GenUType, GenUType, GenUType);
        declarator.function("min", GenUType, GenUType, UINT);

        declarator.function("max", GenFType, GenFType, GenFType);
        declarator.function("max", GenFType, GenFType, FLOAT);
        declarator.function("max", GenDType, GenDType, GenDType);
        declarator.function("max", GenDType, GenDType, DOUBLE);
        declarator.function("max", GenIType, GenIType, GenIType);
        declarator.function("max", GenIType, GenIType, INT);
        declarator.function("max", GenUType, GenUType, GenUType);
        declarator.function("max", GenUType, GenUType, UINT);

        declarator.function("clamp", GenFType, GenFType, GenFType, GenFType);
        declarator.function("clamp", GenFType, GenFType, GenFType, FLOAT);
        declarator.function("clamp", GenDType, GenDType, GenDType, GenDType);
        declarator.function("clamp", GenDType, GenDType, GenDType, DOUBLE);
        declarator.function("clamp", GenIType, GenIType, GenIType, GenIType);
        declarator.function("clamp", GenIType, GenIType, GenIType, INT);
        declarator.function("clamp", GenUType, GenUType, GenUType, GenUType);
        declarator.function("clamp", GenUType, GenUType, GenUType, UINT);

        declarator.function("mix", GenFType, GenFType, GenFType, GenFType);
        declarator.function("mix", GenFType, GenFType, GenFType, FLOAT);
        declarator.function("mix", GenDType, GenDType, GenDType, GenDType);
        declarator.function("mix", GenDType, GenDType, GenDType, DOUBLE);
        declarator.function("mix", GenIType, GenIType, GenIType, GenIType);
        declarator.function("mix", GenIType, GenIType, GenIType, INT);
        declarator.function("mix", GenUType, GenUType, GenUType, GenUType);
        declarator.function("mix", GenUType, GenUType, GenUType, UINT);

        declarator.function("step", GenFType, GenFType, GenFType);
        declarator.function("step", GenFType, FLOAT, GenFType);
        declarator.function("step", GenDType, GenDType, GenDType);
        declarator.function("step", GenDType, DOUBLE, GenDType);

        declarator.function("smoothstep", GenFType, GenFType, GenFType, GenFType);
        declarator.function("smoothstep", GenFType, FLOAT, FLOAT, GenFType);
        declarator.function("smoothstep", GenDType, GenDType, GenDType, GenDType);
        declarator.function("smoothstep", GenDType, DOUBLE, DOUBLE, GenDType);

        walk(declarator, "isnan", GenBType, GenFType);
        walk(declarator, "isnan", GenBType, GenDType);
        walk(declarator, "isinf", GenBType, GenFType);
        walk(declarator, "isinf", GenBType, GenDType);

        walk(declarator, "floatBitsToInt", GenIType, GenFType);
        walk(declarator, "floatBitsToUint", GenUType, GenIType);
        walk(declarator, "intBitsToFloat", GenFType, GenIType);
        walk(declarator, "uintBitsToFloat", GenFType, GenUType);

        declarator.function("fma", GenFType, GenFType, GenFType, GenFType);
        declarator.function("fma", GenDType, GenDType, GenDType, GenDType);

        for (int i = 0; i < GenIType.types.length; i++) {
            PredefinedType iType = GenIType.types[i];
            PredefinedType fType = GenFType.types[i];
            PredefinedType dType = GenDType.types[i];

            fixedOutput(declarator, "frexp", fType, fType, iType);
            fixedOutput(declarator, "frexp", dType, dType, iType);
            declarator.function("ldexp", fType, fType, iType);
            declarator.function("ldexp", dType, dType, iType);
        }

        // add the unpack functions
        declarator.function("packUnorm2x16", UINT, VEC2);
        declarator.function("packSnorm2x16", UINT, VEC2);
        declarator.function("packUnorm4x8", UINT, VEC4);
        declarator.function("packSnorm4x8", UINT, VEC4);

        declarator.function("unpackUnorm2x16", VEC2, UINT);
        declarator.function("unpackSnorm2x16", VEC2, UINT);
        declarator.function("unpackUnorm4x8", VEC4, UINT);
        declarator.function("unpackSnorm4x8", VEC4, UINT);

        declarator.function("packHalf2x16", UINT, VEC2);
        declarator.function("unpackHalf2x16", VEC2, UINT);
        declarator.function("packDouble2x32", DOUBLE, UVEC2);
        declarator.function("unpackDouble2x32", UVEC2, DOUBLE);
    }

    private void walk(BuiltInFunctionRegistry.FunctionDeclarator declarator, String identifier, GenTypes returnValues, GenTypes parameterValues) {
        assert returnValues.types.length == parameterValues.types.length : "Wrong number of return and parameter types";
        for (int i = 0; i < parameterValues.types.length; i++) {
            declarator.function(identifier, returnValues.types[i], parameterValues.types[i]);
        }
    }

    private void fixedOutput(BuiltInFunctionRegistry.FunctionDeclarator declarator, String identifier, PredefinedType returnType, PredefinedType arg0, PredefinedType arg1) {
        final FunctionPrototypeNode node = new FunctionPrototypeNode(SourcePosition.TOP, identifier, new FullySpecifiedType(returnType));

        PredefinedType[] types = {arg0, arg1};
        StorageQualifier[] qualifiers = {IN, OUT};

        node.setPrototype(new FunctionPrototype(true, returnType, types, qualifiers));
        declarator.function(node);
    }
}
