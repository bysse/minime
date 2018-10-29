package com.tazadum.glsl.optimizer.simplification;


import com.tazadum.glsl.optimizer.BranchRegistry;
import com.tazadum.glsl.optimizer.BranchingOptimizer;
import com.tazadum.glsl.optimizer.OptimizationDecider;
import com.tazadum.glsl.optimizer.OptimizerVisitor;
import com.tazadum.glsl.parser.ParserContext;

import java.util.List;

/**
 * Pattern matching based arithmetic simplifications.
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
        return "intrinsics";
    }

    @Override
    protected OptimizerVisitor createVisitor(ParserContext context, BranchRegistry branchRegistry, OptimizationDecider decider) {
        return new RuleOptimizerVisitor(context, decider, ruleSet);
    }
}
