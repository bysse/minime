package com.tazadum.glsl.preprocesor.language.ast;

import com.tazadum.glsl.preprocesor.language.Flow;
import com.tazadum.glsl.preprocesor.model.DeclarationType;

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
}
