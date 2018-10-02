package com.tazadum.glsl.preprocessor.language.ast.flow;

import com.tazadum.glsl.preprocessor.language.DeclarationType;
import com.tazadum.glsl.preprocessor.language.Flow;
import com.tazadum.glsl.preprocessor.language.ast.BaseNode;
import com.tazadum.glsl.util.SourcePositionId;

/**
 * Created by erikb on 2018-09-17.
 */
public class ElseFlowNode extends BaseNode implements Flow {
    public ElseFlowNode(SourcePositionId sourcePosition) {
        super(sourcePosition);
    }

    @Override
    public DeclarationType getDeclarationType() {
        return DeclarationType.ELSE;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return "#else";
    }
}
