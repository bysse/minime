package com.tazadum.glsl.preprocessor.language.ast.flow;

import com.tazadum.glsl.preprocessor.language.Flow;
import com.tazadum.glsl.preprocessor.model.DeclarationType;

/**
 * Created by erikb on 2018-09-17.
 */
public class IfNotDefinedFlowNode implements Flow {
    private String identifier;

    public IfNotDefinedFlowNode(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public DeclarationType getDeclarationType() {
        return DeclarationType.IF_NOT_DEFINED;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return "#ifndef " + identifier;
    }
}
