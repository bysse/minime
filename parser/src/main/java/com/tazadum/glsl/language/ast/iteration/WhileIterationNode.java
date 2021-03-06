package com.tazadum.glsl.language.ast.iteration;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.traits.IterationNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

public class WhileIterationNode extends FixedChildParentNode implements IterationNode, GLSLContext {
    private GLSLContext parentContext;

    public WhileIterationNode(SourcePosition position) {
        this(position, null);
    }

    public WhileIterationNode(SourcePosition position, ParentNode parentNode) {
        super(position, 2, parentNode);
    }

    public void setCondition(Node condition) {
        setChild(0, condition);
    }

    public Node getCondition() {
        return getChild(0);
    }

    @Override
    public Node getStatement() {
        return getChild(1);
    }

    @Override
    public void setStatement(Node statement) {
        setChild(1, statement);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new WhileIterationNode(getSourcePosition(), newParent));
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitWhileIteration(this);
    }

    @Override
    public GLSLType getType() {
        return null;
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
}
