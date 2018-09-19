package com.tazadum.glsl.preprocessor.language.ast.expression;

import com.tazadum.glsl.preprocessor.language.Expression;
import com.tazadum.glsl.preprocessor.language.ast.BaseNode;
import com.tazadum.glsl.util.SourcePosition;

import java.util.Objects;

/**
 * Node for holding integer values.
 */
public class IntegerNode extends BaseNode implements Expression {
    private int value;

    public IntegerNode(SourcePosition sourcePosition, int value) {
        super(sourcePosition);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public String toString() {
        return Objects.toString(value);
    }
}
