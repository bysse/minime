package com.tazadum.glsl.optimizer.simplification;

import com.tazadum.glsl.language.ast.Node;

/**
 * Created by Erik on 2018-03-30.
 */
public class TypeMatcher extends ParentMatcher {
    private Class<? extends Node> type;

    public TypeMatcher(Class<? extends Node> type, Matcher... childMatchers) {
        super(childMatchers);
        this.type = type;
    }

    @Override
    public boolean doMatch(Node node) {
        return type.isAssignableFrom(node.getClass());
    }
}
