package com.tazadum.glsl.ast.expression;

import com.tazadum.glsl.ast.FixedChildParentNode;
import com.tazadum.glsl.ast.MutatingOperation;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.language.AssignmentOperator;
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

    public AssignmentOperator getOperator() {
        return operator;
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        final Node lparam = CloneUtils.clone(getChild(0));
        final Node rparam = CloneUtils.clone(getChild(1));
        return new AssignmentNode(newParent, lparam, operator, rparam);
    }
}
