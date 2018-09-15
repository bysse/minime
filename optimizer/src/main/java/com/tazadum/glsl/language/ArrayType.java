package com.tazadum.glsl.language;

public class ArrayType implements GLSLType, HasToken {
    public static final int UNKNOWN_LENGTH = -1;
    private final GLSLType type;
    private final int length;

    public ArrayType(GLSLType type, int length) {
        this.type = type;
        this.length = length;
    }

    @Override
    public GLSLType fieldType(String name) {
        return type;
    }

    @Override
    public boolean isAssignableBy(GLSLType type) {
        if (!type.isArray()) {
            return false;
        }
        return type.isAssignableBy(type.baseType());
    }

    public boolean isArray() {
        return true;
    }

    public int arrayLength() {
        return length;
    }

    @Override
    public GLSLType baseType() {
        return type;
    }

    @Override
    public String token() {
        return type.token();
    }
}
