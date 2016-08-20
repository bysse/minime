package com.tazadum.glsl.parser.variable;

import com.tazadum.glsl.ast.VariableDeclarationNode;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.Usage;

public class ResolutionResultImpl implements ResolutionResult {
    private GLSLContext context;
    private VariableDeclarationNode declarationNode;

    public ResolutionResultImpl(GLSLContext context, VariableDeclarationNode declarationNode) {
        this.context = context;
        this.declarationNode = declarationNode;
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
        return null;
    }
}
