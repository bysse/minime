package com.tazadum.glsl.preprocessor.language.ast.flow;

import com.tazadum.glsl.preprocessor.language.DeclarationType;
import com.tazadum.glsl.preprocessor.language.Expression;
import com.tazadum.glsl.preprocessor.language.Flow;
import com.tazadum.glsl.preprocessor.language.ast.BaseNode;
import com.tazadum.glsl.util.SourcePositionId;

/**
 * Created by erikb on 2018-09-17.
 */
public class IfFlowNode extends BaseNode implements Flow {
    private Expression expression;

    public IfFlowNode(SourcePositionId sourcePosition, Expression expression) {
        super(sourcePosition);
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
