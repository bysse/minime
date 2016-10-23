package com.tazadum.glsl.parser.variable;

import com.tazadum.glsl.ast.Identifier;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.ast.variable.VariableNode;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.Usage;

import java.util.Map;

public interface VariableRegistry {
    void declare(GLSLContext context, VariableDeclarationNode variableNode);

    void usage(GLSLContext context, String identifier, Node node);

    ResolutionResult resolve(GLSLContext context, String identifier);

    Usage<VariableDeclarationNode> resolve(VariableDeclarationNode declarationNode);

    Map<Identifier, Usage<VariableDeclarationNode>> getUsedVariables();

    boolean dereference(VariableNode node);

    boolean dereference(VariableDeclarationNode node);
}
