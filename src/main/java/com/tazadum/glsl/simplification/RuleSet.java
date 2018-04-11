package com.tazadum.glsl.simplification;

import com.tazadum.glsl.ast.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static com.tazadum.glsl.language.NumericOperator.*;
import static com.tazadum.glsl.simplification.Matchers.*;

/**
 * Created by Erik on 2018-03-31.
 */
public class RuleSet {
    private List<Rule> rules = new ArrayList<>();

    public RuleSet() {
        rules.addAll(arrangementRules());
        rules.addAll(simpleArithmeticRules());
        rules.addAll(divisionRules());
    }

    public List<Rule> getRules() {
        return rules;
    }

    private Collection<Rule> arrangementRules() {
        List<Rule> list = new ArrayList<>();

        // a * _1 = _1 * a
        list.add(rule(
                mMul(mNot(mNumeric()), mNumeric()),
                operation(MUL, nGroup(1), nGroup(0))
        ));

        // _1 + a = a + _1
        list.add(rule(
                mAdd(mNumeric(), mNot(mNumeric())),
                operation(ADD, nGroup(1), nGroup(0))
        ));

        // -a + b = b - a

        return list;
    }

    public Collection<Rule> simpleArithmeticRules() {
        List<Rule> list = new ArrayList<>();

        // 0 * a = 0
        list.add(rule(mMul(mLiteral(0f), mAny()), nGroup(0)));
        // a * 0 = 0
        list.add(rule(mMul(mAny(), mLiteral(0f)), nGroup(1)));
        // 1 * a = a
        list.add(rule(mMul(mLiteral(1f), mAny()), nGroup(1)));
        // a * 1 = a
        list.add(rule(mMul(mAny(), mLiteral(1f)), nGroup(0)));
        // 0 + a = a
        list.add(rule(mAdd(mLiteral(0f), mAny()), nGroup(1)));
        // a + 0 = a
        list.add(rule(mAdd(mAny(), mLiteral(0f)), nGroup(0)));
        // a - 0 = a
        list.add(rule(mSub(mAny(), mLiteral(0f)), nGroup(0)));
        // _1 - _1 = 0
        list.add(rule(
                mSub(mNumeric(), mNumeric()),
                cSame(0, 1, cSameNumeric()),
                nNumeric(0)
        ));

        return list;
    }

    private Collection<Rule> multiplicationRules() {
        List<Rule> list = new ArrayList<>();

        // _1 * (_2 + a) = _1*_2 + _1*a
        // pow(a,2) = a*a; and the other way around

        return list;
    }

    private Collection<Rule> divisionRules() {
        List<Rule> list = new ArrayList<>();

        // _1 / _1 = 1
        list.add(rule(
                mDiv(mNumeric(), mNumeric()),
                cSame(0, 1, cSameNumeric()),
                nNumeric(1)
        ));
        // a / a = 1
        list.add(rule(
                mDiv(mAny(), mAny()),
                cSame(0, 1, sameTree()),
                nNumeric(1)
        ));

        // (a+b)/a = 1 + b/a

        return list;
    }

    private Rule rule(Matcher matcher, Function<CaptureGroups, Node> replacer) {
        return new RewriteRule(matcher, replacer);
    }

    private Rule rule(Matcher matcher, Function<CaptureGroups, Boolean> constraints, Function<CaptureGroups, Node> replacer) {
        return new RewriteRule(matcher, constraints, replacer);
    }
}
