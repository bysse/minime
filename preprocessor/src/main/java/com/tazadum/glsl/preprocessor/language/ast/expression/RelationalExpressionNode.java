package com.tazadum.glsl.preprocessor.language.ast.expression;

import com.tazadum.glsl.preprocessor.language.Expression;
import com.tazadum.glsl.preprocessor.language.ast.BaseNode;
import com.tazadum.glsl.preprocessor.language.RelationalOperator;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Node for all types of relational expressions.
 */
public class RelationalExpressionNode extends BaseNode implements Expression {
    private Expression left;
    private RelationalOperator operator;
    private Expression right;

    public RelationalExpressionNode(SourcePosition sourcePosition, Expression left, RelationalOperator operator, Expression right) {
        super(sourcePosition);
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
    public int accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public String toString() {
        return left + " " + operator.token() + " " + right;
    }
}
