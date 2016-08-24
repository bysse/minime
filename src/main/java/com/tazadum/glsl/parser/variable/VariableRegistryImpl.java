package com.tazadum.glsl.parser.variable;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.VariableDeclarationNode;
import com.tazadum.glsl.exception.VariableException;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.Usage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VariableRegistryImpl implements VariableRegistry {
    private Map<GLSLContext, VariableRegistryContext> declarationMap;
    private Map<VariableDeclarationNode, Usage<VariableDeclarationNode>> usageMap;

    public VariableRegistryImpl() {
        this.declarationMap = new ConcurrentHashMap<>();
        this.usageMap = new ConcurrentHashMap<>();
    }

    @Override
    public void declare(GLSLContext context, VariableDeclarationNode variableNode) {
        declarationMap.computeIfAbsent(context, VariableRegistryContext::new).declare(variableNode);
    }

    @Override
    public void usage(GLSLContext context, String identifier, Node node) {
        final VariableRegistryContext variableContext = resolveContext(context, identifier);
        if (variableContext == null) {
            throw new VariableException("Unregistered context for identifier " + identifier);
        }

        final VariableDeclarationNode declarationNode = variableContext.resolve(identifier);
        if (declarationNode == null) {
            throw new VariableException("Undeclared identifier " + identifier);
        }

        usageMap.computeIfAbsent(declarationNode, Usage::new).add(context, node);
    }

    @Override
    public ResolutionResult resolve(GLSLContext context, String identifier) {
        final VariableRegistryContext variableContext = resolveContext(context, identifier);
        if (variableContext == null) {
            throw new VariableException("Unregistered context for identifier " + identifier);
        }

        VariableDeclarationNode declarationNode = variableContext.resolve(identifier);
        if (declarationNode == null) {
            throw new VariableException("Undeclared identifier " + identifier);
        }

        // add usage to the result
        final Usage<VariableDeclarationNode> variableUsage = usageMap.computeIfAbsent(declarationNode, Usage::new);
        return new ResolutionResultImpl(context, declarationNode, variableUsage);
    }

    private VariableRegistryContext resolveContext(GLSLContext context, String identifier) {
        final VariableRegistryContext variableContext = declarationMap.get(context);
        if (variableContext == null) {
            return null;
        }

        final VariableDeclarationNode declarationNode = variableContext.resolve(identifier);
        if (declarationNode != null) {
            return variableContext;
        }
        return resolveContext(context.getParent(), identifier);
    }
}
