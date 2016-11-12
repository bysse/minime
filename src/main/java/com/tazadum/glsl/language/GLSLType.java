package com.tazadum.glsl.language;

public interface GLSLType extends HasToken {
    GLSLType fieldType(String name);

    boolean isAssignableBy(GLSLType type);

    boolean isArray();

    GLSLType baseType();
}
