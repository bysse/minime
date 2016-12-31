package com.tazadum.glsl.ast.expression;

import com.tazadum.glsl.ast.*;
import com.tazadum.glsl.language.AssignmentOperator;
import com.tazadum.glsl.language.GLSLType;
import com.tazadum.glsl.util.CloneUtils;

public class AssignmentNode extends FixedChildParentNode implements MutatingOperation {
    private final AssignmentOperator operator;

    public AssignmentNode(Node lparam, AssignmentOperator operator, Node rparam) {
        this(null, lparam, operator, rparam);
    }

    public AssignmentNode(ParentNode parent, Node lparam, AssignmentOperator operator, Node rparam) {
        super(2, parent);
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
        final AssignmentNode node = new AssignmentNode(newParent, null, operator, null);
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
