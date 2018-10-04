package com.tazadum.glsl.preprocessor.language.ast.expression;

import com.tazadum.glsl.preprocessor.language.Expression;
import com.tazadum.glsl.preprocessor.language.RelationalOperator;
import com.tazadum.glsl.preprocessor.language.ast.BaseNode;
import com.tazadum.glsl.util.SourcePositionId;

/**
 * Node for all types of relational expressions.
 */
public class RelationalExpressionNode extends BaseNode implements Expression {
    private final Expression left;
    private final RelationalOperator operator;
    private final Expression right;

    public RelationalExpressionNode(SourcePositionId sourcePosition, Expression left, RelationalOperator operator, Expression right) {
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
