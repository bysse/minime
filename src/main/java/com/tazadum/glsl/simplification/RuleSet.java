package com.tazadum.glsl.simplification;

import com.tazadum.glsl.ast.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static com.tazadum.glsl.simplification.Matchers.*;

/**
 * Created by Erik on 2018-03-31.
 */
public class RuleSet {
    private List<Rule> rules = new ArrayList<>();

    public RuleSet() {
        rules.addAll(simpleRules());
    }

    public List<Rule> getRules() {
        return rules;
    }

    public Collection<Rule> simpleRules() {
        List<Rule> list = new ArrayList<>();
        // mul by zero
        list.add(rule(mul(literal(0f), any()), group(0)));
        list.add(rule(mul(any(), literal(0f)), group(1)));
        // mul by one
        list.add(rule(mul(literal(1f), any()), group(1)));
        list.add(rule(mul(any(), literal(1f)), group(0)));
        // div
        list.add(rule(div(numeric(), numeric()), same(0, 1, sameNumeric()), Matchers.numeric(1)));
        // add zero
        list.add(rule(add(literal(0f), any()), group(1)));
        list.add(rule(add(any(), literal(0f)), group(0)));
        // sub zero
        list.add(rule(sub(any(), literal(0f)), group(0)));
        list.add(rule(sub(numeric(), numeric()), same(0, 1, sameNumeric()), Matchers.numeric(0)));

        return list;
    }

    private Collection<Rule> simpleRules2() {
        List<Rule> list = new ArrayList<>();
        return list;
    }

    private Rule rule(Matcher matcher, Function<CaptureGroups, Node> replacer) {
        return new RewriteRule(matcher, replacer);
    }

    private Rule rule(Matcher matcher, Function<CaptureGroups, Boolean> constraints, Function<CaptureGroups, Node> replacer) {
        return new RewriteRule(matcher, constraints, replacer);
    }
}
