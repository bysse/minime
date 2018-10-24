package com.tazadum.glsl.parser.functions;

import com.tazadum.glsl.language.function.BuiltInFunctionRegistry;
import com.tazadum.glsl.language.type.ArrayType;
import com.tazadum.glsl.language.type.GenTypes;
import com.tazadum.glsl.language.type.PredefinedType;
import com.tazadum.glsl.preprocessor.language.GLSLProfile;

import static com.tazadum.glsl.language.type.GenTypes.*;
import static com.tazadum.glsl.language.type.PredefinedType.*;

/**
 * Texture Functions
 * Created by erikb on 2018-10-22.
 */
public class TextureFunctionSet implements FunctionSet {
    @Override
    public void generate(BuiltInFunctionRegistry registry, GLSLProfile profile) {
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
        GenTypes.iterateFUI((gscalar, gvec2, gvec3, gvec4, gsampler1d, gsampler2d, gsampler3d, gsamplerCube,
                             gsampler2dRect, gsampler2dMS, gsamplerBuffer, gsampler1dArray, gsampler2dArray,
                             gsamplerCubeArray, gsampler2dMSArray) -> {
            def.function("texture", gvec4, gsampler1d, FLOAT);
            def.function("texture", gvec4, gsampler1d, FLOAT, FLOAT);
            def.function("texture", gvec4, gsampler2d, VEC2);
            def.function("texture", gvec4, gsampler2d, VEC2, FLOAT);
            def.function("texture", gvec4, gsampler3d, VEC3);
            def.function("texture", gvec4, gsampler3d, VEC3, FLOAT);
            def.function("texture", gvec4, gsamplerCube, VEC3);
            def.function("texture", gvec4, gsamplerCube, VEC3, FLOAT);
            def.function("texture", gvec4, gsampler2dRect, VEC2);
            def.function("texture", gvec4, gsampler1dArray, VEC2);
            def.function("texture", gvec4, gsampler1dArray, VEC2, FLOAT);
            def.function("texture", gvec4, gsampler2dArray, VEC3);
            def.function("texture", gvec4, gsampler2dArray, VEC3, FLOAT);
            def.function("texture", gvec4, gsamplerCubeArray, VEC4);
            def.function("texture", gvec4, gsamplerCubeArray, VEC4, FLOAT);

            def.function("textureProj", gvec4, gsampler1d, VEC2);
            def.function("textureProj", gvec4, gsampler1d, VEC2, FLOAT);
            def.function("textureProj", gvec4, gsampler1d, VEC4);
            def.function("textureProj", gvec4, gsampler1d, VEC4, FLOAT);
            def.function("textureProj", gvec4, gsampler2d, VEC3);
            def.function("textureProj", gvec4, gsampler2d, VEC3, FLOAT);
            def.function("textureProj", gvec4, gsampler2d, VEC4);
            def.function("textureProj", gvec4, gsampler2d, VEC4, FLOAT);
            def.function("textureProj", gvec4, gsampler3d, VEC4);
            def.function("textureProj", gvec4, gsampler3d, VEC4, FLOAT);
            def.function("textureProj", gvec4, gsampler2dRect, VEC3);
            def.function("textureProj", gvec4, gsampler2dRect, VEC4);

            def.function("textureLod", gvec4, gsampler1d, FLOAT);
            def.function("textureLod", gvec4, gsampler2d, VEC2);
            def.function("textureLod", gvec4, gsampler3d, VEC3);
            def.function("textureLod", gvec4, gsamplerCube, VEC3);
            def.function("textureLod", gvec4, gsampler1dArray, VEC2, FLOAT);
            def.function("textureLod", gvec4, gsampler2dArray, VEC3, FLOAT);
            def.function("textureLod", gvec4, gsampler2dArray, VEC4, FLOAT);

            def.function("textureOffset", gvec4, gsampler1d, FLOAT, INT);
            def.function("textureOffset", gvec4, gsampler1d, FLOAT, INT, FLOAT);
            def.function("textureOffset", gvec4, gsampler2d, VEC2, IVEC2);
            def.function("textureOffset", gvec4, gsampler2d, VEC2, IVEC2, FLOAT);
            def.function("textureOffset", gvec4, gsampler3d, VEC3, IVEC3);
            def.function("textureOffset", gvec4, gsampler3d, VEC3, IVEC3, FLOAT);
            def.function("textureOffset", gvec4, gsampler2dRect, VEC2, IVEC2);

            def.function("textureOffset", gvec4, gsampler1dArray, VEC2, INT);
            def.function("textureOffset", gvec4, gsampler1dArray, VEC2, INT, FLOAT);
            def.function("textureOffset", gvec4, gsampler2dArray, VEC3, IVEC2);
            def.function("textureOffset", gvec4, gsampler2dArray, VEC3, IVEC2, FLOAT);

            def.function("texelFetch", gvec4, gsampler1d, INT, INT);
            def.function("texelFetch", gvec4, gsampler2d, IVEC2, INT);
            def.function("texelFetch", gvec4, gsampler3d, IVEC3, INT);
            def.function("texelFetch", gvec4, gsampler1dArray, IVEC2, INT);
            def.function("texelFetch", gvec4, gsampler2dArray, IVEC3, INT);
            def.function("texelFetch", gvec4, gsamplerBuffer, INT, INT);
            def.function("texelFetch", gvec4, gsampler2dMS, IVEC2, INT);
            def.function("texelFetch", gvec4, gsampler2dMSArray, IVEC3, INT);

            def.function("texelFetchOffset", gvec4, gsampler1d, INT, INT, INT);
            def.function("texelFetchOffset", gvec4, gsampler2d, IVEC2, INT, IVEC2);
            def.function("texelFetchOffset", gvec4, gsampler3d, IVEC3, INT, IVEC3);
            def.function("texelFetchOffset", gvec4, gsampler2dRect, IVEC2, IVEC2);
            def.function("texelFetchOffset", gvec4, gsampler1dArray, IVEC2, INT, INT);
            def.function("texelFetchOffset", gvec4, gsampler2dArray, IVEC3, INT, IVEC2);

            def.function("textureProjOffset", gvec4, gsampler1d, VEC2, INT);
            def.function("textureProjOffset", gvec4, gsampler1d, VEC2, INT, FLOAT);
            def.function("textureProjOffset", gvec4, gsampler1d, VEC4, INT);
            def.function("textureProjOffset", gvec4, gsampler1d, VEC4, INT, FLOAT);
            def.function("textureProjOffset", gvec4, gsampler2d, VEC3, IVEC2);
            def.function("textureProjOffset", gvec4, gsampler2d, VEC3, IVEC2, FLOAT);
            def.function("textureProjOffset", gvec4, gsampler2d, VEC4, IVEC2);
            def.function("textureProjOffset", gvec4, gsampler2d, VEC4, IVEC2, FLOAT);
            def.function("textureProjOffset", gvec4, gsampler3d, VEC4, IVEC3);
            def.function("textureProjOffset", gvec4, gsampler3d, VEC4, IVEC3, FLOAT);
            def.function("textureProjOffset", gvec4, gsampler2dRect, VEC3, IVEC2);
            def.function("textureProjOffset", gvec4, gsampler2dRect, VEC4, IVEC2);

            def.function("textureLodOffset", gvec4, gsampler1d, FLOAT, FLOAT, INT);
            def.function("textureLodOffset", gvec4, gsampler2d, VEC2, FLOAT, IVEC2);
            def.function("textureLodOffset", gvec4, gsampler3d, VEC3, FLOAT, IVEC3);
            def.function("textureLodOffset", gvec4, gsampler1dArray, VEC2, FLOAT, INT);
            def.function("textureLodOffset", gvec4, gsampler2dArray, VEC3, FLOAT, IVEC2);

            def.function("textureProjLod", gvec4, gsampler1d, VEC2, FLOAT);
            def.function("textureProjLod", gvec4, gsampler1d, VEC4, FLOAT);
            def.function("textureProjLod", gvec4, gsampler2d, VEC3, FLOAT);
            def.function("textureProjLod", gvec4, gsampler2d, VEC4, FLOAT);
            def.function("textureProjLod", gvec4, gsampler3d, VEC4, FLOAT);

            def.function("textureProjLodOffset", gvec4, gsampler1d, VEC2, FLOAT, INT);
            def.function("textureProjLodOffset", gvec4, gsampler1d, VEC4, FLOAT, INT);
            def.function("textureProjLodOffset", gvec4, gsampler2d, VEC3, FLOAT, IVEC2);
            def.function("textureProjLodOffset", gvec4, gsampler2d, VEC4, FLOAT, IVEC2);
            def.function("textureProjLodOffset", gvec4, gsampler3d, VEC4, FLOAT, IVEC3);

            def.function("textureGrad", gvec4, gsampler1d, FLOAT, FLOAT, FLOAT);
            def.function("textureGrad", gvec4, gsampler2d, VEC2, VEC2, VEC2);
            def.function("textureGrad", gvec4, gsampler3d, VEC3, VEC3, VEC3);
            def.function("textureGrad", gvec4, gsamplerCube, VEC3, VEC3, VEC3);
            def.function("textureGrad", gvec4, gsampler2dRect, VEC2, VEC2, VEC2);
            def.function("textureGrad", gvec4, gsampler1dArray, VEC2, FLOAT, FLOAT);
            def.function("textureGrad", gvec4, gsampler2dArray, VEC3, VEC2, VEC2);
            def.function("textureGrad", gvec4, gsamplerCubeArray, VEC4, VEC3, VEC3);

            def.function("textureGradOffset", gvec4, gsampler1d, FLOAT, FLOAT, FLOAT, INT);
            def.function("textureGradOffset", gvec4, gsampler2d, VEC2, VEC2, VEC2, IVEC2);
            def.function("textureGradOffset", gvec4, gsampler3d, VEC3, VEC3, VEC3, IVEC3);
            def.function("textureGradOffset", gvec4, gsampler2dRect, VEC2, VEC2, VEC2, IVEC2);
            def.function("textureGradOffset", gvec4, gsampler1dArray, VEC2, FLOAT, FLOAT, INT);
            def.function("textureGradOffset", gvec4, gsampler2dArray, VEC3, VEC2, VEC2, IVEC2);

            def.function("textureProjGrad", gvec4, gsampler1d, VEC2, FLOAT, FLOAT);
            def.function("textureProjGrad", gvec4, gsampler1d, VEC4, FLOAT, FLOAT);
            def.function("textureProjGrad", gvec4, gsampler2d, VEC2, VEC2, VEC2);
            def.function("textureProjGrad", gvec4, gsampler2d, VEC4, VEC2, VEC2);
            def.function("textureProjGrad", gvec4, gsampler3d, VEC4, VEC3, VEC3);
            def.function("textureProjGrad", gvec4, gsampler2dRect, VEC3, VEC2, VEC2);
            def.function("textureProjGrad", gvec4, gsampler2dRect, VEC4, VEC2, VEC2);

            def.function("textureProjGradOffset", gvec4, gsampler1d, VEC2, FLOAT, FLOAT, INT);
            def.function("textureProjGradOffset", gvec4, gsampler1d, VEC4, FLOAT, FLOAT, INT);
            def.function("textureProjGradOffset", gvec4, gsampler2d, VEC2, VEC2, VEC2, IVEC2);
            def.function("textureProjGradOffset", gvec4, gsampler2d, VEC4, VEC2, VEC2, IVEC2);
            def.function("textureProjGradOffset", gvec4, gsampler3d, VEC4, VEC3, VEC3, IVEC3);
            def.function("textureProjGradOffset", gvec4, gsampler2dRect, VEC3, VEC2, VEC2, IVEC2);
            def.function("textureProjGradOffset", gvec4, gsampler2dRect, VEC4, VEC2, VEC2, IVEC2);
        });

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
        GenTypes.iterateFUI((gscalar, gvec2, gvec3, gvec4, gsampler1d, gsampler2d, gsampler3d, gsamplerCube,
                             gsampler2dRect, gsampler2dMS, gsamplerBuffer, gsampler1dArray, gsampler2dArray,
                             gsamplerCubeArray, gsampler2dMSArray) -> {

            def.function("textureGather", gvec4, gsampler2d, VEC2);
            def.function("textureGather", gvec4, gsampler2d, VEC2, INT);
            def.function("textureGather", gvec4, gsampler2dArray, VEC3);
            def.function("textureGather", gvec4, gsampler2dArray, VEC3, INT);
            def.function("textureGather", gvec4, gsamplerCube, VEC3);
            def.function("textureGather", gvec4, gsamplerCube, VEC3, INT);
            def.function("textureGather", gvec4, gsamplerCubeArray, VEC4);
            def.function("textureGather", gvec4, gsamplerCubeArray, VEC4, INT);
            def.function("textureGather", gvec4, gsampler2dRect, VEC2);
            def.function("textureGather", gvec4, gsampler2dRect, VEC2, INT);

            def.function("textureGatherOffset", gvec4, gsampler2d, VEC2, IVEC2);
            def.function("textureGatherOffset", gvec4, gsampler2d, VEC2, IVEC2, INT);
            def.function("textureGatherOffset", gvec4, gsampler2dArray, VEC3, IVEC2);
            def.function("textureGatherOffset", gvec4, gsampler2dArray, VEC3, IVEC2, INT);
            def.function("textureGatherOffset", gvec4, gsampler2dRect, VEC2, IVEC2);
            def.function("textureGatherOffset", gvec4, gsampler2dRect, VEC2, IVEC2, INT);

            def.function("textureGatherOffsets", gvec4, gsampler2d, VEC2, array(IVEC2, 4));
            def.function("textureGatherOffsets", gvec4, gsampler2d, VEC2, array(IVEC2, 4), INT);
            def.function("textureGatherOffsets", gvec4, gsampler2dArray, VEC3, array(IVEC2, 4));
            def.function("textureGatherOffsets", gvec4, gsampler2dArray, VEC3, array(IVEC2, 4), INT);
            def.function("textureGatherOffsets", gvec4, gsampler2dRect, VEC2, array(IVEC2, 4));
            def.function("textureGatherOffsets", gvec4, gsampler2dRect, VEC2, array(IVEC2, 4), INT);

        });

        def.function("textureGather", FLOAT, SAMPLER2DSHADOW, VEC2, FLOAT);
        def.function("textureGather", FLOAT, SAMPLERCUBESHADOW, VEC3, FLOAT);
        def.function("textureGather", FLOAT, SAMPLER2DARRAYSHADOW, VEC3, FLOAT);
        def.function("textureGather", FLOAT, SAMPLERCUBEARRAYSHADOW, VEC4, FLOAT);
        def.function("textureGather", FLOAT, SAMPLER2DRECTSHADOW, VEC2, FLOAT);

        def.function("textureGatherOffset", FLOAT, SAMPLER2DSHADOW, VEC2, FLOAT, IVEC2);
        def.function("textureGatherOffset", FLOAT, SAMPLER2DARRAYSHADOW, VEC3, FLOAT, IVEC2);
        def.function("textureGatherOffset", FLOAT, SAMPLER2DRECTSHADOW, VEC2, FLOAT, IVEC2);

        def.function("textureGatherOffsets", FLOAT, SAMPLER2DSHADOW, VEC2, FLOAT, array(IVEC2, 4));
        def.function("textureGatherOffsets", FLOAT, SAMPLER2DARRAYSHADOW, VEC3, FLOAT, array(IVEC2, 4));
        def.function("textureGatherOffsets", FLOAT, SAMPLER2DRECTSHADOW, VEC2, FLOAT, array(IVEC2, 4));

        // Compatibility Profile Texture Functions
        if (profile == GLSLProfile.COMPATIBILITY) {
            def.function("texture1D", VEC4, SAMPLER1D, FLOAT);
            def.function("texture1D", VEC4, SAMPLER1D, FLOAT, FLOAT);
            def.function("texture1DProj", VEC4, SAMPLER1D, VEC2);
            def.function("texture1DProj", VEC4, SAMPLER1D, VEC2, FLOAT);
            def.function("texture1DProj", VEC4, SAMPLER1D, VEC4);
            def.function("texture1DProj", VEC4, SAMPLER1D, VEC2, FLOAT);
            def.function("texture1DLod", VEC4, SAMPLER1D, FLOAT, FLOAT);
            def.function("texture1DProjLod", VEC4, SAMPLER1D, VEC2, FLOAT);
            def.function("texture1DProjLod", VEC4, SAMPLER1D, VEC2, FLOAT);

            def.function("texture2D", VEC4, SAMPLER2D, VEC2);
            def.function("texture2D", VEC4, SAMPLER2D, VEC2, FLOAT);
            def.function("texture2DProj", VEC4, SAMPLER2D, VEC3);
            def.function("texture2DProj", VEC4, SAMPLER2D, VEC3, FLOAT);
            def.function("texture2DProj", VEC4, SAMPLER2D, VEC4);
            def.function("texture2DProj", VEC4, SAMPLER2D, VEC2, FLOAT);
            def.function("texture2DLod", VEC4, SAMPLER2D, VEC2, FLOAT);
            def.function("texture2DProjLod", VEC4, SAMPLER2D, VEC3, FLOAT);
            def.function("texture2DProjLod", VEC4, SAMPLER2D, VEC4, FLOAT);

            def.function("texture3D", VEC4, SAMPLER3D, VEC3);
            def.function("texture3D", VEC4, SAMPLER3D, VEC3, FLOAT);
            def.function("texture3DProj", VEC4, SAMPLER3D, VEC4);
            def.function("texture3DProj", VEC4, SAMPLER3D, VEC4, FLOAT);
            def.function("texture3DLod", VEC4, SAMPLER3D, VEC3);
            def.function("texture3DProjLod", VEC4, SAMPLER3D, VEC4, FLOAT);

            def.function("textureCube", VEC4, SAMPLERCUBE, VEC3);
            def.function("textureCube", VEC4, SAMPLERCUBE, VEC3, FLOAT);
            def.function("textureCubeLod", VEC4, SAMPLERCUBE, VEC3, FLOAT);

            def.function("shadow1D", VEC4, SAMPLER1DSHADOW, VEC3);
            def.function("shadow1D", VEC4, SAMPLER1DSHADOW, VEC3, FLOAT);
            def.function("shadow2D", VEC4, SAMPLER2DSHADOW, VEC3);
            def.function("shadow2D", VEC4, SAMPLER2DSHADOW, VEC3, FLOAT);

            def.function("shadow1DProj", VEC4, SAMPLER1DSHADOW, VEC4);
            def.function("shadow1DProj", VEC4, SAMPLER1DSHADOW, VEC4, FLOAT);
            def.function("shadow2DProj", VEC4, SAMPLER2DSHADOW, VEC4);
            def.function("shadow2DProj", VEC4, SAMPLER2DSHADOW, VEC4, FLOAT);

            def.function("shadow1DLod", VEC4, SAMPLER1DSHADOW, VEC3, FLOAT);
            def.function("shadow2DLod", VEC4, SAMPLER2DSHADOW, VEC3, FLOAT);
            def.function("shadow1DProjLod", VEC4, SAMPLER1DSHADOW, VEC4, FLOAT);
            def.function("shadow2DProjLod", VEC4, SAMPLER2DSHADOW, VEC4, FLOAT);
        }
    }

    private ArrayType array(PredefinedType type, int size) {
        return new ArrayType(type, size);
    }
}
