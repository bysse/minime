package com.tazadum.glsl.language;

/**
 * Created by Erik on 2016-10-17.
 */
public enum GenTypes {
    GenType(BuiltInType.FLOAT, BuiltInType.VEC2, BuiltInType.VEC3, BuiltInType.VEC4),
    GenIType(BuiltInType.INT, BuiltInType.IVEC2, BuiltInType.IVEC3, BuiltInType.IVEC4),
    genBType(BuiltInType.BOOL, BuiltInType.BVEC2, BuiltInType.BVEC3, BuiltInType.BVEC4),
    ;

    public BuiltInType[] concreteTypes;

    GenTypes(BuiltInType... types) {
        concreteTypes = types;
    }
}
