package com.tazadum.glsl.simplification;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.traits.HasNumeric;
import com.tazadum.glsl.parser.ParserContext;

import java.util.List;

/**
 * Created by Erik on 2018-03-31.
 */
public class RuleRunner {
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
