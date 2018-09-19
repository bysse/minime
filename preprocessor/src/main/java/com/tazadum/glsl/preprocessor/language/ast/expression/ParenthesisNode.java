package com.tazadum.glsl.preprocessor.language.ast.expression;

import com.tazadum.glsl.preprocessor.language.Expression;
import com.tazadum.glsl.preprocessor.language.ast.BaseNode;
import com.tazadum.glsl.util.SourcePosition;

public class ParenthesisNode extends BaseNode implements Expression {
    private Expression expression;

    public ParenthesisNode(SourcePosition sourcePosition, Expression expression) {
        super(sourcePosition);
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public int accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public String toString() {
        return "(" + expression + ")";
    }
}
