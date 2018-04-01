package com.tazadum.glsl.simplification;

import com.tazadum.glsl.ast.HasNumeric;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.language.Numeric;

/**
 * Created by Erik on 2018-03-30.
 */
public class NumericLiteralMatcher implements Matcher {
    private Node node;
    private Numeric numeric;

    public NumericLiteralMatcher(Numeric numeric) {
        this.numeric = numeric;
    }

    @Override
    public boolean matches(Node node) {
        this.node = node;

        if (node instanceof HasNumeric) {
            return numeric.equals(((HasNumeric) node).getValue());
        }
        return false;
    }

    @Override
    public MatchNodeStorage capture(MatchNodeStorage matchNodeStorage) {
        return matchNodeStorage.capture(node);
    }
}
