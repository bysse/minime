package com.tazadum.glsl.parser.variable;

import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.Usage;

public interface ResolutionResult {
    GLSLContext getContext();

    VariableDeclarationNode getDeclaration();

    // TODO: change to appropriate type
    Usage<VariableDeclarationNode> getUsage();

}
