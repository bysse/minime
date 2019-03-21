package com.tazadum.glsl.optimizer.simplification;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.arithmetic.PrefixOperationNode;
import com.tazadum.glsl.language.model.UnaryOperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static com.tazadum.glsl.language.model.NumericOperator.ADD;
import static com.tazadum.glsl.language.model.NumericOperator.MUL;
import static com.tazadum.glsl.optimizer.simplification.helpers.Constraints.*;
import static com.tazadum.glsl.optimizer.simplification.helpers.Generators.*;
import static com.tazadum.glsl.optimizer.simplification.helpers.Matchers.*;

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

        list.add(rule("a * _1 = _1 * a",
            mMul(mNot(mNumeric()), mNumeric()),
            gOperation(MUL, gGroup(1), gGroup(0))
        ));

        list.add(rule("_1 + a = a + _1",
            mAdd(mNumeric(), mNot(mNumeric())),
            gOperation(ADD, gGroup(1), gGroup(0))
        ));

        // -a + b = b - a

        return list;
    }

    public Collection<Rule> simpleArithmeticRules() {
        List<Rule> list = new ArrayList<>();

        list.add(rule("0 * a = 0", mMul(mLiteral(0f), mAny()), gGroup(0)));
        list.add(rule("a * 0 = 0", mMul(mAny(), mLiteral(0f)), gGroup(1)));
        list.add(rule("1 * a = a", mMul(mLiteral(1f), mAny()), gGroup(1)));
        list.add(rule("a * 1 = a", mMul(mAny(), mLiteral(1f)), gGroup(0)));
        list.add(rule("0 + a = a", mAdd(mLiteral(0f), mAny()), gGroup(1)));
        list.add(rule("a + 0 = a", mAdd(mAny(), mLiteral(0f)), gGroup(0)));
        list.add(rule("a - 0 = a", mSub(mAny(), mLiteral(0f)), gGroup(0)));

        list.add(rule("_1 - _1 = 0",
            mSub(mNumeric(), mNumeric()),
            cSame(0, 1, cSameNumeric()),
            gNumeric(0)
        ));

        return list;
    }

    private Collection<Rule> functionOptimizations() {
        List<Rule> list = new ArrayList<>();

        /*
        list.add(rule("_1 * (_2 + a) = _1*_2 + _1*a",
                mMul(mNumeric(), mParen(mAdd(mNumeric(), mAny()))),
                gOperation(ADD, gOperation(MUL, gGroup(0), gGroup(1)), gOperation(MUL, gClone(0), gGroup(2)))
        ));
        */

        list.add(rule("pow(_1,1) = _1",
            mFunc("pow", mNumeric(), mLiteral(1f)),
            gGroup(0)
        ));

        list.add(rule("pow(_1,2) = _1*_1;",
            mFunc("pow", mNumeric(), mLiteral(2f)),
            gOperation(MUL, gGroup(0), gClone(0))
        ));

        list.add(rule("abs(_1) = _1",
            mFunc("abs", mNumeric()),
            gGroup(0)
        ));

        list.add(rule("abs(-_1) = _1",
            mFunc("abs", mParent(PrefixOperationNode.class, op -> op.getOperator() == UnaryOperator.MINUS, mNumeric())),
            gGroup(0)
        ));

        // length(abs(a)) = length(a)


        // sqrt(_1) = _2


        list.add(rule("sin(0) = 0",
            mFunc("sin", mLiteral(0f)),
            gGroup(0)
        ));

        list.add(rule("cos(0) = 1",
            mFunc("cos", mLiteral(0f)),
            gNumeric(1)
        ));

        return list;
    }

    private Collection<Rule> divisionRules() {
        List<Rule> list = new ArrayList<>();

        list.add(rule("0 / a = 0",
            mDiv(mLiteral(0), mAny()),
            gNumeric(0)
        ));

        list.add(rule("_1 / _1 = 1",
            mDiv(mNumeric(), mNumeric()),
            cSame(0, 1, cSameNumeric()),
            gNumeric(1)
        ));

        list.add(rule("a / a = 1",
            mDiv(mAny(), mAny()),
            cSame(0, 1, cSameTree()),
            gNumeric(1)
        ));

        // (a+b)/a = 1 + b/a

        return list;
    }

    private Rule rule(String name, Matcher matcher, Function<CaptureGroups, Node> generator) {
        return new RewriteRule(name, matcher, generator);
    }

    private Rule rule(String name, Matcher matcher, Function<CaptureGroups, Boolean> constraints, Function<CaptureGroups, Node> generator) {
        return new RewriteRule(name, matcher, constraints, generator);
    }
}
