package com.tazadum.glsl.language.ast.arithmetic;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.traits.HasConstState;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.model.BitOperator;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Created by Erik on 2018-10-12.
 */
public class BitOperationNode extends FixedChildParentNode implements HasConstState {
    private BitOperator operator;
    private GLSLType type;

    public BitOperationNode(SourcePosition position, BitOperator operator) {
        this(position, null, operator);
    }

    public BitOperationNode(SourcePosition position, BitOperator operator, Node left, Node right) {
        this(position, null, operator);
        setLeft(left);
        setRight(right);
    }

    public BitOperationNode(SourcePosition position, ParentNode parentNode, BitOperator operator) {
        super(position, 2, parentNode);
        this.operator = operator;
    }

    public BitOperator getOperator() {
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
    public BitOperationNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new BitOperationNode(getSourcePosition(), newParent, operator));
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitBitOperation(this);
    }

    @Override
    public GLSLType getType() {
        return type;
    }
}
