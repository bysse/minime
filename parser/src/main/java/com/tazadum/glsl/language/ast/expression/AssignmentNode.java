package com.tazadum.glsl.language.ast.expression;

import com.tazadum.glsl.language.ast.*;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.model.AssignmentOperator;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

public class AssignmentNode extends FixedChildParentNode implements MutatingOperation {
    private final AssignmentOperator operator;

    public AssignmentNode(SourcePosition position, Node lparam, AssignmentOperator operator, Node rparam) {
        this(position, null, lparam, operator, rparam);
    }

    public AssignmentNode(SourcePosition position, ParentNode parent, Node lparam, AssignmentOperator operator, Node rparam) {
        super(position, 2, parent);
        this.operator = operator;
        setChild(0, lparam);
        setChild(1, rparam);
    }

    public Node getLeft() {
        return getChild(0);
    }

    public Node getRight() {
        return getChild(1);
    }

    public AssignmentOperator getOperator() {
        return operator;
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        final AssignmentNode node = new AssignmentNode(getSourcePosition(), newParent, null, operator, null);
        return CloneUtils.cloneChildren(this, node);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitAssignment(this);
    }

    @Override
    public GLSLType getType() {
        return getLeft().getType();
    }
}
