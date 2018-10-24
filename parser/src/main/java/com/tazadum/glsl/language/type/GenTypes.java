package com.tazadum.glsl.language.type;

import static com.tazadum.glsl.language.type.PredefinedType.*;

/**
 * Created by Erik on 2016-10-17.
 */
public enum GenTypes {
    GenFType(FLOAT, VEC2, VEC3, VEC4),
    GenIType(INT, IVEC2, IVEC3, VEC4),
    GenUType(UINT, UVEC2, UVEC3, UVEC4),
    GenBType(BOOL, BVEC2, BVEC3, BVEC4),
    GenDType(DOUBLE, DVEC2, DVEC3, DVEC4),

    GenSingleType(INT, UINT, FLOAT, DOUBLE, BOOL),
    GenScalarType(INT, UINT, FLOAT, DOUBLE),

    GenBVectorType(BVEC2, BVEC3, BVEC4),
    GenIVectorType(IVEC2, IVEC3, IVEC4),
    GenUVectorType(UVEC2, UVEC3, UVEC4),
    GenFVectorType(VEC2, VEC3, VEC4),

    GenVectorType(VEC2, VEC3, VEC4, IVEC2, IVEC3, IVEC4, UVEC2, UVEC3, UVEC4, BVEC2, BVEC3, BVEC4, DVEC2, DVEC3, DVEC4),

    GenVec2Type(VEC2, IVEC2, UVEC2, DVEC2),
    GenVec3Type(VEC3, IVEC3, UVEC3, DVEC3),
    GenVec4Type(VEC4, IVEC4, UVEC4, DVEC4),

    GenMat2Type(MAT2, DMAT2, MAT2X2, DMAT2X2),
    GenMat3Type(MAT3, DMAT3, MAT3X3, DMAT3X3),
    GenMat4Type(MAT4, DMAT4, MAT4X4, DMAT4X4),

    GenFMatrixType(MAT2, MAT3, MAT4),
    GenMatrixType(MAT2, MAT3, MAT4, DMAT2, DMAT3, DMAT4),

    GenSamplerType(SAMPLER2D, SAMPLER3D, SAMPLERCUBE),
    GenSampler1D(SAMPLER1D, ISAMPLER1D, USAMPLER1D),
    GenSampler2D(SAMPLER2D, ISAMPLER2D, USAMPLER2D),
    GenSampler3D(SAMPLER3D, ISAMPLER3D, USAMPLER3D),
    GenSamplerCube(SAMPLERCUBE, ISAMPLERCUBE, USAMPLERCUBE),
    GenSampler2DRect(SAMPLER2DRECT, ISAMPLER2DRECT, USAMPLER2DRECT),

    GenSampler1DArray(SAMPLER1DARRAY, ISAMPLER1DARRAY, USAMPLER1DARRAY),
    GenSampler2DArray(SAMPLER2DARRAY, ISAMPLER2DARRAY, USAMPLER2DARRAY),
    GenSamplerCubeArray(SAMPLERCUBEARRAY, ISAMPLERCUBEARRAY, USAMPLERCUBEARRAY),
    GenSampleBuffer(SAMPLERBUFFER, ISAMPLERBUFFER, USAMPLERBUFFER),

    GenSampler2DMS(SAMPLER2DMS, ISAMPLER2DMS, USAMPLER2DMS),
    GenSampler2DMSArray(SAMPLER2DMSARRAY, ISAMPLER2DMSARRAY, USAMPLER2DMSARRAY),

    GenImage1D(IMAGE1D, IIMAGE1D, UIMAGE1D),
    GenImage2D(IMAGE2D, IIMAGE2D, UIMAGE2D),
    GenImage3D(IMAGE3D, IIMAGE3D, UIMAGE3D),
    GenImageCube(IMAGECUBE, IIMAGECUBE, UIMAGECUBE),
    GenImageRect(IMAGE2DRECT, IIMAGE2DRECT, UIMAGE2DRECT),
    GenImageBuffer(IMAGEBUFFER, IIMAGEBUFFER, UIMAGEBUFFER),
    GenImage1DArray(IMAGE1DARRAY, IIMAGE1DARRAY, UIMAGE1DARRAY),
    GenImage2DArray(IMAGE2DARRAY, IIMAGE2DARRAY, UIMAGE2DARRAY),
    GenImageCubeArray(IMAGECUBEARRAY, IIMAGECUBEARRAY, UIMAGECUBEARRAY),
    GenImage2DMS(IMAGE2DMS, IIMAGE2DMS, UIMAGE2DMS),
    GenImage2DMSArray(IMAGE2DMSARRAY, IIMAGE2DMSARRAY, UIMAGE2DMSARRAY),;

    public PredefinedType[] types;

    GenTypes(PredefinedType... types) {
        this.types = types;
    }

    public static void iterateFUI(TypeIterationCallback callback) {
        for (int i = 0; i < 3; i++) {
            PredefinedType gscalar = GenFType.types[i];
            PredefinedType gvec2 = GenVec2Type.types[i];
            PredefinedType gvec3 = GenVec3Type.types[i];
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

            callback.iterate(
                gscalar, gvec2, gvec3, gvec4,
                gsampler1D, gsampler2D, gsampler3D, gsamplerCube,
                gsampler2DRect, gsampler2DMS, gsamplerBuffer,
                gsampler1DArray, gsampler2DArray, gsamplerCubeArray, gsampler2DMSArray
            );
        }
    }

    public PredefinedType fromBase(PredefinedType baseType) {
        for (PredefinedType type : types) {
            if (type.baseType() == baseType) {
                return type;
            }
        }
        return null;
    }

    public interface TypeIterationCallback {
        void iterate(
            PredefinedType gscalar, PredefinedType gvec2, PredefinedType gvec3, PredefinedType gvec4,
            PredefinedType gsampler1d, PredefinedType gsampler2d, PredefinedType gsampler3d, PredefinedType gsamplerCube,
            PredefinedType gsampler2dRect, PredefinedType gsampler2dMS, PredefinedType gsamplerBuffer,
            PredefinedType gsampler1dArray, PredefinedType gsampler2dArray, PredefinedType gsamplerCubeArray, PredefinedType gsampler2dMSArray
        );
    }
}
