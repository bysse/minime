package com.tazadum.glsl.parser.function;

import com.tazadum.glsl.ast.function.FunctionCallNode;
import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.Usage;

public interface FunctionRegistry {
    void declare(FunctionPrototypeNode node);

    void usage(GLSLContext context, String identifier, FunctionCallNode node);

    FunctionPrototypeNode resolve(String identifier);

    Usage<FunctionPrototypeNode> usagesOf(FunctionPrototypeNode node);

}
