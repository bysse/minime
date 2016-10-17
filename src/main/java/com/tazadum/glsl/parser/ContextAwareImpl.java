package com.tazadum.glsl.parser;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class ContextAwareImpl implements ContextAware {
    private final GLSLContext globalContext;

    private Stack<GLSLContext> stack = new Stack<>();
    private Set<GLSLContext> contexts = new HashSet<>();

    public ContextAwareImpl() {
        this.globalContext = enterContext(new GLSLContextImpl());
    }

    @Override
    public GLSLContext enterContext(GLSLContext context) {
        if (!stack.isEmpty()) {
            GLSLContext previous = stack.peek();
            if (previous != null) {
                context.setParent(previous);
            }
        }

        stack.add(context);
        contexts.add(context);

        return context;
    }

    @Override
    public GLSLContext exitContext() {
        return stack.pop();
    }

    @Override
    public GLSLContext currentContext() {
        return stack.peek();
    }

    @Override
    public GLSLContext globalContext() {
        return globalContext;
    }
}
