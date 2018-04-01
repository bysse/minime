package com.tazadum.glsl.simplification;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ParentNode;

/**
 * Created by Erik on 2018-03-30.
 */
public abstract class ParentMatcher implements Matcher {
    private Matcher[] childMatchers;
    private Node capture;

    public ParentMatcher(Matcher... childMatchers) {
        this.childMatchers = childMatchers;
    }

    @Override
    public boolean matches(Node node) {
        this.capture = node;
        if (!doMatch(node)) {
            return false;
        }

        if (!(node instanceof ParentNode)) {
            return false;
        }

        int childCount = ((ParentNode) node).getChildCount();
        if (childCount != childMatchers.length) {
            return false;
        }

        for (int i = 0; i < childMatchers.length; i++) {
            Node child = ((ParentNode) node).getChild(i);
            if (!childMatchers[i].matches(child)) {
                return false;
            }
        }

        return true;
    }

    public abstract boolean doMatch(Node node);

    public MatchNodeStorage capture(MatchNodeStorage matchNodeStorage) {
        for (int i = 0; i < childMatchers.length; i++) {
            childMatchers[i].capture(matchNodeStorage);
        }
        return matchNodeStorage;
    }
}
