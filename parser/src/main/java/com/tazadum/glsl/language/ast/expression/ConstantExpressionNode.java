package com.tazadum.glsl.language.ast.expression;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.traits.HasConstState;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Created by Erik on 2016-10-11.
 */
public class ConstantExpressionNode extends FixedChildParentNode implements HasConstState {
    public ConstantExpressionNode(SourcePosition position, Node expression) {
        this(position, null, expression);
    }

    public ConstantExpressionNode(SourcePosition position, ParentNode parentNode, Node expression) {
        super(position, 1, parentNode);
        setChild(0, expression);
    }

    public Node getExpression() {
        return getChild(0);
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public void setConstant(boolean constant) {
        // no effect
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        final ConstantExpressionNode node = new ConstantExpressionNode(getSourcePosition(), newParent, null);
        node.setChild(0, CloneUtils.clone(getExpression(), node));
        return node;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitConstantExpression(this);
    }

    @Override
    public GLSLType getType() {
        return getExpression().getType();
    }
}
