package com.tazadum.glsl.parser.functions;

import com.tazadum.glsl.language.function.BuiltInFunctionRegistry;
import com.tazadum.glsl.language.type.PredefinedType;

import static com.tazadum.glsl.language.type.GenTypes.*;
import static com.tazadum.glsl.language.type.PredefinedType.*;

/**
 * Texture Functions
 * Created by erikb on 2018-10-22.
 */
public class TextureFunctionSet implements FunctionSet {
    @Override
    public void generate(BuiltInFunctionRegistry registry) {
        BuiltInFunctionRegistry.FunctionDeclarator def = registry.getFunctionDeclarator();

        // Texture Query Functions
        def.function("textureSize", INT, GenSampler1D, INT);
        def.function("textureSize", IVEC2, GenSampler2D, INT);
        def.function("textureSize", IVEC3, GenSampler3D, INT);
        def.function("textureSize", IVEC2, GenSamplerCube, INT);
        def.function("textureSize", IVEC2, GenSampler2DMS);
        def.function("textureSize", INT, GenSampleBuffer);
        def.function("textureSize", IVEC2, GenSampler2DRect);
        def.function("textureSize", IVEC2, SAMPLER2DRECTSHADOW);
        def.function("textureSize", IVEC2, SAMPLER1DSHADOW, INT);
        def.function("textureSize", IVEC2, SAMPLER2DSHADOW, INT);
        def.function("textureSize", IVEC2, SAMPLERCUBESHADOW, INT);
        def.function("textureSize", IVEC2, GenSampler1DArray, INT);
        def.function("textureSize", IVEC3, GenSampler2DArray, INT);
        def.function("textureSize", IVEC3, GenSamplerCubeArray, INT);
        def.function("textureSize", IVEC3, GenSampler2DMSArray);
        def.function("textureSize", IVEC3, SAMPLERCUBEARRAYSHADOW, INT);
        def.function("textureSize", IVEC2, SAMPLER1DARRAYSHADOW, INT);
        def.function("textureSize", IVEC3, SAMPLER2DARRAYSHADOW, INT);

        def.function("textureQueryLod", VEC2, GenSampler1D, FLOAT);
        def.function("textureQueryLod", VEC2, GenSampler2D, VEC2);
        def.function("textureQueryLod", VEC2, GenSampler3D, VEC3);
        def.function("textureQueryLod", VEC2, GenSamplerCube, VEC3);
        def.function("textureQueryLod", VEC2, SAMPLER1DSHADOW, FLOAT);
        def.function("textureQueryLod", VEC2, SAMPLER2DSHADOW, VEC2);
        def.function("textureQueryLod", VEC2, SAMPLERCUBESHADOW, VEC3);
        def.function("textureQueryLod", VEC2, GenSampler1DArray, FLOAT);
        def.function("textureQueryLod", VEC2, GenSampler2DArray, VEC2);
        def.function("textureQueryLod", VEC2, GenSamplerCubeArray, VEC3);
        def.function("textureQueryLod", VEC2, SAMPLER1DARRAYSHADOW, FLOAT);
        def.function("textureQueryLod", VEC2, SAMPLER2DARRAYSHADOW, VEC2);
        def.function("textureQueryLod", VEC2, SAMPLERCUBEARRAYSHADOW, VEC3);

        def.function("textureQueryLevels", INT, GenSampler1D);
        def.function("textureQueryLevels", INT, GenSampler2D);
        def.function("textureQueryLevels", INT, GenSampler3D);
        def.function("textureQueryLevels", INT, GenSamplerCube);
        def.function("textureQueryLevels", INT, GenSampler1DArray);
        def.function("textureQueryLevels", INT, GenSampler2DArray);
        def.function("textureQueryLevels", INT, GenSamplerCubeArray);
        def.function("textureQueryLevels", INT, SAMPLER1DARRAYSHADOW);
        def.function("textureQueryLevels", INT, SAMPLER2DARRAYSHADOW);
        def.function("textureQueryLevels", INT, SAMPLERCUBEARRAYSHADOW);

        def.function("textureSamples", INT, GenSampler2DMS);
        def.function("textureSamples", INT, GenSampler2DMSArray);

        // Textel Lookup Functions

        for (int i = 0; i < GenSampler1D.types.length; i++) {
            PredefinedType gvec4 = GenVec4Type.types[i];

            PredefinedType gsampler1D = GenSampler1D.types[i];
            PredefinedType gsampler2D = GenSampler2D.types[i];
            PredefinedType gsampler3D = GenSampler3D.types[i];
            PredefinedType gsamplerCube = GenSamplerCube.types[i];
            PredefinedType gsampler2DRect = GenSampler2DRect.types[i];
            PredefinedType gsampler1DArray = GenSampler1DArray.types[i];
            PredefinedType gsampler2DArray = GenSampler2DArray.types[i];
            PredefinedType gsamplerCubeArray = GenSamplerCubeArray.types[i];
            PredefinedType gsamplerBuffer = GenSampleBuffer.types[i];
            PredefinedType gsampler2DMS = GenSampler2DMS.types[i];
            PredefinedType gsampler2DMSArray = GenSampler2DMSArray.types[i];

            def.function("texture", gvec4, gsampler1D, FLOAT);
            def.function("texture", gvec4, gsampler1D, FLOAT, FLOAT);
            def.function("texture", gvec4, gsampler2D, VEC2);
            def.function("texture", gvec4, gsampler2D, VEC2, FLOAT);
            def.function("texture", gvec4, gsampler3D, VEC3);
            def.function("texture", gvec4, gsampler3D, VEC3, FLOAT);
            def.function("texture", gvec4, gsamplerCube, VEC3);
            def.function("texture", gvec4, gsamplerCube, VEC3, FLOAT);
            def.function("texture", gvec4, gsampler2DRect, VEC2);
            def.function("texture", gvec4, gsampler1DArray, VEC2);
            def.function("texture", gvec4, gsampler1DArray, VEC2, FLOAT);
            def.function("texture", gvec4, gsampler2DArray, VEC3);
            def.function("texture", gvec4, gsampler2DArray, VEC3, FLOAT);
            def.function("texture", gvec4, gsamplerCubeArray, VEC4);
            def.function("texture", gvec4, gsamplerCubeArray, VEC4, FLOAT);

            def.function("textureProj", gvec4, gsampler1D, VEC2);
            def.function("textureProj", gvec4, gsampler1D, VEC2, FLOAT);
            def.function("textureProj", gvec4, gsampler1D, VEC4);
            def.function("textureProj", gvec4, gsampler1D, VEC4, FLOAT);
            def.function("textureProj", gvec4, gsampler2D, VEC3);
            def.function("textureProj", gvec4, gsampler2D, VEC3, FLOAT);
            def.function("textureProj", gvec4, gsampler2D, VEC4);
            def.function("textureProj", gvec4, gsampler2D, VEC4, FLOAT);
            def.function("textureProj", gvec4, gsampler3D, VEC4);
            def.function("textureProj", gvec4, gsampler3D, VEC4, FLOAT);
            def.function("textureProj", gvec4, gsampler2DRect, VEC3);
            def.function("textureProj", gvec4, gsampler2DRect, VEC4);

            def.function("textureLod", gvec4, gsampler1D, FLOAT);
            def.function("textureLod", gvec4, gsampler2D, VEC2);
            def.function("textureLod", gvec4, gsampler3D, VEC3);
            def.function("textureLod", gvec4, gsamplerCube, VEC3);
            def.function("textureLod", gvec4, gsampler1DArray, VEC2, FLOAT);
            def.function("textureLod", gvec4, gsampler2DArray, VEC3, FLOAT);
            def.function("textureLod", gvec4, gsampler2DArray, VEC4, FLOAT);

            def.function("textureOffset", gvec4, gsampler1D, FLOAT, INT);
            def.function("textureOffset", gvec4, gsampler1D, FLOAT, INT, FLOAT);
            def.function("textureOffset", gvec4, gsampler2D, VEC2, IVEC2);
            def.function("textureOffset", gvec4, gsampler2D, VEC2, IVEC2, FLOAT);
            def.function("textureOffset", gvec4, gsampler3D, VEC3, IVEC3);
            def.function("textureOffset", gvec4, gsampler3D, VEC3, IVEC3, FLOAT);
            def.function("textureOffset", gvec4, gsampler2DRect, VEC2, IVEC2);

            def.function("textureOffset", gvec4, gsampler1DArray, VEC2, INT);
            def.function("textureOffset", gvec4, gsampler1DArray, VEC2, INT, FLOAT);
            def.function("textureOffset", gvec4, gsampler2DArray, VEC3, IVEC2);
            def.function("textureOffset", gvec4, gsampler2DArray, VEC3, IVEC2, FLOAT);

            def.function("texelFetch", gvec4, gsampler1D, INT, INT);
            def.function("texelFetch", gvec4, gsampler2D, IVEC2, INT);
            def.function("texelFetch", gvec4, gsampler3D, IVEC3, INT);
            def.function("texelFetch", gvec4, gsampler1DArray, IVEC2, INT);
            def.function("texelFetch", gvec4, gsampler2DArray, IVEC3, INT);
            def.function("texelFetch", gvec4, gsamplerBuffer, INT, INT);
            def.function("texelFetch", gvec4, gsampler2DMS, IVEC2, INT);
            def.function("texelFetch", gvec4, gsampler2DMSArray, IVEC3, INT);

            def.function("texelFetchOffset", gvec4, gsampler1D, INT, INT, INT);
            def.function("texelFetchOffset", gvec4, gsampler2D, IVEC2, INT, IVEC2);
            def.function("texelFetchOffset", gvec4, gsampler3D, IVEC3, INT, IVEC3);
            def.function("texelFetchOffset", gvec4, gsampler2DRect, IVEC2, IVEC2);
            def.function("texelFetchOffset", gvec4, gsampler1DArray, IVEC2, INT, INT);
            def.function("texelFetchOffset", gvec4, gsampler2DArray, IVEC3, INT, IVEC2);

            def.function("textureProjOffset", gvec4, gsampler1D, VEC2, INT);
            def.function("textureProjOffset", gvec4, gsampler1D, VEC2, INT, FLOAT);
            def.function("textureProjOffset", gvec4, gsampler1D, VEC4, INT);
            def.function("textureProjOffset", gvec4, gsampler1D, VEC4, INT, FLOAT);
            def.function("textureProjOffset", gvec4, gsampler2D, VEC3, IVEC2);
            def.function("textureProjOffset", gvec4, gsampler2D, VEC3, IVEC2, FLOAT);
            def.function("textureProjOffset", gvec4, gsampler2D, VEC4, IVEC2);
            def.function("textureProjOffset", gvec4, gsampler2D, VEC4, IVEC2, FLOAT);
            def.function("textureProjOffset", gvec4, gsampler3D, VEC4, IVEC3);
            def.function("textureProjOffset", gvec4, gsampler3D, VEC4, IVEC3, FLOAT);
            def.function("textureProjOffset", gvec4, gsampler2DRect, VEC3, IVEC2);
            def.function("textureProjOffset", gvec4, gsampler2DRect, VEC4, IVEC2);

            def.function("textureLodOffset", gvec4, gsampler1D, FLOAT, FLOAT, INT);
            def.function("textureLodOffset", gvec4, gsampler2D, VEC2, FLOAT, IVEC2);
            def.function("textureLodOffset", gvec4, gsampler3D, VEC3, FLOAT, IVEC3);
            def.function("textureLodOffset", gvec4, gsampler1DArray, VEC2, FLOAT, INT);
            def.function("textureLodOffset", gvec4, gsampler2DArray, VEC3, FLOAT, IVEC2);

            def.function("textureProjLod", gvec4, gsampler1D, VEC2, FLOAT);
            def.function("textureProjLod", gvec4, gsampler1D, VEC4, FLOAT);
            def.function("textureProjLod", gvec4, gsampler2D, VEC3, FLOAT);
            def.function("textureProjLod", gvec4, gsampler2D, VEC4, FLOAT);
            def.function("textureProjLod", gvec4, gsampler3D, VEC4, FLOAT);

            def.function("textureProjLodOffset", gvec4, gsampler1D, VEC2, FLOAT, INT);
            def.function("textureProjLodOffset", gvec4, gsampler1D, VEC4, FLOAT, INT);
            def.function("textureProjLodOffset", gvec4, gsampler2D, VEC3, FLOAT, IVEC2);
            def.function("textureProjLodOffset", gvec4, gsampler2D, VEC4, FLOAT, IVEC2);
            def.function("textureProjLodOffset", gvec4, gsampler3D, VEC4, FLOAT, IVEC3);

            def.function("textureGrad", gvec4, gsampler1D, FLOAT, FLOAT, FLOAT);
            def.function("textureGrad", gvec4, gsampler2D, VEC2, VEC2, VEC2);
            def.function("textureGrad", gvec4, gsampler3D, VEC3, VEC3, VEC3);
            def.function("textureGrad", gvec4, gsamplerCube, VEC3, VEC3, VEC3);
            def.function("textureGrad", gvec4, gsampler2DRect, VEC2, VEC2, VEC2);
            def.function("textureGrad", gvec4, gsampler1DArray, VEC2, FLOAT, FLOAT);
            def.function("textureGrad", gvec4, gsampler2DArray, VEC3, VEC2, VEC2);
            def.function("textureGrad", gvec4, gsamplerCubeArray, VEC4, VEC3, VEC3);

            def.function("textureGradOffset", gvec4, gsampler1D, FLOAT, FLOAT, FLOAT, INT);
            def.function("textureGradOffset", gvec4, gsampler2D, VEC2, VEC2, VEC2, IVEC2);
            def.function("textureGradOffset", gvec4, gsampler3D, VEC3, VEC3, VEC3, IVEC3);
            def.function("textureGradOffset", gvec4, gsampler2DRect, VEC2, VEC2, VEC2, IVEC2);
            def.function("textureGradOffset", gvec4, gsampler1DArray, VEC2, FLOAT, FLOAT, INT);
            def.function("textureGradOffset", gvec4, gsampler2DArray, VEC3, VEC2, VEC2, IVEC2);

            def.function("textureProjGrad", gvec4, gsampler1D, VEC2, FLOAT, FLOAT);
            def.function("textureProjGrad", gvec4, gsampler1D, VEC4, FLOAT, FLOAT);
            def.function("textureProjGrad", gvec4, gsampler2D, VEC2, VEC2, VEC2);
            def.function("textureProjGrad", gvec4, gsampler2D, VEC4, VEC2, VEC2);
            def.function("textureProjGrad", gvec4, gsampler3D, VEC4, VEC3, VEC3);
            def.function("textureProjGrad", gvec4, gsampler2DRect, VEC3, VEC2, VEC2);
            def.function("textureProjGrad", gvec4, gsampler2DRect, VEC4, VEC2, VEC2);

            def.function("textureProjGradOffset", gvec4, gsampler1D, VEC2, FLOAT, FLOAT, INT);
            def.function("textureProjGradOffset", gvec4, gsampler1D, VEC4, FLOAT, FLOAT, INT);
            def.function("textureProjGradOffset", gvec4, gsampler2D, VEC2, VEC2, VEC2, IVEC2);
            def.function("textureProjGradOffset", gvec4, gsampler2D, VEC4, VEC2, VEC2, IVEC2);
            def.function("textureProjGradOffset", gvec4, gsampler3D, VEC4, VEC3, VEC3, IVEC3);
            def.function("textureProjGradOffset", gvec4, gsampler2DRect, VEC3, VEC2, VEC2, IVEC2);
            def.function("textureProjGradOffset", gvec4, gsampler2DRect, VEC4, VEC2, VEC2, IVEC2);

        }

        def.function("texture", FLOAT, SAMPLER1DSHADOW, VEC3);
        def.function("texture", FLOAT, SAMPLER1DSHADOW, VEC3, FLOAT);
        def.function("texture", FLOAT, SAMPLER2DSHADOW, VEC3);
        def.function("texture", FLOAT, SAMPLER2DSHADOW, VEC3, FLOAT);
        def.function("texture", FLOAT, SAMPLERCUBESHADOW, VEC4);
        def.function("texture", FLOAT, SAMPLERCUBESHADOW, VEC4, FLOAT);
        def.function("texture", FLOAT, SAMPLER1DARRAYSHADOW, VEC2);
        def.function("texture", FLOAT, SAMPLER1DARRAYSHADOW, VEC2, FLOAT);
        def.function("texture", FLOAT, SAMPLER2DARRAYSHADOW, VEC4);
        def.function("texture", FLOAT, SAMPLERCUBEARRAYSHADOW, VEC4, FLOAT);
        def.function("texture", FLOAT, SAMPLER2DRECTSHADOW, VEC3);

        def.function("textureProj", FLOAT, SAMPLER1DSHADOW, VEC4);
        def.function("textureProj", FLOAT, SAMPLER1DSHADOW, VEC4, FLOAT);
        def.function("textureProj", FLOAT, SAMPLER2DSHADOW, VEC4);
        def.function("textureProj", FLOAT, SAMPLER2DSHADOW, VEC4, FLOAT);
        def.function("textureProj", FLOAT, SAMPLER2DRECTSHADOW, VEC4);

        def.function("textureLod", FLOAT, SAMPLER1DSHADOW, VEC3, FLOAT);
        def.function("textureLod", FLOAT, SAMPLER2DSHADOW, VEC3, FLOAT);

        def.function("textureOffset", FLOAT, SAMPLER1DSHADOW, VEC3, INT);
        def.function("textureOffset", FLOAT, SAMPLER1DSHADOW, VEC3, INT, FLOAT);
        def.function("textureOffset", FLOAT, SAMPLER2DSHADOW, VEC3, IVEC2);
        def.function("textureOffset", FLOAT, SAMPLER2DSHADOW, VEC3, IVEC2, FLOAT);
        def.function("textureOffset", FLOAT, SAMPLER1DARRAYSHADOW, VEC3, INT);
        def.function("textureOffset", FLOAT, SAMPLER1DARRAYSHADOW, VEC3, INT, FLOAT);
        def.function("textureOffset", FLOAT, SAMPLER2DARRAYSHADOW, VEC4, IVEC2);
        def.function("textureOffset", FLOAT, SAMPLER2DSHADOW, VEC3, IVEC2);

        def.function("textureProjOffset", FLOAT, SAMPLER2DRECTSHADOW, VEC4, IVEC2);
        def.function("textureProjOffset", FLOAT, SAMPLER1DSHADOW, VEC4, INT);
        def.function("textureProjOffset", FLOAT, SAMPLER1DSHADOW, VEC4, INT, FLOAT);
        def.function("textureProjOffset", FLOAT, SAMPLER2DSHADOW, VEC4, IVEC2);
        def.function("textureProjOffset", FLOAT, SAMPLER2DSHADOW, VEC4, IVEC2, FLOAT);

        def.function("textureLodOffset", FLOAT, SAMPLER1DSHADOW, VEC3, FLOAT, INT);
        def.function("textureLodOffset", FLOAT, SAMPLER2DSHADOW, VEC3, FLOAT, IVEC2);
        def.function("textureLodOffset", FLOAT, SAMPLER1DARRAYSHADOW, VEC3, FLOAT, INT);

        def.function("textureProjLod", FLOAT, SAMPLER1DSHADOW, VEC4, FLOAT);
        def.function("textureProjLod", FLOAT, SAMPLER2DSHADOW, VEC4, FLOAT);
        def.function("textureProjLodOffset", FLOAT, SAMPLER1DSHADOW, VEC4, FLOAT, INT);
        def.function("textureProjLodOffset", FLOAT, SAMPLER2DSHADOW, VEC4, FLOAT, IVEC2);

        def.function("textureGrad", FLOAT, SAMPLER1DSHADOW, VEC3, FLOAT, FLOAT);
        def.function("textureGrad", FLOAT, SAMPLER2DSHADOW, VEC3, VEC2, VEC2);
        def.function("textureGrad", FLOAT, SAMPLERCUBESHADOW, VEC4, VEC3, VEC3);
        def.function("textureGrad", FLOAT, SAMPLER2DRECTSHADOW, VEC3, VEC2, VEC2);
        def.function("textureGrad", FLOAT, SAMPLER1DSHADOW, VEC3, FLOAT, FLOAT);
        def.function("textureGrad", FLOAT, SAMPLER2DSHADOW, VEC4, VEC2, VEC2);

        def.function("textureGradOffset", FLOAT, SAMPLER1DSHADOW, VEC3, FLOAT, FLOAT, INT);
        def.function("textureGradOffset", FLOAT, SAMPLER2DSHADOW, VEC3, VEC2, VEC2, IVEC2);
        def.function("textureGradOffsetGrad", FLOAT, SAMPLER2DRECTSHADOW, VEC3, VEC2, VEC2, IVEC2);
        def.function("textureGradOffset", FLOAT, SAMPLER1DSHADOW, VEC3, FLOAT, FLOAT, INT);
        def.function("textureGrad", FLOAT, SAMPLER2DSHADOW, VEC4, VEC2, VEC2, IVEC2);

        def.function("textureProjGrad", FLOAT, SAMPLER1DSHADOW, VEC4, FLOAT, FLOAT);
        def.function("textureProjGrad", FLOAT, SAMPLER2DSHADOW, VEC4, VEC2, VEC2);
        def.function("textureProjGrad", FLOAT, SAMPLER2DRECTSHADOW, VEC4, VEC2, VEC2);

        def.function("textureProjGradOffset", FLOAT, SAMPLER1DSHADOW, VEC4, FLOAT, FLOAT, INT);
        def.function("textureProjGradOffset", FLOAT, SAMPLER2DSHADOW, VEC4, VEC2, VEC2, IVEC2);
        def.function("textureProjGradOffset", FLOAT, SAMPLER2DRECTSHADOW, VEC4, VEC2, VEC2, IVEC2);

        // Texture Gather Functions

        // Derivative Functions
        def.function("dFdx", GenFType, GenFType);
        def.function("dFdy", GenFType, GenFType);
        def.function("fwidth", GenFType, GenFType);
    }
}
