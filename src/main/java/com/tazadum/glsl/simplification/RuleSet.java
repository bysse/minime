package com.tazadum.glsl.simplification;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.arithmetic.UnaryOperationNode;
import com.tazadum.glsl.language.NumericOperator;
import com.tazadum.glsl.language.UnaryOperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static com.tazadum.glsl.language.NumericOperator.ADD;
import static com.tazadum.glsl.language.NumericOperator.MUL;
import static com.tazadum.glsl.simplification.helpers.Constraints.*;
import static com.tazadum.glsl.simplification.helpers.Generators.*;
import static com.tazadum.glsl.simplification.helpers.Matchers.*;

/**
 * Created by Erik on 2018-03-31.
 */
public class RuleSet {
    private List<Rule> rules = new ArrayList<>();

    public RuleSet() {
        rules.addAll(arrangementRules());
        rules.addAll(simpleArithmeticRules());
        rules.addAll(divisionRules());

        rules.addAll(functionOptimizations());
    }

    public List<Rule> getRules() {
        return rules;
    }

    private Collection<Rule> arrangementRules() {
        List<Rule> list = new ArrayList<>();

        // a * _1 = _1 * a
        list.add(rule(
                mMul(mNot(mNumeric()), mNumeric()),
                gOperation(MUL, gGroup(1), gGroup(0))
        ));

        // _1 + a = a + _1
        list.add(rule(
                mAdd(mNumeric(), mNot(mNumeric())),
                gOperation(ADD, gGroup(1), gGroup(0))
        ));

        // -a + b = b - a

        return list;
    }

    public Collection<Rule> simpleArithmeticRules() {
        List<Rule> list = new ArrayList<>();

        // 0 * a = 0
        list.add(rule(mMul(mLiteral(0f), mAny()), gGroup(0)));
        // a * 0 = 0
        list.add(rule(mMul(mAny(), mLiteral(0f)), gGroup(1)));
        // 1 * a = a
        list.add(rule(mMul(mLiteral(1f), mAny()), gGroup(1)));
        // a * 1 = a
        list.add(rule(mMul(mAny(), mLiteral(1f)), gGroup(0)));
        // 0 + a = a
        list.add(rule(mAdd(mLiteral(0f), mAny()), gGroup(1)));
        // a + 0 = a
        list.add(rule(mAdd(mAny(), mLiteral(0f)), gGroup(0)));
        // a - 0 = a
        list.add(rule(mSub(mAny(), mLiteral(0f)), gGroup(0)));
        // _1 - _1 = 0
        list.add(rule(
                mSub(mNumeric(), mNumeric()),
                cSame(0, 1, cSameNumeric()),
                gNumeric(0)
        ));

        return list;
    }

    private Collection<Rule> functionOptimizations() {
        List<Rule> list = new ArrayList<>();

        /*
        // _1 * (_2 + a) = _1*_2 + _1*a
        list.add(rule(
                mMul(mNumeric(), mParen(mAdd(mNumeric(), mAny()))),
                gOperation(ADD, gOperation(MUL, gGroup(0), gGroup(1)), gOperation(MUL, gClone(0), gGroup(2)))
        ));
        */

        // pow(_1,1) = _1
        list.add(rule(
                mFunc("pow", mNumeric(), mLiteral(1f)),
                gGroup(0)
        ));

        // pow(_1,2) = _1*_1;
        list.add(rule(
                mFunc("pow", mNumeric(), mLiteral(2f)),
                gOperation(NumericOperator.MUL, gGroup(0), gClone(0))
                ));

        // abs(_1) = _1
        list.add(rule(
                mFunc("abs", mNumeric()),
                gGroup(0)
                ));

        // TODO: create matchers for prefix operations
        // abs(-_1) = _1
        list.add(rule(
                mFunc("abs", mParent(UnaryOperationNode.class, op -> op.getOperator() == UnaryOperator.MINUS, mNumeric())),
                gGroup(0)
                ));

        // length(abs(a)) = length(a)


        // sqrt(_1) = _2
        // sin(0) = 0
        // cos(0) = 1

        return list;
    }

    private Collection<Rule> divisionRules() {
        List<Rule> list = new ArrayList<>();

        // _1 / _1 = 1
        list.add(rule(
                mDiv(mNumeric(), mNumeric()),
                cSame(0, 1, cSameNumeric()),
                gNumeric(1)
        ));
        // a / a = 1
        list.add(rule(
                mDiv(mAny(), mAny()),
                cSame(0, 1, cSameTree()),
                gNumeric(1)
        ));

        // (a+b)/a = 1 + b/a

        return list;
    }

    private Rule rule(Matcher matcher, Function<CaptureGroups, Node> generator) {
        return new RewriteRule(matcher, generator);
    }

    private Rule rule(Matcher matcher, Function<CaptureGroups, Boolean> constraints, Function<CaptureGroups, Node> generator) {
        return new RewriteRule(matcher, constraints, generator);
    }
}
