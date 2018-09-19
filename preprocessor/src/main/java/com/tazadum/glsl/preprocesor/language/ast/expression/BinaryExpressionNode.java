package com.tazadum.glsl.preprocesor.language.ast.expression;

import com.tazadum.glsl.preprocesor.language.Expression;
import com.tazadum.glsl.preprocesor.model.UnaryOperator;

/**
 * Node for all types of binary numeric expressions.
 */
public class BinaryExpressionNode implements Expression {
    private Expression left;
    private UnaryOperator operator;
    private Expression right;

    public BinaryExpressionNode(Expression left, UnaryOperator operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Expression getLeft() {
        return left;
    }

    public UnaryOperator getOperator() {
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
