package com.tazadum.glsl.preprocesor.language.ast.expression;

import com.sun.javafx.fxml.expression.UnaryExpression;
import com.tazadum.glsl.preprocesor.language.Expression;
import com.tazadum.glsl.preprocesor.model.UnaryOperator;

public class UnaryExpressionNode implements Expression {
    private UnaryOperator operator;
    private Expression expression;

    public UnaryExpressionNode(UnaryOperator operator, Expression expression) {
        this.operator = operator;
        this.expression = expression;
    }

    public UnaryOperator getOperator() {
        return operator;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return operator.token() + expression;
    }
}
