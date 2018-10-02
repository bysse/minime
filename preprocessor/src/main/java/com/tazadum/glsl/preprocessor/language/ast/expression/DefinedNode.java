package com.tazadum.glsl.preprocessor.language.ast.expression;

import com.tazadum.glsl.preprocessor.language.Expression;
import com.tazadum.glsl.preprocessor.language.ast.BaseNode;
import com.tazadum.glsl.util.SourcePositionId;

public class DefinedNode extends BaseNode implements Expression {
    private String identifier;

    public DefinedNode(SourcePositionId sourcePosition, String identifier) {
        super(sourcePosition);
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public int accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public String toString() {
        return "defined(" + identifier + ")";
    }
}
