package com.tazadum.glsl.preprocessor.language.ast.flow;

import com.tazadum.glsl.preprocessor.language.Expression;
import com.tazadum.glsl.preprocessor.language.Flow;
import com.tazadum.glsl.preprocessor.model.DeclarationType;

/**
 * Created by erikb on 2018-09-17.
 */
public class IfFlowNode implements Flow {
    private Expression expression;

    public IfFlowNode(Expression expression) {
        this.expression = expression;
    }

    @Override
    public DeclarationType getDeclarationType() {
        return DeclarationType.IF;
    }

    public Expression getExpression() {
        return expression;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return "#if " + expression;
    }
}
