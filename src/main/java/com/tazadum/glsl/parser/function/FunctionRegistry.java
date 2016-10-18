package com.tazadum.glsl.parser.function;

import com.tazadum.glsl.ast.Identifier;
import com.tazadum.glsl.ast.function.FunctionCallNode;
import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.parser.Usage;

import java.util.Map;

public interface FunctionRegistry {
    void declare(FunctionPrototypeNode node);

    void usage(FunctionPrototypeNode prototypeNode, FunctionCallNode node);

    FunctionPrototypeNode resolve(Identifier identifier, FunctionPrototypeMatcher prototypeMatcher);

    Usage<FunctionPrototypeNode> usagesOf(FunctionPrototypeNode node);

    Map<Identifier, Usage<FunctionPrototypeNode>> getUsedFunctions();
}
