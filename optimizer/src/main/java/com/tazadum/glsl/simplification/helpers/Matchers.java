package com.tazadum.glsl.simplification.helpers;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.arithmetic.NumericLeafNode;
import com.tazadum.glsl.language.ast.expression.ParenthesisNode;
import com.tazadum.glsl.language.ast.traits.HasNumeric;
import com.tazadum.glsl.language.model.NumericOperator;
import com.tazadum.glsl.language.type.Numeric;
import com.tazadum.glsl.parser.TypeCombination;
import com.tazadum.glsl.simplification.*;

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

    public static NodeMatcher mLiteral(float value) {
        final Numeric numeric = new Numeric(value, 0, FLOAT);
        return new NodeMatcher((node) ->
                node instanceof HasNumeric && numeric.equals(((HasNumeric) node).getValue())
        );
    }

    public static NodeMatcher mLiteral(int value) {
        final Numeric numeric = new Numeric(value, 0, INT);
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

    public static Matcher mInt() {
        return new NodeMatcher((node) -> node instanceof NumericLeafNode && TypeCombination.anyOf(node.getType(), INT, UINT));
    }

    public static Matcher mFloat() {
        return new NodeMatcher((node) -> node instanceof NumericLeafNode && TypeCombination.anyOf(node.getType(), FLOAT, DOUBLE));
    }

}
