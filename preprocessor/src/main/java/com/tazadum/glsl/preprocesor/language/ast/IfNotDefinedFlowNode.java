package com.tazadum.glsl.preprocesor.language.ast;

import com.tazadum.glsl.preprocesor.language.Flow;
import com.tazadum.glsl.preprocesor.model.DeclarationType;

/**
 * Created by erikb on 2018-09-17.
 */
public class IfNotDefinedFlowNode implements Flow {

    public IfNotDefinedFlowNode() {
    }

    @Override
    public DeclarationType getDeclarationType() {
        return DeclarationType.IF_NOT_DEFINED;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
