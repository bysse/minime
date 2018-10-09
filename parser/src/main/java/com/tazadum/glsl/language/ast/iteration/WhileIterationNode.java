package com.tazadum.glsl.language.ast.iteration;

import com.tazadum.glsl.language.ast.*;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePositionId;

public class WhileIterationNode extends FixedChildParentNode implements IterationNode {
    public WhileIterationNode(SourcePositionId position) {
        this(position, null);
    }

    public WhileIterationNode(SourcePositionId position, ParentNode parentNode) {
        super(position, 2, parentNode);
    }

    public void setCondition(Node condition) {
        setChild(0, condition);
    }

    public Node getCondition() {
        return getChild(0);
    }

    public Node getStatement() {
        return getChild(1);
    }

    public void setStatement(Node statement) {
        setChild(1, statement);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new WhileIterationNode(getSourcePositionId(), newParent));
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitWhileIteration(this);
    }

    @Override
    public GLSLType getType() {
        return null;
    }
}
