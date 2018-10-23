package com.tazadum.glsl.parser.functions;

import com.tazadum.glsl.language.function.BuiltInFunctionRegistry;
import com.tazadum.glsl.language.type.GenTypes;

import static com.tazadum.glsl.language.type.GenTypes.*;
import static com.tazadum.glsl.language.type.PredefinedType.*;

/**
 * Vector and Matrix Functions
 * Created by erikb on 2018-10-23.
 */
public class VectorAndMatrixFunctionSet implements FunctionSet {
    @Override
    public void generate(BuiltInFunctionRegistry registry) {
        BuiltInFunctionRegistry.FunctionDeclarator declarator = registry.getFunctionDeclarator();

        declarator.function("length", FLOAT, GenFType);
        declarator.function("length", DOUBLE, GenDType);
        declarator.function("distance", FLOAT, GenFType);
        declarator.function("distance", DOUBLE, GenDType);
        declarator.function("dot", FLOAT, GenFType, GenFType);
        declarator.function("dot", DOUBLE, GenDType, GenDType);
        declarator.function("cross", VEC3, VEC3, VEC3);
        declarator.function("cross", DVEC3, DVEC3, DVEC3);
        declarator.function("normalize", GenFType, GenFType);
        declarator.function("normalize", GenDType, GenDType);
        declarator.function("faceforward", GenFType, GenFType, GenFType, GenFType);
        declarator.function("faceforward", GenDType, GenDType, GenDType, GenDType);
        declarator.function("reflect", GenFType, GenFType, GenFType);
        declarator.function("reflect", GenDType, GenDType, GenDType);
        declarator.function("refract", GenFType, GenFType, GenFType, FLOAT);
        declarator.function("refract", GenDType, GenDType, GenDType, FLOAT);

        // matrix
        declarator.function("matrixCompMult", GenMatrixType, GenMatrixType, GenMatrixType);
        declarator.function("outerProduct", MAT2, VEC2, VEC2);
        declarator.function("outerProduct", MAT3, VEC3, VEC3);
        declarator.function("outerProduct", MAT4, VEC4, VEC4);

        declarator.function("transpose", GenFMatrixType, GenFMatrixType);
        declarator.function("determinant", GenFMatrixType, GenFMatrixType);
        declarator.function("inverse", GenFMatrixType, GenFMatrixType);

        // vector relational
        walk(declarator, "lessThan", GenBType, GenFType);
        walk(declarator, "lessThan", GenBType, GenIType);
        walk(declarator, "lessThan", GenBType, GenUType);

        walk(declarator, "lessThanEqual", GenBType, GenFType);
        walk(declarator, "lessThanEqual", GenBType, GenIType);
        walk(declarator, "lessThanEqual", GenBType, GenUType);

        walk(declarator, "greaterThan", GenBType, GenFType);
        walk(declarator, "greaterThan", GenBType, GenIType);
        walk(declarator, "greaterThan", GenBType, GenUType);

        walk(declarator, "greaterThanEqual", GenBType, GenFType);
        walk(declarator, "greaterThanEqual", GenBType, GenIType);
        walk(declarator, "greaterThanEqual", GenBType, GenUType);

        walk(declarator, "equal", GenBType, GenFType);
        walk(declarator, "equal", GenBType, GenIType);
        walk(declarator, "equal", GenBType, GenUType);

        walk(declarator, "notEqual", GenBType, GenFType);
        walk(declarator, "notEqual", GenBType, GenIType);
        walk(declarator, "notEqual", GenBType, GenUType);

        declarator.function("any", BOOL, GenBVectorType);
        declarator.function("all", BOOL, GenBVectorType);
        declarator.function("not", BOOL, GenBVectorType);
    }

    private void walk(BuiltInFunctionRegistry.FunctionDeclarator declarator, String identifier, GenTypes returnValues, GenTypes parameterValues) {
        assert returnValues.types.length == parameterValues.types.length : "Wrong number of return and parameter types";
        for (int i = 0; i < parameterValues.types.length; i++) {
            declarator.function(identifier, returnValues.types[i], parameterValues.types[i], parameterValues.types[i]);
        }
    }
}
