package com.tazadum.glsl.language.context;


import com.tazadum.glsl.language.ast.Node;

import java.util.Set;

public interface ContextAware {
    GLSLContext enterContext(GLSLContext context);

    GLSLContext exitContext();

    GLSLContext currentContext();

    GLSLContext globalContext();

    boolean removeContext(GLSLContext context);

    void addContext(GLSLContext context);

    Set<GLSLContext> contexts();

    ContextAware remap(Node base);
}
