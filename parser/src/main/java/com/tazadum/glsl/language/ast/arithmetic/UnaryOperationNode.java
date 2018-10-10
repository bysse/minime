package com.tazadum.glsl.language.ast.arithmetic;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.model.UnaryOperator;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Created by Erik on 2016-10-07.
 */
public class UnaryOperationNode extends FixedChildParentNode {
    private UnaryOperator operator;

    public UnaryOperationNode(SourcePosition position, UnaryOperator operator) {
        this(position, null, operator);
    }

    public UnaryOperationNode(SourcePosition position, ParentNode parentNode, UnaryOperator operator) {
        super(position, 1, parentNode);
        this.operator = operator;
    }

    public UnaryOperator getOperator() {
        return operator;
    }

    public void setExpression(Node expression) {
        setChild(0, expression);
    }

    public Node getExpression() {
        return getChild(0);
    }

    @Override
    public UnaryOperationNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new UnaryOperationNode(getSourcePosition(), newParent, operator));
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitUnaryOperation(this);
    }

    @Override
    public GLSLType getType() {
        return getExpression().getType();
    }
}