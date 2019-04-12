package com.tazadum.glsl.language.ast;

import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.util.SourcePosition;

public class ContextBlockNode extends FixedChildParentNode implements GLSLContext {
    private GLSLContext parentContext;

    public static ContextBlockNode wrap(Node node) {
        ContextBlockNode blockNode = new ContextBlockNode(node.getSourcePosition());
        blockNode.setStatement(node);
        return blockNode;
    }

    public ContextBlockNode(SourcePosition position) {
        super(position, 1);
    }

    public ContextBlockNode(SourcePosition sourcePosition, ParentNode newParent) {
        super(sourcePosition, 1, newParent);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new ContextBlockNode(getSourcePosition(), newParent));
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitBlockNode(this);
    }

    public void setStatement(Node node) {
        setChild(0, node);
    }

    public Node getStatment() {
        return getChild(0);
    }

    @Override
    public GLSLContext getParent() {
        return parentContext;
    }

    @Override
    public void setParent(GLSLContext parentContext) {
        this.parentContext = parentContext;
    }

    @Override
    public boolean isGlobal() {
        return false;
    }

    @Override
    public String toString() {
        return "{ " + getStatment() + " }";
    }
}
