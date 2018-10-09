package com.tazadum.glsl.language.ast.arithmetic;

import com.tazadum.glsl.language.NumericOperator;
import com.tazadum.glsl.language.ast.*;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePositionId;

/**
 * Created by Erik on 2016-10-10.
 */
public class NumericOperationNode extends FixedChildParentNode implements HasMutableType {
    private NumericOperator operator;
    private GLSLType type;

    public NumericOperationNode(SourcePositionId position, NumericOperator operator) {
        this(position, null, operator);
    }

    public NumericOperationNode(SourcePositionId position, NumericOperator operator, Node left, Node right) {
        this(position, null, operator);
        setLeft(left);
        setRight(right);
    }

    public NumericOperationNode(SourcePositionId position, ParentNode parentNode, NumericOperator operator) {
        super(position, 2, parentNode);
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

    public Node getLeft() {
        return getChild(0);
    }

    public Node getRight() {
        return getChild(1);
    }

    @Override
    public NumericOperationNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new NumericOperationNode(getSourcePositionId(), newParent, operator));
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitNumericOperation(this);
    }

    @Override
    public GLSLType getType() {
        return type;
    }

    @Override
    public void setType(GLSLType type) {
        this.type = type;
    }
}
