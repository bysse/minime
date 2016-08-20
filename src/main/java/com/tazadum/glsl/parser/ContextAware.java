package com.tazadum.glsl.parser;

public interface ContextAware {
    GLSLContext enterContext();

    GLSLContext exitContext();

    GLSLContext currentContext();

    GLSLContext globalContext();
}
