package com.tazadum.glsl.simplification;

import com.tazadum.glsl.ast.Node;

import java.util.List;
import java.util.function.Function;

/**
 * Created by Erik on 2018-03-30.
 */
public class RewriteRule implements Rule {
    private Matcher matcher;
    private Function<MatchNodeStorage, Node> replacer;
    private MatchNodeStorage matchNodeStorage;

    public RewriteRule(Matcher matcher, Function<MatchNodeStorage, Node> replacer) {
        this.matcher = matcher;
        this.replacer = replacer;
    }

    @Override
    public boolean matches(Node node) {
        matchNodeStorage = null;
        if (matcher.matches(node)) {
            matchNodeStorage = matcher.capture(new MatchNodeStorage());
            return true;
        }
        return false;
    }

    @Override
    public Node replacement() {
        return replacer.apply(matchNodeStorage);
    }

    @Override
    public List<Node> removedNodes() {
        return matchNodeStorage.unreferenced();
    }
}
