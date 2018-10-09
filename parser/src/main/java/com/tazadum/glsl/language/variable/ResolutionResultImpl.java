package com.tazadum.glsl.language.variable;

import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.parser.Usage;

public class ResolutionResultImpl implements ResolutionResult {
    private GLSLContext context;
    private VariableDeclarationNode declarationNode;
    private Usage<VariableDeclarationNode> variableUsage;

    public ResolutionResultImpl(GLSLContext context, VariableDeclarationNode declarationNode, Usage<VariableDeclarationNode> variableUsage) {
        this.context = context;
        this.declarationNode = declarationNode;
        this.variableUsage = variableUsage;
    }

    @Override
    public GLSLContext getContext() {
        return context;
    }

    @Override
    public VariableDeclarationNode getDeclaration() {
        return declarationNode;
    }

    @Override
    public Usage<VariableDeclarationNode> getUsage() {
        return variableUsage;
    }
}
