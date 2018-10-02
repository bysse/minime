package com.tazadum.glsl.preprocessor.language.ast.expression;

import com.tazadum.glsl.preprocessor.language.Expression;
import com.tazadum.glsl.preprocessor.language.UnaryOperator;
import com.tazadum.glsl.preprocessor.language.ast.BaseNode;
import com.tazadum.glsl.util.SourcePositionId;

public class UnaryExpressionNode extends BaseNode implements Expression {
    private UnaryOperator operator;
    private Expression expression;

    public UnaryExpressionNode(SourcePositionId sourcePosition, UnaryOperator operator, Expression expression) {
        super(sourcePosition);
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
    public int accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public String toString() {
        return operator.token() + expression;
    }
}
