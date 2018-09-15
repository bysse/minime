package com.tazadum.glsl.language;

import com.tazadum.glsl.exception.TypeException;

/**
 * @author erikb
 * @since 2016-07-31
 */
public enum BuiltInType implements GLSLType, HasToken {
    VOID("void", TypeCategory.Special),
    FLOAT("float", TypeCategory.Scalar),
    INT("int", TypeCategory.Scalar),
    BOOL("bool", TypeCategory.Special),
    VEC2("vec2", TypeCategory.Vector),
    VEC3("vec3", TypeCategory.Vector),
    VEC4("vec4", TypeCategory.Vector),
    BVEC2("bvec2", TypeCategory.Vector),
    BVEC3("bvec3", TypeCategory.Vector),
    BVEC4("bvec4", TypeCategory.Vector),
    IVEC2("ivec2", TypeCategory.Vector),
    IVEC3("ivec3", TypeCategory.Vector),
    IVEC4("ivec4", TypeCategory.Vector),
    MAT2("mat2", TypeCategory.Matrix),
    MAT3("mat3", TypeCategory.Matrix),
    MAT4("mat4", TypeCategory.Matrix),
    SAMPLER2D("sampler2D", TypeCategory.Special),
    SAMPLER3D("sampler3D", TypeCategory.Special),
    SAMPLER_CUBE("samplerCube", TypeCategory.Special);

    private String token;
    private TypeCategory category;

    BuiltInType(String token, TypeCategory category) {
        this.token = token;
        this.category = category;
    }

    @Override
    public String token() {
        return token;
    }

    public TypeCategory category() {
        return category;
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
            case SAMPLER3D:
            case SAMPLER_CUBE:
                throw TypeException.noFields(name());
        }

        // verify all characters in the component specification
        for (int i = 0; i < field.length(); i++) {
            if (!BuiltInField.isVectorComponent(field.charAt(i))) {
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
        if (isArray() != type.isArray()) {
            return false;
        }

        switch (this) {
            case FLOAT:
            case INT:
                return FLOAT == type || INT == type;

        }
        return this == type;
    }

    public boolean isArray() {
        return false;
    }

    public GLSLType baseType() {
        return this;
    }

    private BuiltInType swizzle(String field, BuiltInType... types) {
        final int length = field.length();
        if (length > types.length) {
            throw TypeException.noSuchField(name(), field);
        }
        return types[length - 1];
    }

    public static int elements(BuiltInType type) {
        switch (type) {
            case VEC2:
            case BVEC2:
            case IVEC2:
                return 2;
            case VEC3:
            case BVEC3:
            case IVEC3:
                return 3;
            case VEC4:
            case IVEC4:
            case BVEC4:
                return 4;
            default:
                return 0;
        }
    }
}
