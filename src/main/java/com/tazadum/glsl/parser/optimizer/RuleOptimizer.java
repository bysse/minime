package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.simplification.Rule;
import com.tazadum.glsl.simplification.RuleSet;

import java.util.List;

/**
 * Created by Erik on 2016-10-20.
 */
public class RuleOptimizer extends BranchingOptimizer {
    private List<Rule> ruleSet;

    public RuleOptimizer(List<Rule> ruleSet) {
        this.ruleSet = ruleSet;
    }

    public RuleOptimizer() {
        this(new RuleSet().getRules());
    }

    @Override
    public String name() {
        return "arithmetic simplifications";
    }

    @Override
    OptimizerVisitor createVisitor(ParserContext context, OptimizationDecider decider) {
        return new RuleOptimizerVisitor(context, decider, ruleSet);
    }
}
