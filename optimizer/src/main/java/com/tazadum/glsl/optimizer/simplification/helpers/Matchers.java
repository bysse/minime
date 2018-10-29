package com.tazadum.glsl.optimizer.simplification.helpers;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.arithmetic.NumericLeafNode;
import com.tazadum.glsl.language.ast.expression.ParenthesisNode;
import com.tazadum.glsl.language.ast.traits.HasNumeric;
import com.tazadum.glsl.language.model.NumericOperator;
import com.tazadum.glsl.language.type.Numeric;
import com.tazadum.glsl.optimizer.simplification.*;
import com.tazadum.glsl.parser.TypeCombination;

import java.util.function.Function;

import static com.tazadum.glsl.language.type.PredefinedType.*;

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

    public static <T extends Node> ParentMatcher mParent(Class<T> type, Matcher matcher) {
        return new ParentMatcher(matcher) {
            @Override
            public boolean doMatch(Node node) {
                return type.isAssignableFrom(type);
            }
        };
    }

    public static <T extends Node> ParentMatcher mParent(Class<T> type, Function<T, Boolean> condition, Matcher matcher) {
        return new ParentMatcher(matcher) {
            @Override
            public boolean doMatch(Node node) {
                return type.isAssignableFrom(node.getClass()) && condition.apply(type.cast(node));
            }
        };
    }

    public static ParentMatcher mParen(Matcher matcher) {
        return new ParentMatcher(matcher) {
            @Override
            public boolean doMatch(Node node) {
                return node instanceof ParenthesisNode;
            }
        };
    }

    public static FunctionCallMatcher mFunc(String function, Matcher... argumentMatchers) {
        return new FunctionCallMatcher(function, argumentMatchers);
    }

    public static NodeMatcher mLiteral(String value) {
        final Numeric numeric = Numeric.create(value);
        return new NodeMatcher((node) -> compare(numeric, node));
    }

    public static NodeMatcher mLiteral(float value) {
        final Numeric numeric = Numeric.createFloat(value, FLOAT);
        return new NodeMatcher((node) -> compare(numeric, node));
    }

    public static NodeMatcher mLiteral(int value) {
        final Numeric numeric = Numeric.createInt(value, INT);
        return new NodeMatcher((node) -> compare(numeric, node));
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

    public static Matcher mInt() {
        return new NodeMatcher((node) -> node instanceof NumericLeafNode && TypeCombination.anyOf(node.getType(), INT, UINT));
    }

    public static Matcher mFloat() {
        return new NodeMatcher((node) -> node instanceof NumericLeafNode && TypeCombination.anyOf(node.getType(), FLOAT, DOUBLE));
    }

    private static boolean compare(Numeric a, Node n) {
        if (n instanceof HasNumeric) {
            return compare(a, ((HasNumeric) n).getValue());
        }
        return false;
    }

    private static boolean compare(Numeric a, Numeric b) {
        return 0 == a.compareTo(b);
    }
}
