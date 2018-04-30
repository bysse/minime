package com.tazadum.glsl.parser;

public interface GLSLContext {
    GLSLContext getParent();

    void setParent(GLSLContext parentContext);

    boolean isGlobal();
}
