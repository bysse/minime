package com.tazadum.glsl.ast.arithmetic;

import com.tazadum.glsl.ast.FixedChildParentNode;
import com.tazadum.glsl.ast.MutatingOperation;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.language.UnaryOperator;
import com.tazadum.glsl.util.CloneUtils;

/**
 * Created by Erik on 2016-10-07.
 */
public class PostfixOperationNode extends FixedChildParentNode implements MutatingOperation {
    private UnaryOperator operator;

    public PostfixOperationNode(UnaryOperator operator) {
        this(null, operator);
    }

    public PostfixOperationNode(ParentNode parentNode, UnaryOperator operator) {
        super(1, parentNode);
        this.operator = operator;
    }

    public UnaryOperator getOperator() {
        return operator;
    }

    public void setExpression(Node expression) {
        setChild(0, expression);
    }

    @Override
    public PostfixOperationNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new PostfixOperationNode(newParent, operator));
    }
}
