package com.tazadum.glsl.preprocessor.language.ast.flow;

import com.tazadum.glsl.preprocessor.language.Flow;
import com.tazadum.glsl.preprocessor.model.DeclarationType;

/**
 * Created by erikb on 2018-09-17.
 */
public class ElseFlowNode implements Flow {

    public ElseFlowNode() {
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
