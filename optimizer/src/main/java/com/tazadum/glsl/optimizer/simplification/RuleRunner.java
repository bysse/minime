package com.tazadum.glsl.optimizer.simplification;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.traits.HasNumeric;
import com.tazadum.glsl.parser.ParserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Erik on 2018-03-31.
 */
public class RuleRunner {
    private final Logger logger = LoggerFactory.getLogger(RuleOptimizerVisitor.class);

    public Node runRuleSet(ParserContext parserContext, List<Rule> rules, Node node) {
        for (Rule rule : rules) {
            final Node replacement = run(parserContext, rule, node);
            if (replacement != null) {
                return replacement;
            }
        }
        return null;
    }

    public Node run(ParserContext parserContext, Rule rule, Node node) {
        if (!rule.matches(node)) {
            return null;
        }
        logger.trace("    - Applying rule {}", rule.getName());

        Node replacement = rule.replacement();
        for (Node removed : rule.removedNodes()) {
            if (removed instanceof HasNumeric) {
                continue;
            }

            // Dereference node
            parserContext.dereferenceTree(removed);
        }

        return replacement;
    }
}
