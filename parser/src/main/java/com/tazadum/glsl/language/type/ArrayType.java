package com.tazadum.glsl.language.type;

import com.tazadum.glsl.language.HasToken;

public class ArrayType implements GLSLType, HasToken {
    public static final int UNKNOWN_LENGTH = -1;

    private final GLSLType type;
    private final int dimension;

    public ArrayType(GLSLType type, int dimension) {
        this.type = type;
        this.dimension = dimension;
    }

    public ArrayType(GLSLType type) {
        this.type = type;
        this.dimension = UNKNOWN_LENGTH;
    }

    @Override
    public GLSLType fieldType(String name) {
        return type;
    }

    @Override
    public boolean isAssignableBy(GLSLType type) {
        if (this == type) {
            return true;
        }
        if (!type.isArray() || !type.isAssignableBy(type.baseType())) {
            return false;
        }
        if (hasDimension()) {
            // check that the dimensions are valid
            return ((ArrayType) type).hasDimension() && getDimension() == ((ArrayType) type).getDimension();
        }
        return true;
    }

    public boolean isArray() {
        return true;
    }

    public int getDimension() {
        return dimension;
    }

    public boolean hasDimension() {
        return dimension > UNKNOWN_LENGTH;
    }

    @Override
    public GLSLType baseType() {
        return type;
    }

    @Override
    public String token() {
        return type.token();
    }

    @Override
    public int tokenId() {
        return HasToken.NO_TOKEN_ID;
    }
}
