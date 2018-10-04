package com.tazadum.glsl.preprocessor.language.ast.expression;

import com.tazadum.glsl.preprocessor.language.Expression;
import com.tazadum.glsl.preprocessor.language.ast.BaseNode;
import com.tazadum.glsl.util.SourcePositionId;

/**
 * The only use for this Node is to detect when macro-substitution
 * has failed in an expression.
 */
public class IdentifierNode extends BaseNode implements Expression {
    private final String identifier;

    public IdentifierNode(SourcePositionId sourcePosition, String identifier) {
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
        return identifier;
    }
}
