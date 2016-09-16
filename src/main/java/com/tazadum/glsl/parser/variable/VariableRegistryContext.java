package com.tazadum.glsl.parser.variable;

import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.parser.GLSLContext;

import java.util.HashSet;
import java.util.Set;

public class VariableRegistryContext {
    private final GLSLContext context;
    private Set<VariableDeclarationNode> variables;


    public VariableRegistryContext(GLSLContext context) {
        this.context = context;
        this.variables = new HashSet<>();
    }

    public void declare(VariableDeclarationNode variableNode) {
        variables.add(variableNode);
    }

    public VariableDeclarationNode resolve(String identifier) {
        for (VariableDeclarationNode node : variables) {
            if (identifier.equals(node.getIdentifier().original())) {
                return node;
            }
        }
        return null;
    }
}
