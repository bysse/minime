package com.tazadum.glsl.preprocessor.language.ast.flow;

import com.tazadum.glsl.preprocessor.language.Flow;
import com.tazadum.glsl.preprocessor.model.DeclarationType;

/**
 * Created by erikb on 2018-09-17.
 */
public class EndIfFlowNode implements Flow {

    public EndIfFlowNode() {
    }

    @Override
    public DeclarationType getDeclarationType() {
        return DeclarationType.END_IF;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return "#endif";
    }
}
