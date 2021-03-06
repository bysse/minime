package com.tazadum.glsl.optimizer.simplification;

import com.tazadum.glsl.language.ast.Node;

import java.util.List;
import java.util.function.Function;

/**
 * Created by Erik on 2018-03-30.
 */
public class RewriteRule implements Rule {
    private final String name;
    private Matcher matcher;
    private Function<CaptureGroups, Boolean> constraints;
    private Function<CaptureGroups, Node> replacer;
    private CaptureGroups captureGroups;

    public RewriteRule(String name, Matcher matcher, Function<CaptureGroups, Node> replacer) {
        this(name, matcher, (groups) -> Boolean.TRUE, replacer);
    }

    public RewriteRule(String name, Matcher matcher, Function<CaptureGroups, Boolean> constraints, Function<CaptureGroups, Node> replacer) {
        this.name = name;
        this.matcher = matcher;
        this.constraints = constraints;
        this.replacer = replacer;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean matches(Node node) {
        captureGroups = null;
        if (matcher.matches(node)) {
            captureGroups = matcher.getGroups();
            return constraints.apply(captureGroups);
        }
        return false;
    }

    @Override
    public Node replacement() {
        return replacer.apply(captureGroups);
    }

    @Override
    public List<Node> removedNodes() {
        return captureGroups.unreferenced();
    }

    public String toString() {
        return name;
    }
}
