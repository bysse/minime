package com.tazadum.glsl.parser;

import com.tazadum.glsl.ast.Node;

import java.util.Set;

public interface ContextAware {
    GLSLContext enterContext(GLSLContext context);

    GLSLContext exitContext();

    GLSLContext currentContext();

    GLSLContext globalContext();

    Set<GLSLContext> contexts();

    ContextAware remap(Node base);
}
