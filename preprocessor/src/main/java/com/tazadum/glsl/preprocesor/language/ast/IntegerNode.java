package com.tazadum.glsl.preprocesor.language.ast;

import com.tazadum.glsl.preprocesor.language.Declaration;
import com.tazadum.glsl.preprocesor.language.Expression;
import com.tazadum.glsl.preprocesor.language.Node;

import java.util.Objects;

/**
 * Created by erikb on 2018-09-17.
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
