package com.tazadum.glsl.parser.variable;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.parser.GLSLContext;

public interface VariableRegistry {
    void declare(GLSLContext context, VariableDeclarationNode variableNode);

    void usage(GLSLContext context, String identifier, Node node);

    ResolutionResult resolve(GLSLContext context, String identifier);
}
