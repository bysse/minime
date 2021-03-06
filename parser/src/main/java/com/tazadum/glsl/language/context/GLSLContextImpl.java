package com.tazadum.glsl.language.context;

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

    @Override
    public boolean isGlobal() {
        return parent == null;
    }

    @Override
    public String toString() {
        if (parent == null) {
            return "GlobalContext";
        }
        return parent + " <- context";
    }
}
