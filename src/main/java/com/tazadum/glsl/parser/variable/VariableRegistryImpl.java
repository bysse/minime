package com.tazadum.glsl.parser.variable;

import com.tazadum.glsl.ast.VariableDeclarationNode;
import com.tazadum.glsl.parser.GLSLContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VariableRegistryImpl implements VariableRegistry {
    private Map<GLSLContext, VariableRegistryContext> declarationMap;

    public VariableRegistryImpl() {
        this.declarationMap = new ConcurrentHashMap<>();
    }

    @Override
    public void declare(GLSLContext context, VariableDeclarationNode variableNode) {
        declarationMap.computeIfAbsent(context, VariableRegistryContext::new).declare(variableNode);
    }

    @Override
    public ResolutionResult resolve(GLSLContext context, String identifier) {
        VariableRegistryContext variableContext = declarationMap.get(context);
        if (variableContext == null) {
            return null;
        }
        VariableDeclarationNode declarationNode = variableContext.resolve(identifier);
        if (declarationNode == null) {
            return resolve(context.getParent(), identifier);
        }

        // TODO: construct the result

        return null;
    }
}
