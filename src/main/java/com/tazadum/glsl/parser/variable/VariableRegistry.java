package com.tazadum.glsl.parser.variable;

import com.tazadum.glsl.ast.VariableDeclarationNode;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.Usage;

public interface VariableRegistry {
    void declare(GLSLContext context, VariableDeclarationNode variableNode);

    ResolutionResult resolve(GLSLContext context, String identifier);


    interface ResolutionResult {
        GLSLContext getContext();

        Usage<VariableDeclarationNode> getUsage();
    }
}
