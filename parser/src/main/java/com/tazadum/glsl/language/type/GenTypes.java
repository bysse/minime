package com.tazadum.glsl.language.type;

/**
 * Created by Erik on 2016-10-17.
 */
public enum GenTypes {
    GenType(PredefinedType.FLOAT, PredefinedType.VEC2, PredefinedType.VEC3, PredefinedType.VEC4),
    GenIType(PredefinedType.INT, PredefinedType.IVEC2, PredefinedType.IVEC3, PredefinedType.IVEC4),
    GenBType(PredefinedType.BOOL, PredefinedType.BVEC2, PredefinedType.BVEC3, PredefinedType.BVEC4),

    GenScalarType(PredefinedType.FLOAT, PredefinedType.INT),
    GenVectorType(PredefinedType.VEC2, PredefinedType.VEC3, PredefinedType.VEC4, PredefinedType.IVEC2, PredefinedType.IVEC3, PredefinedType.IVEC4, PredefinedType.BVEC2, PredefinedType.BVEC3, PredefinedType.BVEC4),
    GenMatrixType(PredefinedType.MAT2, PredefinedType.MAT3, PredefinedType.MAT4),
    GenSamplerType(PredefinedType.SAMPLER2D, PredefinedType.SAMPLER3D, PredefinedType.SAMPLERCUBE),;

    public PredefinedType[] concreteTypes;

    GenTypes(PredefinedType... types) {
        concreteTypes = types;
    }
}
