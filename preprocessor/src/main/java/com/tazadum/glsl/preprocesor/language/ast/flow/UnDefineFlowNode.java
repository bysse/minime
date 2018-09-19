package com.tazadum.glsl.preprocesor.language.ast.flow;

import com.tazadum.glsl.preprocesor.language.Flow;
import com.tazadum.glsl.preprocesor.model.DeclarationType;

/**
 * Created by erikb on 2018-09-17.
 */
public class UnDefineFlowNode implements Flow {
    private String identifier;

    public UnDefineFlowNode(String identifier) {

        this.identifier = identifier;
    }

    @Override
    public DeclarationType getDeclarationType() {
        return DeclarationType.UNDEF;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return "#undef " + identifier;
    }
}
