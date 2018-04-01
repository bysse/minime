package com.tazadum.glsl.simplification;

import com.tazadum.glsl.ast.HasNumeric;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.language.Numeric;
import com.tazadum.glsl.language.NumericOperator;

import java.util.function.Function;

/**
 * Created by Erik on 2018-03-31.
 */
public class Matchers {
    public static OperationMatcher mul(Matcher a, Matcher b) {
        return new OperationMatcher(NumericOperator.MUL, a, b);
    }

    public static OperationMatcher add(Matcher a, Matcher b) {
        return new OperationMatcher(NumericOperator.ADD, a, b);
    }

    public static OperationMatcher sub(Matcher a, Matcher b) {
        return new OperationMatcher(NumericOperator.SUB, a, b);
    }

    public static NumericLiteralMatcher literal(float value) {
        return new NumericLiteralMatcher(new Numeric(value, 0, true));
    }

    public static NumericLiteralMatcher literal(int value) {
        return new NumericLiteralMatcher(new Numeric(value, 0, false));
    }

    public static Matcher any() {
        return new NodeMatcher((node) -> Boolean.TRUE);
    }

    public static Matcher numeric() {
        return new NodeMatcher((node) -> node instanceof HasNumeric);
    }

    public static Function<MatchNodeStorage, Node> group(int group) {
        return (storage) -> storage.get(group);
    }
}
