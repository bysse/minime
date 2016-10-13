package com.tazadum.glsl.ast.arithmetic;

import com.tazadum.glsl.ast.*;
import com.tazadum.glsl.language.UnaryOperator;
import com.tazadum.glsl.util.CloneUtils;

/**
 * Created by Erik on 2016-10-07.
 */
public class PrefixOperationNode extends FixedChildParentNode implements MutatingOperation {
    private UnaryOperator operator;

    public PrefixOperationNode(UnaryOperator operator) {
        this(null, operator);
    }

    public PrefixOperationNode(ParentNode parentNode, UnaryOperator operator) {
        super(1, parentNode);
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
    public PrefixOperationNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new PrefixOperationNode(newParent, operator));
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitPrefixOperation(this);
    }
}
