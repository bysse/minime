package com.tazadum.glsl.parser.variable;

import com.tazadum.glsl.ast.VariableDeclarationNode;

public interface VariableRegistry {
    void declare(VariableDeclarationNode variableNode);
}
