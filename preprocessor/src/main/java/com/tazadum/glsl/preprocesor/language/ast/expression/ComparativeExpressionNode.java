package com.tazadum.glsl.preprocesor.language.ast.expression;

import com.tazadum.glsl.preprocesor.language.Expression;
import com.tazadum.glsl.preprocesor.model.ComparisonOperator;

/**
 * Node for all types of binary numeric expressions.
 */
public class ComparativeExpressionNode implements Expression {
    private Expression left;
    private ComparisonOperator operator;
    private Expression right;

    public ComparativeExpressionNode(Expression left, ComparisonOperator operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Expression getLeft() {
        return left;
    }

    public ComparisonOperator getOperator() {
        return operator;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return left + " " + operator.token() + " " + right;
    }
}
