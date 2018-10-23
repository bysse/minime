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
        BuiltInFunctionRegistry.FunctionDeclarator declarator = registry.getFunctionDeclarator();

        // Texture Query Functions
        declarator.function("textureSize", INT, GenSampler1D, INT);
        declarator.function("textureSize", IVEC2, GenSampler2D, INT);
        declarator.function("textureSize", IVEC3, GenSampler3D, INT);
        declarator.function("textureSize", IVEC2, GenSamplerCube, INT);
        declarator.function("textureSize", IVEC2, GenSampler2DMS);
        declarator.function("textureSize", INT, GenSampleBuffer);
        declarator.function("textureSize", IVEC2, GenSampler2DRect);
        declarator.function("textureSize", IVEC2, SAMPLER2DRECTSHADOW);
        declarator.function("textureSize", IVEC2, SAMPLER1DSHADOW, INT);
        declarator.function("textureSize", IVEC2, SAMPLER2DSHADOW, INT);
        declarator.function("textureSize", IVEC2, SAMPLERCUBESHADOW, INT);
        declarator.function("textureSize", IVEC2, GenSampler1DArray, INT);
        declarator.function("textureSize", IVEC3, GenSampler2DArray, INT);
        declarator.function("textureSize", IVEC3, GenSamplerCubeArray, INT);
        declarator.function("textureSize", IVEC3, GenSampler2DMSArray);
        declarator.function("textureSize", IVEC3, SAMPLERCUBEARRAYSHADOW, INT);
        declarator.function("textureSize", IVEC2, SAMPLER1DARRAYSHADOW, INT);
        declarator.function("textureSize", IVEC3, SAMPLER2DARRAYSHADOW, INT);

        declarator.function("textureQueryLod", VEC2, GenSampler1D, FLOAT);
        declarator.function("textureQueryLod", VEC2, GenSampler2D, VEC2);
        declarator.function("textureQueryLod", VEC2, GenSampler3D, VEC3);
        declarator.function("textureQueryLod", VEC2, GenSamplerCube, VEC3);
        declarator.function("textureQueryLod", VEC2, SAMPLER1DSHADOW, FLOAT);
        declarator.function("textureQueryLod", VEC2, SAMPLER2DSHADOW, VEC2);
        declarator.function("textureQueryLod", VEC2, SAMPLERCUBESHADOW, VEC3);
        declarator.function("textureQueryLod", VEC2, GenSampler1DArray, FLOAT);
        declarator.function("textureQueryLod", VEC2, GenSampler2DArray, VEC2);
        declarator.function("textureQueryLod", VEC2, GenSamplerCubeArray, VEC3);
        declarator.function("textureQueryLod", VEC2, SAMPLER1DARRAYSHADOW, FLOAT);
        declarator.function("textureQueryLod", VEC2, SAMPLER2DARRAYSHADOW, VEC2);
        declarator.function("textureQueryLod", VEC2, SAMPLERCUBEARRAYSHADOW, VEC3);

        declarator.function("textureQueryLevels", INT, GenSampler1D);
        declarator.function("textureQueryLevels", INT, GenSampler2D);
        declarator.function("textureQueryLevels", INT, GenSampler3D);
        declarator.function("textureQueryLevels", INT, GenSamplerCube);
        declarator.function("textureQueryLevels", INT, GenSampler1DArray);
        declarator.function("textureQueryLevels", INT, GenSampler2DArray);
        declarator.function("textureQueryLevels", INT, GenSamplerCubeArray);
        declarator.function("textureQueryLevels", INT, SAMPLER1DARRAYSHADOW);
        declarator.function("textureQueryLevels", INT, SAMPLER2DARRAYSHADOW);
        declarator.function("textureQueryLevels", INT, SAMPLERCUBEARRAYSHADOW);

        declarator.function("textureSamples", INT, GenSampler2DMS);
        declarator.function("textureSamples", INT, GenSampler2DMSArray);

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

            declarator.function("texture", gvec4, gsampler1D, FLOAT);
            declarator.function("texture", gvec4, gsampler1D, FLOAT, FLOAT);
            declarator.function("texture", gvec4, gsampler2D, VEC2);
            declarator.function("texture", gvec4, gsampler2D, VEC2, FLOAT);
            declarator.function("texture", gvec4, gsampler3D, VEC3);
            declarator.function("texture", gvec4, gsampler3D, VEC3, FLOAT);
            declarator.function("texture", gvec4, gsamplerCube, VEC3);
            declarator.function("texture", gvec4, gsamplerCube, VEC3, FLOAT);
            declarator.function("texture", gvec4, gsampler2DRect, VEC2);
            declarator.function("texture", gvec4, gsampler1DArray, VEC2);
            declarator.function("texture", gvec4, gsampler1DArray, VEC2, FLOAT);
            declarator.function("texture", gvec4, gsampler2DArray, VEC3);
            declarator.function("texture", gvec4, gsampler2DArray, VEC3, FLOAT);
            declarator.function("texture", gvec4, gsamplerCubeArray, VEC4);
            declarator.function("texture", gvec4, gsamplerCubeArray, VEC4, FLOAT);

            declarator.function("textureProj", gvec4, gsampler1D, VEC2);
            declarator.function("textureProj", gvec4, gsampler1D, VEC2, FLOAT);
            declarator.function("textureProj", gvec4, gsampler1D, VEC4);
            declarator.function("textureProj", gvec4, gsampler1D, VEC4, FLOAT);
            declarator.function("textureProj", gvec4, gsampler2D, VEC3);
            declarator.function("textureProj", gvec4, gsampler2D, VEC3, FLOAT);
            declarator.function("textureProj", gvec4, gsampler2D, VEC4);
            declarator.function("textureProj", gvec4, gsampler2D, VEC4, FLOAT);
            declarator.function("textureProj", gvec4, gsampler3D, VEC4);
            declarator.function("textureProj", gvec4, gsampler3D, VEC4, FLOAT);
            declarator.function("textureProj", gvec4, gsampler2DRect, VEC3);
            declarator.function("textureProj", gvec4, gsampler2DRect, VEC4);

            declarator.function("textureLod", gvec4, gsampler1D, FLOAT);
            declarator.function("textureLod", gvec4, gsampler2D, VEC2);
            declarator.function("textureLod", gvec4, gsampler3D, VEC3);
            declarator.function("textureLod", gvec4, gsamplerCube, VEC3);
            declarator.function("textureLod", gvec4, gsampler1DArray, VEC2, FLOAT);
            declarator.function("textureLod", gvec4, gsampler2DArray, VEC3, FLOAT);
            declarator.function("textureLod", gvec4, gsampler2DArray, VEC4, FLOAT);

            declarator.function("textureOffset", gvec4, gsampler1D, FLOAT, INT);
            declarator.function("textureOffset", gvec4, gsampler1D, FLOAT, INT, FLOAT);
            declarator.function("textureOffset", gvec4, gsampler2D, VEC2, IVEC2);
            declarator.function("textureOffset", gvec4, gsampler2D, VEC2, IVEC2, FLOAT);
            declarator.function("textureOffset", gvec4, gsampler3D, VEC3, IVEC3);
            declarator.function("textureOffset", gvec4, gsampler3D, VEC3, IVEC3, FLOAT);
            declarator.function("textureOffset", gvec4, gsampler2DRect, VEC2, IVEC2);

            declarator.function("textureOffset", gvec4, gsampler1DArray, VEC2, INT);
            declarator.function("textureOffset", gvec4, gsampler1DArray, VEC2, INT, FLOAT);
            declarator.function("textureOffset", gvec4, gsampler2DArray, VEC3, IVEC2);
            declarator.function("textureOffset", gvec4, gsampler2DArray, VEC3, IVEC2, FLOAT);

            declarator.function("texelFetch", gvec4, gsampler1D, INT, INT);
            declarator.function("texelFetch", gvec4, gsampler2D, IVEC2, INT);
            declarator.function("texelFetch", gvec4, gsampler3D, IVEC3, INT);
            declarator.function("texelFetch", gvec4, gsampler1DArray, IVEC2, INT);
            declarator.function("texelFetch", gvec4, gsampler2DArray, IVEC3, INT);
            declarator.function("texelFetch", gvec4, gsamplerBuffer, INT, INT);
            declarator.function("texelFetch", gvec4, gsampler2DMS, IVEC2, INT);
            declarator.function("texelFetch", gvec4, gsampler2DMSArray, IVEC3, INT);

            declarator.function("texelFetchOffset", gvec4, gsampler1D, INT, INT, INT);
            declarator.function("texelFetchOffset", gvec4, gsampler2D, IVEC2, INT, IVEC2);
            declarator.function("texelFetchOffset", gvec4, gsampler3D, IVEC3, INT, IVEC3);
            declarator.function("texelFetchOffset", gvec4, gsampler2DRect, IVEC2, IVEC2);
            declarator.function("texelFetchOffset", gvec4, gsampler1DArray, IVEC2, INT, INT);
            declarator.function("texelFetchOffset", gvec4, gsampler2DArray, IVEC3, INT, IVEC2);

            declarator.function("textureProjOffset", gvec4, gsampler1D, VEC2, INT);
            declarator.function("textureProjOffset", gvec4, gsampler1D, VEC2, INT, FLOAT);
            declarator.function("textureProjOffset", gvec4, gsampler1D, VEC4, INT);
            declarator.function("textureProjOffset", gvec4, gsampler1D, VEC4, INT, FLOAT);
            declarator.function("textureProjOffset", gvec4, gsampler2D, VEC3, IVEC2);
            declarator.function("textureProjOffset", gvec4, gsampler2D, VEC3, IVEC2, FLOAT);
            declarator.function("textureProjOffset", gvec4, gsampler2D, VEC4, IVEC2);
            declarator.function("textureProjOffset", gvec4, gsampler2D, VEC4, IVEC2, FLOAT);
            declarator.function("textureProjOffset", gvec4, gsampler3D, VEC4, IVEC3);
            declarator.function("textureProjOffset", gvec4, gsampler3D, VEC4, IVEC3, FLOAT);
            declarator.function("textureProjOffset", gvec4, gsampler2DRect, VEC3, IVEC2);
            declarator.function("textureProjOffset", gvec4, gsampler2DRect, VEC4, IVEC2);
        }

        declarator.function("texture", FLOAT, SAMPLER1DSHADOW, VEC3);
        declarator.function("texture", FLOAT, SAMPLER1DSHADOW, VEC3, FLOAT);
        declarator.function("texture", FLOAT, SAMPLER2DSHADOW, VEC3);
        declarator.function("texture", FLOAT, SAMPLER2DSHADOW, VEC3, FLOAT);
        declarator.function("texture", FLOAT, SAMPLERCUBESHADOW, VEC4);
        declarator.function("texture", FLOAT, SAMPLERCUBESHADOW, VEC4, FLOAT);
        declarator.function("texture", FLOAT, SAMPLER1DARRAYSHADOW, VEC2);
        declarator.function("texture", FLOAT, SAMPLER1DARRAYSHADOW, VEC2, FLOAT);
        declarator.function("texture", FLOAT, SAMPLER2DARRAYSHADOW, VEC4);
        declarator.function("texture", FLOAT, SAMPLERCUBEARRAYSHADOW, VEC4, FLOAT);
        declarator.function("texture", FLOAT, SAMPLER2DRECTSHADOW, VEC3);

        declarator.function("textureProj", FLOAT, SAMPLER1DSHADOW, VEC4);
        declarator.function("textureProj", FLOAT, SAMPLER1DSHADOW, VEC4, FLOAT);
        declarator.function("textureProj", FLOAT, SAMPLER2DSHADOW, VEC4);
        declarator.function("textureProj", FLOAT, SAMPLER2DSHADOW, VEC4, FLOAT);
        declarator.function("textureProj", FLOAT, SAMPLER2DRECTSHADOW, VEC4);

        declarator.function("textureLod", FLOAT, SAMPLER1DSHADOW, VEC3, FLOAT);
        declarator.function("textureLod", FLOAT, SAMPLER2DSHADOW, VEC3, FLOAT);

        declarator.function("textureOffset", FLOAT, SAMPLER1DSHADOW, VEC3, INT);
        declarator.function("textureOffset", FLOAT, SAMPLER1DSHADOW, VEC3, INT, FLOAT);
        declarator.function("textureOffset", FLOAT, SAMPLER2DSHADOW, VEC3, IVEC2);
        declarator.function("textureOffset", FLOAT, SAMPLER2DSHADOW, VEC3, IVEC2, FLOAT);
        declarator.function("textureOffset", FLOAT, SAMPLER1DARRAYSHADOW, VEC3, INT);
        declarator.function("textureOffset", FLOAT, SAMPLER1DARRAYSHADOW, VEC3, INT, FLOAT);
        declarator.function("textureOffset", FLOAT, SAMPLER2DARRAYSHADOW, VEC4, IVEC2);
        declarator.function("textureOffset", FLOAT, SAMPLER2DSHADOW, VEC3, IVEC2);

        declarator.function("textureProjOffset", FLOAT, SAMPLER2DRECTSHADOW, VEC4, IVEC2);
        declarator.function("textureProjOffset", FLOAT, SAMPLER1DSHADOW, VEC4, INT);
        declarator.function("textureProjOffset", FLOAT, SAMPLER1DSHADOW, VEC4, INT, FLOAT);
        declarator.function("textureProjOffset", FLOAT, SAMPLER2DSHADOW, VEC4, IVEC2);
        declarator.function("textureProjOffset", FLOAT, SAMPLER2DSHADOW, VEC4, IVEC2, FLOAT);

        // Derivative Functions
        declarator.function("dFdx", GenFType, GenFType);
        declarator.function("dFdy", GenFType, GenFType);
        declarator.function("fwidth", GenFType, GenFType);
    }
}
