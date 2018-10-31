package com.tazadum.glsl.language.context;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.util.CloneUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class ContextAwareImpl implements ContextAware {
    private final GLSLContext globalContext;

    private Stack<GLSLContext> stack = new Stack<>();
    private Set<GLSLContext> contexts = new HashSet<>();

    public ContextAwareImpl(GLSLContext globalContext) {
        this.globalContext = enterContext(globalContext);
    }

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
    public boolean removeContext(GLSLContext context) {
        if (!stack.peek().equals(globalContext)) {
            throw new IllegalStateException("Can't remove a context while the stack is populated!");
        }

        return contexts.removeIf(context::equals);
    }

    @Override
    public void addContext(GLSLContext context) {
        if (context.getParent() == null) {
            throw new IllegalArgumentException("Context needs to have a parent context");
        }
        contexts.add(context);
    }

    @Override
    public Set<GLSLContext> contexts() {
        return contexts;
    }

    @Override
    public ContextAware remap(Node base) {
        final GLSLContext global = new GLSLContextImpl();

        final Set<GLSLContext> remappedContexts = new HashSet<>();
        for (GLSLContext context : contexts) {
            if (context.getParent() == null) {
                remappedContexts.add(global);
            } else {
                final GLSLContext remap = CloneUtils.remap(base, context);
                remap.setParent(CloneUtils.remap(base, context.getParent()));
                remappedContexts.add(remap);
            }
        }

        final Stack<GLSLContext> stackRemapped = new Stack<>();
        for (GLSLContext context : stack) {
            if (context.getParent() == null) {
                stackRemapped.push(global);
            } else {
                GLSLContext remap = CloneUtils.remap(base, context);
                stackRemapped.push(remap);
            }
        }

        return new ContextAwareImpl(remappedContexts, global, stackRemapped);
    }
}
