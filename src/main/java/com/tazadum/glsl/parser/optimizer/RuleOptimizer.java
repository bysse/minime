package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.simplification.Rule;
import com.tazadum.glsl.simplification.RuleSet;

import java.util.List;

/**
 * Created by Erik on 2016-10-20.
 */
public class RuleOptimizer implements Optimizer {
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
    public OptimizerResult run(ParserContext parserContext, OptimizationDecider decider, Node node) {
        final RuleOptimizerVisitor visitor = new RuleOptimizerVisitor(parserContext, decider, ruleSet);

        int changes, totalChanges = 0;
        do {
            visitor.reset();
            Node accept = node.accept(visitor);
            changes = visitor.getChanges();
            totalChanges += changes;

            if (accept != null) {
                node = accept;
            }
        } while (changes > 0);

        return new OptimizerResult(totalChanges, node);
    }
}
