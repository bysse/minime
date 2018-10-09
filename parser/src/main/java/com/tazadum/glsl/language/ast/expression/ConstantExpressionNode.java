package com.tazadum.glsl.language.ast.expression;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.GLSLType;

/**
 * Created by Erik on 2016-10-11.
 */
public class ConstantExpressionNode extends FixedChildParentNode {
    public ConstantExpressionNode(Node expression) {
        this(null, expression);
    }

    public ConstantExpressionNode(ParentNode parentNode, Node expression) {
        super(1, parentNode);
        setChild(0, expression);
    }

    public Node getExpression() {
        return getChild(0);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        final ConstantExpressionNode node = new ConstantExpressionNode(newParent, null);
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
