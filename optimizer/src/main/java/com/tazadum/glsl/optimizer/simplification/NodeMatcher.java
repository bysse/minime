package com.tazadum.glsl.optimizer.simplification;

import com.tazadum.glsl.language.ast.Node;

import java.util.function.Function;

/**
 * Created by Erik on 2018-03-31.
 */
public class NodeMatcher implements Matcher {
    private Node node;
    private Function<Node, Boolean> condition;

    public NodeMatcher(Function<Node, Boolean> condition) {
        this.condition = condition;
    }

    @Override
    public boolean matches(Node node) {
        this.node = node;
        return condition.apply(node);
    }

    @Override
    public CaptureGroups getGroups() {
        return new CaptureGroups(node);
    }
}
