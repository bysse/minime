package com.tazadum.glsl.parser.variable;

import com.tazadum.glsl.ast.Identifier;
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

    public boolean undeclare(VariableDeclarationNode variableNode) {
        return variables.remove(variableNode);
    }

    public Set<VariableDeclarationNode> getVariables() {
        return variables;
    }

    public VariableDeclarationNode resolve(String identifier, Identifier.Mode mode) {
        for (VariableDeclarationNode node : variables) {
            if (identifier.equals(node.getIdentifier().token(mode))) {
                return node;
            }
        }
        return null;
    }
}
