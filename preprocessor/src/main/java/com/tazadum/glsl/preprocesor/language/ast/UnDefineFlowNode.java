package com.tazadum.glsl.preprocesor.language.ast;

import com.tazadum.glsl.preprocesor.language.Flow;
import com.tazadum.glsl.preprocesor.model.DeclarationType;

/**
 * Created by erikb on 2018-09-17.
 */
public class UnDefineFlowNode implements Flow {

    public UnDefineFlowNode() {
    }

    @Override
    public DeclarationType getDeclarationType() {
        return DeclarationType.UNDEF;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
