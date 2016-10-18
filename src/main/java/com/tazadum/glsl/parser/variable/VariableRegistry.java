package com.tazadum.glsl.parser.variable;

import com.tazadum.glsl.ast.Identifier;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.Usage;

import java.util.Map;

public interface VariableRegistry {
    void declare(GLSLContext context, VariableDeclarationNode variableNode);

    void usage(GLSLContext context, String identifier, Node node);

    ResolutionResult resolve(GLSLContext context, String identifier);

    Map<Identifier, Usage<VariableDeclarationNode>> getUsedVariables();
}
