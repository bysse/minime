package com.tazadum.glsl.ast.logical;

import com.tazadum.glsl.ast.FixedChildParentNode;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.language.LogicalOperator;
import com.tazadum.glsl.util.CloneUtils;

/**
 * Created by Erik on 2016-10-10.
 */
public class LogicalOperationNode extends FixedChildParentNode {
    private LogicalOperator operator;

    public LogicalOperationNode(LogicalOperator operator) {
        this(null, operator);
    }

    public LogicalOperationNode(ParentNode parentNode, LogicalOperator operator) {
        super(2, parentNode);
        this.operator = operator;
    }

    public LogicalOperator getOperator() {
        return operator;
    }

    public void setLeft(Node expression) {
        setChild(0, expression);
    }

    public void setRight(Node expression) {
        setChild(1, expression);
    }

    public Node getLeft() {
        return getChild(0);
    }

    public Node getRight() {
        return getChild(1);
    }

    @Override
    public LogicalOperationNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new LogicalOperationNode(newParent, operator));
    }

}
