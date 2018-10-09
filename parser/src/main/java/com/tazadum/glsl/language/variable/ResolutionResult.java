package com.tazadum.glsl.language.variable;

import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.parser.Usage;

public interface ResolutionResult {
    GLSLContext getContext();

    VariableDeclarationNode getDeclaration();

    // TODO: change return value to appropriate type
    Usage<VariableDeclarationNode> getUsage();
}
