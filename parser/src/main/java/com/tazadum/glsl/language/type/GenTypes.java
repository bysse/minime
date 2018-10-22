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
    GenVectorType(VEC2, VEC3, VEC4, IVEC2, IVEC3, IVEC4, UVEC2, UVEC3, UVEC4, BVEC2, BVEC3, BVEC4, DVEC2, DVEC3, DVEC4),

    GenVec2Type(VEC2, IVEC2, UVEC2, DVEC2),
    GenVec3Type(VEC3, IVEC3, UVEC3, DVEC3),
    GenVec4Type(VEC4, IVEC4, UVEC4, DVEC4),

    GenMat2Type(MAT2, DMAT2, MAT2X2, DMAT2X2),
    GenMat3Type(MAT3, DMAT3, MAT3X3, DMAT3X3),
    GenMat4Type(MAT4, DMAT4, MAT4X4, DMAT4X4),

    GenSampler1D(SAMPLER1D, ISAMPLER1D, USAMPLER1D),
    GenSampler2D(SAMPLER2D, ISAMPLER2D, USAMPLER2D),
    GenSampler3D(SAMPLER3D, ISAMPLER3D, USAMPLER3D),

    GenMatrixType(MAT2, MAT3, MAT4, DMAT2, DMAT3, DMAT4),
    GenSamplerType(SAMPLER2D, SAMPLER3D, SAMPLERCUBE),;

    public PredefinedType[] types;

    GenTypes(PredefinedType... types) {
        this.types = types;
    }

    public PredefinedType fromBase(PredefinedType baseType) {
        for (PredefinedType type : types) {
            if (type.baseType() == baseType) {
                return type;
            }
        }
        return null;
    }
}
