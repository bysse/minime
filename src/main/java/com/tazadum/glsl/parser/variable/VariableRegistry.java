package com.tazadum.glsl.parser.variable;

import com.tazadum.glsl.ast.VariableDeclarationNode;
import com.tazadum.glsl.parser.GLSLContext;

public interface VariableRegistry {
    void declare(GLSLContext context, VariableDeclarationNode variableNode);

    ResolutionResult resolve(GLSLContext context, String identifier);
}
