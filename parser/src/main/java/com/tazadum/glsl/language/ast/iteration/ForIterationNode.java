package com.tazadum.glsl.language.ast.iteration;

import com.tazadum.glsl.language.ast.*;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.language.type.GLSLType;

public class ForIterationNode extends FixedChildParentNode implements IterationNode, GLSLContext {
    private GLSLContext parentContext;

    public ForIterationNode() {
        this(null);
    }

    public ForIterationNode(ParentNode parentNode) {
        super(4, parentNode);
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
        return CloneUtils.cloneChildren(this, new ForIterationNode(newParent));
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
}
