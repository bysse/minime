package com.tazadum.glsl.simplification;

import com.tazadum.glsl.ast.HasNumeric;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.arithmetic.FloatLeafNode;
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

    public static OperationMatcher div(Matcher a, Matcher b) {
        return new OperationMatcher(NumericOperator.DIV, a, b);
    }

    public static OperationMatcher add(Matcher a, Matcher b) {
        return new OperationMatcher(NumericOperator.ADD, a, b);
    }

    public static OperationMatcher sub(Matcher a, Matcher b) {
        return new OperationMatcher(NumericOperator.SUB, a, b);
    }

    public static NodeMatcher literal(float value) {
        final Numeric numeric = new Numeric(value, 0, true);
        return new NodeMatcher((node) ->
                node instanceof HasNumeric && numeric.equals(((HasNumeric) node).getValue())
        );
    }

    public static NodeMatcher literal(int value) {
        final Numeric numeric = new Numeric(value, 0, false);
        return new NodeMatcher((node) ->
                node instanceof HasNumeric && numeric.equals(((HasNumeric) node).getValue())
        );
    }

    public static Matcher any() {
        return new NodeMatcher((node) -> Boolean.TRUE);
    }

    public static Matcher numeric() {
        return new NodeMatcher((node) -> node instanceof HasNumeric);
    }

    public static Function<CaptureGroups, Node> group(int group) {
        return (storage) -> storage.get(group);
    }

    public static Function<CaptureGroups, Node> numeric(float value) {
        return (storage) -> new FloatLeafNode(new Numeric(value, 0, true));
    }

    public static Function<CaptureGroups, Boolean> same(int groupA, int groupB, NodeComparator comparator) {
        return (groups) -> comparator.equals(groups.get(groupA), groups.get(groupB));
    }

    public static NodeComparator sameNumeric() {
        return (a, b) -> {
            if (a instanceof HasNumeric && b instanceof HasNumeric) {
                Numeric aNumber = ((HasNumeric) a).getValue();
                Numeric bNumber = ((HasNumeric) b).getValue();
                return aNumber.equals(bNumber);
            }
            return false;
        };
    }

    public interface NodeComparator {
        boolean equals(Node a, Node b);
    }
}
