package com.tazadum.glsl.parser;

import com.tazadum.glsl.language.ast.DefaultASTVisitor;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.context.ContextAware;
import com.tazadum.glsl.language.context.GLSLContext;

/**
 * Fixes breaks in the context chain after a clone
 */
public class ContextAwareVisitor extends DefaultASTVisitor<Void> {
    private ContextAware contextAware;

    public ContextAwareVisitor(ContextAware contextAware) {
        this.contextAware = contextAware;
    }

    @Override
    protected <T extends ParentNode> void visitChildren(T node) {
        if (node instanceof GLSLContext) {
            GLSLContext context = (GLSLContext) node;
            contextAware.enterContext(context);
            super.visitChildren(node);
            contextAware.exitContext();
        } else {
            super.visitChildren(node);
        }
    }
}
