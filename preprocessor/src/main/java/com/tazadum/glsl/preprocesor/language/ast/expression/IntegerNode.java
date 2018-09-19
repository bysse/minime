package com.tazadum.glsl.preprocesor.language.ast.expression;

import com.tazadum.glsl.preprocesor.language.Expression;

import java.util.Objects;

/**
 * Node for holding integer values.
 */
public class IntegerNode implements Expression {
    private int value;

    public IntegerNode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return Objects.toString(value);
    }
}
