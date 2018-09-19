package com.tazadum.glsl.preprocessor.language.ast.expression;

import com.tazadum.glsl.preprocessor.language.Expression;
import com.tazadum.glsl.preprocessor.model.RelationalOperator;

/**
 * Node for all types of relational expressions.
 */
public class RelationalExpressionNode implements Expression {
    private Expression left;
    private RelationalOperator operator;
    private Expression right;

    public RelationalExpressionNode(Expression left, RelationalOperator operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Expression getLeft() {
        return left;
    }

    public RelationalOperator getOperator() {
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
