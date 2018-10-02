package com.tazadum.glsl.preprocessor.language.ast.flow;

import com.tazadum.glsl.preprocessor.language.DeclarationType;
import com.tazadum.glsl.preprocessor.language.Expression;
import com.tazadum.glsl.preprocessor.language.Flow;
import com.tazadum.glsl.preprocessor.language.ast.BaseNode;
import com.tazadum.glsl.util.SourcePositionId;

/**
 * Created by erikb on 2018-09-17.
 */
public class ElseIfFlowNode extends BaseNode implements Flow {
    private Expression expression;

    public ElseIfFlowNode(SourcePositionId sourcePosition, Expression expression) {
        super(sourcePosition);
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
