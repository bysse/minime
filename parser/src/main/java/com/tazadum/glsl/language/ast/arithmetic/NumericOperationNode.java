package com.tazadum.glsl.language.ast.arithmetic;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.traits.HasConstState;
import com.tazadum.glsl.language.ast.traits.HasMutableType;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.model.NumericOperator;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Created by Erik on 2016-10-10.
 */
public class NumericOperationNode extends FixedChildParentNode implements HasMutableType, HasConstState {
    private NumericOperator operator;
    private GLSLType type;

    public NumericOperationNode(SourcePosition position, NumericOperator operator) {
        this(position, null, operator);
    }

    public NumericOperationNode(SourcePosition position, NumericOperator operator, Node left, Node right) {
        this(position, null, operator);
        setLeft(left);
        setRight(right);
    }

    public NumericOperationNode(SourcePosition position, ParentNode parentNode, NumericOperator operator) {
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
    public boolean isConstant() {
        return HasConstState.isConst(getLeft()) && HasConstState.isConst(getRight());
    }

    @Override
    public NumericOperationNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new NumericOperationNode(getSourcePosition(), newParent, operator));
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
