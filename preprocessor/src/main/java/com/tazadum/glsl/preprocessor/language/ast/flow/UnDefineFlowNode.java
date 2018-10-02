package com.tazadum.glsl.preprocessor.language.ast.flow;

import com.tazadum.glsl.preprocessor.language.DeclarationType;
import com.tazadum.glsl.preprocessor.language.Flow;
import com.tazadum.glsl.preprocessor.language.ast.BaseNode;
import com.tazadum.glsl.util.SourcePositionId;

/**
 * Created by erikb on 2018-09-17.
 */
public class UnDefineFlowNode extends BaseNode implements Flow {
    private String identifier;

    public UnDefineFlowNode(SourcePositionId sourcePosition, String identifier) {
        super(sourcePosition);
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
