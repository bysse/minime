package com.tazadum.glsl.preprocessor.language.ast.flow;

import com.tazadum.glsl.preprocessor.language.Expression;
import com.tazadum.glsl.preprocessor.language.Flow;
import com.tazadum.glsl.preprocessor.model.DeclarationType;

/**
 * Created by erikb on 2018-09-17.
 */
public class ElseIfFlowNode implements Flow {
    private Expression expression;

    public ElseIfFlowNode(Expression expression) {
        this.expression = expression;
    }

    @Override
    public DeclarationType getDeclarationType() {
        return DeclarationType.ELSE_IF;
    }

    public Expression getExpression() {
        return expression;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "#elseif " + expression;
    }
}
