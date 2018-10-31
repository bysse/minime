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

public class ForIterationNode extends FixedChildParentNode implements IterationNode, GLSLContext {
    private GLSLContext parentContext;

    public ForIterationNode(SourcePosition position) {
        this(position, null);
    }

    public ForIterationNode(SourcePosition position, ParentNode parentNode) {
        super(position, 4, parentNode);
    }

    public void setInitialization(Node initialization) {
        setChild(0, initialization);
    }

    public Node getInitialization() {
        return getChild(0);
    }

    public Node getCondition() {
        return getChild(1);
    }

    public void setCondition(Node condition) {
        setChild(1, condition);
    }

    public Node getExpression() {
        return getChild(2);
    }

    public void setExpression(Node expression) {
        setChild(2, expression);
    }

    public Node getStatement() {
        return getChild(3);
    }

    public void setStatement(Node statements) {
        setChild(3, statements);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new ForIterationNode(getSourcePosition(), newParent));
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitForIteration(this);
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

    public String toString() {
        return "for-iteration(id=" + getId() + ")";
    }
}
