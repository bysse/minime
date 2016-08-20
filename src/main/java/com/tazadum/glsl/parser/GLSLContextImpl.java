package com.tazadum.glsl.parser;

public class GLSLContextImpl implements GLSLContext {
    private GLSLContext parent;

    public GLSLContextImpl() {
        this(null);
    }

    public GLSLContextImpl(GLSLContext parent) {
        this.parent = parent;
    }

    @Override
    public GLSLContext getParent() {
        return parent;
    }

    @Override
    public void setParent(GLSLContext context) {
        this.parent = context;
    }
}
