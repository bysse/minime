package com.tazadum.glsl.preprocessor.language.ast.expression;

import com.tazadum.glsl.preprocessor.language.BinaryOperator;
import com.tazadum.glsl.preprocessor.language.Expression;
import com.tazadum.glsl.preprocessor.language.ast.BaseNode;
import com.tazadum.glsl.util.SourcePositionId;

/**
 * Node for all types of binary numeric expressions.
 */
public class BinaryExpressionNode extends BaseNode implements Expression {
    private Expression left;
    private BinaryOperator operator;
    private Expression right;

    public BinaryExpressionNode(SourcePositionId sourcePosition, Expression left, BinaryOperator operator, Expression right) {
        super(sourcePosition);
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Expression getLeft() {
        return left;
    }

    public BinaryOperator getOperator() {
        return operator;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public int accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public String toString() {
        return left + " " + operator.token() + " " + right;
    }
}
