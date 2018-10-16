package com.tazadum.glsl.parser;

import com.tazadum.glsl.language.ast.DefaultASTVisitor;
import com.tazadum.glsl.language.ast.conditional.SwitchNode;
import com.tazadum.glsl.language.ast.function.FunctionDefinitionNode;
import com.tazadum.glsl.language.ast.iteration.DoWhileIterationNode;
import com.tazadum.glsl.language.ast.iteration.ForIterationNode;
import com.tazadum.glsl.language.ast.iteration.WhileIterationNode;
import com.tazadum.glsl.language.context.GLSLContext;

import java.util.Stack;

/**
 * A visitor base class that handles context transitions automatically.
 * Created by Erik on 2016-10-20.
 */
public abstract class ContextASTVisitor<T> extends DefaultASTVisitor<T> {
    private Stack<GLSLContext> contextStack = new Stack<>();

    public ContextASTVisitor(GLSLContext globalContext) {
        contextStack.push(globalContext);
    }

    public GLSLContext getContext() {
        return contextStack.peek();
    }

    @Override
    public T visitWhileIteration(WhileIterationNode node) {
        contextStack.push(node);
        visitChildren(node);
        contextStack.pop();
        return null;
    }

    @Override
    public T visitForIteration(ForIterationNode node) {
        contextStack.push(node);
        visitChildren(node);
        contextStack.pop();
        return null;
    }

    @Override
    public T visitDoWhileIteration(DoWhileIterationNode node) {
        contextStack.push(node);
        visitChildren(node);
        contextStack.pop();
        return null;
    }

    @Override
    public T visitFunctionDefinition(FunctionDefinitionNode node) {
        contextStack.push(node);
        visitChildren(node);
        contextStack.pop();
        return null;
    }

    @Override
    public T visitSwitch(SwitchNode node) {
        contextStack.push(node);
        visitChildren(node);
        contextStack.pop();
        return null;
    }
}
