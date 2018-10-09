package com.tazadum.glsl.language.context;

public interface GLSLContext {
    /**
     * Returns the parent context or null if this is the global context.
     */
    GLSLContext getParent();

    /**
     * Set the parent context.
     *
     * @param parentContext The context that is a parent to the current context.
     */
    void setParent(GLSLContext parentContext);

    /**
     * Returns true if this is the global context.
     */
    boolean isGlobal();
}
