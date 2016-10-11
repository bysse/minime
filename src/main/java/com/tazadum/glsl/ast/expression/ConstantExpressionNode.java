package com.tazadum.glsl.ast.expression;

import com.tazadum.glsl.ast.FixedChildParentNode;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.util.CloneUtils;

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
        return new ConstantExpressionNode(newParent, CloneUtils.clone(getExpression()));
    }
}
