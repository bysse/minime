package com.tazadum.glsl.preprocesor.language.ast.expression;

import com.tazadum.glsl.preprocesor.language.Expression;

public class ParenthesisNode implements Expression {
    private Expression expression;

    public ParenthesisNode(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return "(" + expression + ")";
    }
}
