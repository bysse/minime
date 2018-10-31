package com.tazadum.glsl.parser;

import com.tazadum.glsl.language.ast.DefaultASTVisitor;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.context.GLSLContext;

/**
 * Fixes breaks in the context chain after a clone
 */
public class ContextVisitor extends DefaultASTVisitor<Void> {

    @Override
    protected <T extends ParentNode> void visitChildren(T node) {
        if (node instanceof GLSLContext) {
            GLSLContext context = (GLSLContext) node;
            if (context.getParent() == null) {
                if (node.getParentNode() != null) {
                    context.setParent(findContext(node.getParentNode()));
                }
            }
        }

        super.visitChildren(node);
    }

    private GLSLContext findContext(Node node) {
        if (node instanceof GLSLContext) {
            return (GLSLContext) node;
        }
        final ParentNode parentNode = node.getParentNode();
        if (parentNode != null) {
            return findContext(parentNode);
        }
        return null;
    }
}
