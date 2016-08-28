package com.tazadum.glsl.language;

import com.tazadum.glsl.exception.TypeException;

/**
 * @author erikb
 * @since 2016-07-31
 */
public enum BuiltInType implements GLSLType, HasToken {
    VOID("void"),
    FLOAT("float"),
    INT("int"),
    BOOL("bool"),
    VEC2("vec2"),
    VEC3("vec3"),
    VEC4("vec4"),
    BVEC2("bvec2"),
    BVEC3("bvec3"),
    BVEC4("bvec4"),
    IVEC2("ivec2"),
    IVEC3("ivec3"),
    IVEC4("ivec4"),
    MAT2("mat2"),
    MAT3("mat3"),
    MAT4("mat4"),
    SAMPLER2D("sampler2D"),
    SAMPLER_CUBE("samplerCube");

    private String token;

    BuiltInType(String token) {
        this.token = token;
    }

    @Override
    public String token() {
        return token;
    }

    @Override
    public GLSLType fieldType(String field) {
        switch (this) {
            case VOID:
            case FLOAT:
            case INT:
            case BOOL:
            case MAT2:
            case MAT3:
            case MAT4:
            case SAMPLER2D:
            case SAMPLER_CUBE:
                throw TypeException.noFields(name());
        }

        // verify all characters in the component specification
        for (int i=0;i<field.length();i++) {
            if (!isComponent(field.charAt(i))) {
                throw TypeException.noSuchField(name(), field);
            }
        }

        switch (this) {
            case VEC2:
            case VEC3:
            case VEC4:
                return swizzle(field, FLOAT, VEC2, VEC3, VEC4);
            case IVEC2:
            case IVEC3:
            case IVEC4:
                return swizzle(field, INT, IVEC2, IVEC3, IVEC4);
            case BVEC2:
            case BVEC3:
            case BVEC4:
                return swizzle(field, BOOL, BVEC2, BVEC3, BVEC4);
        }

        throw TypeException.unknownError("Unhandled type " + name());
    }

    public boolean isAssignableBy(GLSLType type) {
        switch (this) {
            case FLOAT:
            case INT:
                return FLOAT == type || INT == type;

        }
        return this == type;
    }

    private BuiltInType swizzle(String field, BuiltInType... types) {
        final int length = field.length();
        if (length > types.length) {
            throw TypeException.noSuchField(name(), field);
        }
        return types[length - 1];
    }

    private boolean isComponent(char ch) {
        switch (ch) {
            case 'r': // rgba
            case 'g':
            case 'b':
            case 'a':
            case 's': // stpq
            case 't':
            case 'p':
            case 'q':
            case 'x': // xyzw
            case 'y':
            case 'z':
            case 'w':
                return true;
            default:
                return false;
        }
    }
}
