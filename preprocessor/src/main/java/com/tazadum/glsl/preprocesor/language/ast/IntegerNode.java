package com.tazadum.glsl.preprocesor.language.ast;

import com.tazadum.glsl.preprocesor.language.Node;

import java.util.Objects;

/**
 * Created by erikb on 2018-09-17.
 */
public class IntegerNode implements Node {
    private int value;

    public IntegerNode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return Objects.toString(value);
    }
}
