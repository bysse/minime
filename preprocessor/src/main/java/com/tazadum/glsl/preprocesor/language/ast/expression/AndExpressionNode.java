package com.tazadum.glsl.preprocesor.language.ast.expression;

import com.tazadum.glsl.preprocesor.language.Expression;

/**
 * Node for all types of binary numeric expressions.
 */
public class AndExpressionNode implements Expression {
    private Expression left;
    private Expression right;

    public AndExpressionNode(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return left + " && " + right;
    }
}
