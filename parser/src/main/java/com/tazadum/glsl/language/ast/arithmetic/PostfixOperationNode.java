package com.tazadum.glsl.language.ast.arithmetic;

import com.tazadum.glsl.language.UnaryOperator;
import com.tazadum.glsl.language.ast.*;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.GLSLType;

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

    public Node getExpression() {
        return getChild(0);
    }

    @Override
    public PostfixOperationNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new PostfixOperationNode(newParent, operator));
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitPostfixOperation(this);
    }

    @Override
    public GLSLType getType() {
        return getExpression().getType();
    }
}
