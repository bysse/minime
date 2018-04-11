package com.tazadum.glsl.simplification;

import com.tazadum.glsl.ast.HasNumeric;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.arithmetic.FloatLeafNode;
import com.tazadum.glsl.ast.arithmetic.NumericOperationNode;
import com.tazadum.glsl.language.Numeric;
import com.tazadum.glsl.language.NumericOperator;

import java.util.function.Function;

/**
 * Created by Erik on 2018-03-31.
 */
public class Matchers {
    public static OperationMatcher mMul(Matcher a, Matcher b) {
        return new OperationMatcher(NumericOperator.MUL, a, b);
    }

    public static OperationMatcher mDiv(Matcher a, Matcher b) {
        return new OperationMatcher(NumericOperator.DIV, a, b);
    }

    public static OperationMatcher mAdd(Matcher a, Matcher b) {
        return new OperationMatcher(NumericOperator.ADD, a, b);
    }

    public static OperationMatcher mSub(Matcher a, Matcher b) {
        return new OperationMatcher(NumericOperator.SUB, a, b);
    }

    public static NodeMatcher mLiteral(float value) {
        final Numeric numeric = new Numeric(value, 0, true);
        return new NodeMatcher((node) ->
                node instanceof HasNumeric && numeric.equals(((HasNumeric) node).getValue())
        );
    }

    public static NodeMatcher mLiteral(int value) {
        final Numeric numeric = new Numeric(value, 0, false);
        return new NodeMatcher((node) ->
                node instanceof HasNumeric && numeric.equals(((HasNumeric) node).getValue())
        );
    }

    public static Matcher mAny() {
        return new NodeMatcher((node) -> Boolean.TRUE);
    }

    public static Matcher mNot(Matcher matcher) {
        return new NodeMatcher((node) -> !matcher.matches(node));
    }

    public static Matcher mNumeric() {
        return new NodeMatcher((node) -> node instanceof HasNumeric);
    }

    public static Function<CaptureGroups, Node> nGroup(int group) {
        return (storage) -> storage.get(group);
    }

    public static Function<CaptureGroups, Node> operation(NumericOperator operator, Function<CaptureGroups, Node> a, Function<CaptureGroups, Node> b) {
        return (storage) -> {
            Node left = a.apply(storage);
            Node right = b.apply(storage);
            return new NumericOperationNode(operator, left, right);
        };
    }

    public static Function<CaptureGroups, Node> nNumeric(float value) {
        return (storage) -> new FloatLeafNode(new Numeric(value, 0, true));
    }

    public static Function<CaptureGroups, Boolean> and(Function<CaptureGroups, Boolean>... conditions) {
        return (groups) -> {
            for (Function<CaptureGroups, Boolean> condition : conditions) {
                if (!condition.apply(groups)) {
                    return false;
                }
            }
            return true;
        };
    }

    public static Function<CaptureGroups, Boolean> or(Function<CaptureGroups, Boolean>... conditions) {
        return (groups) -> {
            for (Function<CaptureGroups, Boolean> condition : conditions) {
                if (condition.apply(groups)) {
                    return true;
                }
            }
            return false;
        };
    }

    public static Function<CaptureGroups, Boolean> apply(int group, Function<Node, Boolean> condition) {
        return (groups) -> condition.apply(groups.get(group));
    }

    public static Function<CaptureGroups, Boolean> nNumeric(int group, Function<Numeric, Boolean> condition) {
        return apply(group, (node) -> {
            if (node instanceof HasNumeric) {
                return condition.apply(((HasNumeric) node).getValue());
            }
            return false;
        });
    }

    public static Function<CaptureGroups, Boolean> cSame(int groupA, int groupB, NodeComparator comparator) {
        return (groups) -> comparator.equals(groups.get(groupA), groups.get(groupB));
    }

    public static NodeComparator cSameNumeric() {
        return (a, b) -> {
            if (a instanceof HasNumeric && b instanceof HasNumeric) {
                Numeric aNumber = ((HasNumeric) a).getValue();
                Numeric bNumber = ((HasNumeric) b).getValue();
                return aNumber.equals(bNumber);
            }
            return false;
        };
    }

    public static NodeComparator sameTree() {
        return new TreeNodeComparator();
    }
}
