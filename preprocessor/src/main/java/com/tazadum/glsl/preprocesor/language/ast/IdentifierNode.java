package com.tazadum.glsl.preprocesor.language.ast;

import com.tazadum.glsl.preprocesor.language.Expression;
import com.tazadum.glsl.preprocesor.language.Node;

import java.util.Objects;

/**
 * The only use for this Node is to detect when macro-substitution
 * has failed in an expression.
 */
public class IdentifierNode implements Expression {
    private String identifier;

    public IdentifierNode(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return identifier;
    }
}
