package com.tazadum.glsl.parser.optimizer;


import com.tazadum.glsl.optimizer.BranchingOptimizer;
import com.tazadum.glsl.optimizer.OptimizationDecider;
import com.tazadum.glsl.optimizer.OptimizerContext;
import com.tazadum.glsl.optimizer.OptimizerVisitor;
import com.tazadum.glsl.optimizer.simplification.Rule;
import com.tazadum.glsl.optimizer.simplification.RuleSet;

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
    OptimizerVisitor createVisitor(ParserContext context, BranchRegistry branchRegistry, OptimizationDecider decider) {
        return new RuleOptimizerVisitor(context, decider, ruleSet);
    }
}
