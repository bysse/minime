package com.tazadum.glsl.language.type;

import com.tazadum.glsl.language.HasToken;
import com.tazadum.glsl.language.ast.Node;

public class ArrayType implements GLSLType, HasToken {
    public static final int UNKNOWN_LENGTH = -1;
    private final GLSLType type;
    private Node arraySpecifier;
    private final int length;

    public ArrayType(GLSLType type, int length) {
        this.type = type;
        this.length = length;
    }

    public ArrayType(GLSLType type, Node arraySpecifier) {
        this.type = type;
        this.arraySpecifier = arraySpecifier;
        this.length = UNKNOWN_LENGTH;
    }

    @Override
    public GLSLType fieldType(String name) {
        return type;
    }

    public Node getArraySpecifier() {
        return arraySpecifier;
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

    @Override
    public int tokenId() {
        return HasToken.NO_TOKEN_ID;
    }
}
