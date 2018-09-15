package com.tazadum.glsl.parser;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.util.CloneUtils;

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

    private ContextAwareImpl(Set<GLSLContext> contexts, GLSLContext globalContext, Stack<GLSLContext> stack) {
        this.contexts = contexts;
        this.globalContext = globalContext;
        this.stack = stack;
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

    @Override
    public Set<GLSLContext> contexts() {
        return contexts;
    }

    @Override
    public ContextAware remap(Node base) {
        final GLSLContext global = new GLSLContextImpl();

        final Set<GLSLContext> contextsRemapped = new HashSet<>();
        for (GLSLContext context : contexts) {
            if (context.getParent() == null) {
                contextsRemapped.add(global);
            } else {
                final GLSLContext remap = CloneUtils.remap(base, context);
                remap.setParent(CloneUtils.remap(base, context.getParent()));
                contextsRemapped.add(remap);
            }
        }

        final Stack<GLSLContext> stackRemapped = new Stack<>();
        for (GLSLContext context : stack) {
            if (context.getParent() == null) {
                stackRemapped.push(global);
            } else {
                stackRemapped.push(CloneUtils.remap(base, context));
            }
        }

        return new ContextAwareImpl(contextsRemapped, global, stackRemapped);
    }
}
