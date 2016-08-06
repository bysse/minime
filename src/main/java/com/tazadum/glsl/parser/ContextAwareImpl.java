package com.tazadum.glsl.parser;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class ContextAwareImpl implements ContextAware {
    private Stack<GLSLContext> stack = new Stack<>();
    private Set<GLSLContext> contexts = new HashSet<>();

    @Override
    public GLSLContext enterContext(GLSLContext context) {
        contexts.add(context);

        GLSLContext previous = stack.peek();
        if (previous != null) {
            context.setParent(previous);
        }


        stack.add(context);
        return previous;
    }

    @Override
    public GLSLContext exitContext() {
        return stack.pop();
    }

    @Override
    public GLSLContext currentContext() {
        return stack.peek();
    }
}
