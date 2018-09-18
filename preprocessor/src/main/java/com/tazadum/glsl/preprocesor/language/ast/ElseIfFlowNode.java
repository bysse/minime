package com.tazadum.glsl.preprocesor.language.ast;

import com.tazadum.glsl.preprocesor.language.Flow;
import com.tazadum.glsl.preprocesor.model.DeclarationType;

/**
 * Created by erikb on 2018-09-17.
 */
public class ElseIfFlowNode implements Flow {

    public ElseIfFlowNode() {
    }

    @Override
    public DeclarationType getDeclarationType() {
        return DeclarationType.ELSE_IF;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
