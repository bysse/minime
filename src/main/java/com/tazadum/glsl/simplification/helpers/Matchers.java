package com.tazadum.glsl.simplification.helpers;

import com.tazadum.glsl.ast.HasNumeric;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ParenthesisNode;
import com.tazadum.glsl.ast.arithmetic.FloatLeafNode;
import com.tazadum.glsl.ast.arithmetic.IntLeafNode;
import com.tazadum.glsl.language.Numeric;
import com.tazadum.glsl.language.NumericOperator;
import com.tazadum.glsl.simplification.*;

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

    public static ParentMatcher mParen(Matcher matcher) {
        return new ParentMatcher() {
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

    public static Matcher mInt() {
        return new NodeMatcher((node) -> node instanceof IntLeafNode);
    }

    public static Matcher mFloat() {
        return new NodeMatcher((node) -> node instanceof FloatLeafNode);
    }

}
