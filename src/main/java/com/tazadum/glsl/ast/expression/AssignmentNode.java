package com.tazadum.glsl.ast.expression;

import com.tazadum.glsl.ast.FixedChildParentNode;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.language.AssignmentOperator;

public class AssignmentNode extends FixedChildParentNode {
    private final Node lparam;
    private final AssignmentOperator operator;
    private final Node rparam;

    public AssignmentNode(Node lparam, AssignmentOperator operator, Node rparam) {
        this.lparam = lparam;
        this.operator = operator;
        this.rparam = rparam;
    }

    public AssignmentOperator getOperator() {
        return operator;
    }

    @Override
    protected Node[] getChildNodes() {
        return new Node[] { lparam, rparam };
    }
}
