package com.tazadum.glsl.language.ast.arithmetic;

import com.tazadum.glsl.language.UnaryOperator;
import com.tazadum.glsl.language.ast.*;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePositionId;

/**
 * Created by Erik on 2016-10-07.
 */
public class PrefixOperationNode extends FixedChildParentNode implements MutatingOperation {
    private UnaryOperator operator;

    public PrefixOperationNode(SourcePositionId position, UnaryOperator operator) {
        this(position, null, operator);
    }

    public PrefixOperationNode(SourcePositionId position, ParentNode parentNode, UnaryOperator operator) {
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
    public PrefixOperationNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new PrefixOperationNode(getSourcePositionId(), newParent, operator));
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitPrefixOperation(this);
    }

    @Override
    public GLSLType getType() {
        return getExpression().getType();
    }
}
