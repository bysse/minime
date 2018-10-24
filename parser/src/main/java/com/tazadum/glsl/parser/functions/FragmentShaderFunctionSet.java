package com.tazadum.glsl.parser.functions;

import com.tazadum.glsl.language.function.BuiltInFunctionRegistry;
import com.tazadum.glsl.preprocessor.language.GLSLProfile;

import static com.tazadum.glsl.language.type.GenTypes.GenFType;
import static com.tazadum.glsl.language.type.PredefinedType.INT;
import static com.tazadum.glsl.language.type.PredefinedType.VEC2;

/**
 * Fragment Processing Functions
 * Created by erikb on 2018-10-24.
 */
public class FragmentShaderFunctionSet implements FunctionSet {

    @Override
    public void generate(BuiltInFunctionRegistry registry, GLSLProfile profile) {
        BuiltInFunctionRegistry.FunctionDeclarator def = registry.getFunctionDeclarator();

        // Derivative Functions
        def.function("dFdx", GenFType, GenFType);
        def.function("dFdy", GenFType, GenFType);
        def.function("dFdxFine", GenFType, GenFType);
        def.function("dFdyFine", GenFType, GenFType);
        def.function("dFdxCoarse", GenFType, GenFType);
        def.function("dFdyCoarse", GenFType, GenFType);
        def.function("fwidth", GenFType, GenFType);
        def.function("fwidthFine", GenFType, GenFType);
        def.function("fwidthCoarse", GenFType, GenFType);

        // Interpolation Functions
        def.function("interpolateAtCentroid", GenFType, GenFType);
        def.function("interpolateAtSample", GenFType, GenFType, INT);
        def.function("interpolateAtOffset", GenFType, GenFType, VEC2);

        // Noise Functions
        // def.function("noise1", FLOAT, GenFType);
        // def.function("noise2", FLOAT, GenFType);
        // def.function("noise3", FLOAT, GenFType);
        // def.function("noise4", FLOAT, GenFType);

    }
}
