package com.tazadum.glsl.ast.arithmetic;

import com.tazadum.glsl.ast.FixedChildParentNode;
import com.tazadum.glsl.ast.MutatingOperation;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.language.NumericOperator;
import com.tazadum.glsl.util.CloneUtils;

/**
 * Created by Erik on 2016-10-10.
 */
public class NumericOperationNode extends FixedChildParentNode implements MutatingOperation {
    private NumericOperator operator;

    public NumericOperationNode(NumericOperator operator) {
        this(null, operator);
    }

    public NumericOperationNode(ParentNode parentNode, NumericOperator operator) {
        super(2, parentNode);
        this.operator = operator;
    }

    public NumericOperator getOperator() {
        return operator;
    }

    public void setLeft(Node expression) {
        setChild(0, expression);
    }

    public void setRight(Node expression) {
        setChild(1, expression);
    }

    @Override
    public NumericOperationNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new NumericOperationNode(newParent, operator));
    }
}
