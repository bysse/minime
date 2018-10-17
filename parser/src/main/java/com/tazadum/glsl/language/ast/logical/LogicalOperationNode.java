package com.tazadum.glsl.language.ast.logical;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.traits.HasConstState;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.model.LogicalOperator;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.PredefinedType;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Created by Erik on 2016-10-10.
 */
public class LogicalOperationNode extends FixedChildParentNode implements HasConstState {
    private LogicalOperator operator;

    public LogicalOperationNode(SourcePosition position, LogicalOperator operator) {
        this(position, null, operator);
    }

    public LogicalOperationNode(SourcePosition position, ParentNode parentNode, LogicalOperator operator) {
        super(position, 2, parentNode);
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

    public void setOperator(LogicalOperator operator) {
        this.operator = operator;
    }

    @Override
    public boolean isConstant() {
        return HasConstState.isConst(getLeft()) && HasConstState.isConst(getRight());
    }

    @Override
    public LogicalOperationNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new LogicalOperationNode(getSourcePosition(), newParent, operator));
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitLogicalOperation(this);
    }

    @Override
    public GLSLType getType() {
        return PredefinedType.BOOL;
    }
}
