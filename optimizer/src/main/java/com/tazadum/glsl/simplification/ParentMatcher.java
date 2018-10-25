package com.tazadum.glsl.simplification;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;

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


    @Override
    public CaptureGroups getGroups() {
        if (childMatchers.length == 0) {
            return new CaptureGroups(capture);
        }

        CaptureGroups groups = new CaptureGroups();
        for (int i = 0; i < childMatchers.length; i++) {
            CaptureGroups childGroups = childMatchers[i].getGroups();
            groups.merge(childGroups);
        }
        return groups;
    }
}
