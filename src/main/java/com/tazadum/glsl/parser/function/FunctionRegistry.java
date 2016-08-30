package com.tazadum.glsl.parser.function;

import com.tazadum.glsl.ast.function.FunctionCallNode;
import com.tazadum.glsl.ast.function.FunctionDeclarationNode;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.Usage;

public interface FunctionRegistry {
    void declare(FunctionDeclarationNode node);

    void usage(GLSLContext context, String identifier, FunctionCallNode node);

    FunctionDeclarationNode resolve(String identifier);

    Usage<FunctionDeclarationNode> usagesOf(FunctionDeclarationNode node);

}
