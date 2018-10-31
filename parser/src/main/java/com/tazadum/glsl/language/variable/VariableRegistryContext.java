package com.tazadum.glsl.language.variable;

import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.context.ContextAware;
import com.tazadum.glsl.language.context.GLSLContext;

import java.util.HashSet;
import java.util.Set;

public class VariableRegistryContext {
    private final GLSLContext context;
    private Set<VariableDeclarationNode> variables;


    public VariableRegistryContext(GLSLContext context) {
        this.context = context;
        this.variables = new HashSet<>();
    }

    public GLSLContext getContext() {
        return context;
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

    public VariableRegistryContext remap(ContextAware contextAware, Node base) {
        final VariableRegistryContext remapped = new VariableRegistryContext(CloneUtils.remapContext(contextAware, this.context));
        for (VariableDeclarationNode node : variables) {
            if (node.isBuiltIn()) {
                // this node is a predefined variable which is ok to reuse
                remapped.declare(node);
            } else {
                remapped.declare(CloneUtils.remap(base, node));
            }
        }
        return remapped;
    }
}
