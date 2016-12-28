package com.tazadum.glsl.parser.function;

import com.tazadum.glsl.ast.Identifier;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.function.FunctionCallNode;
import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.parser.Usage;

import java.util.List;

public interface FunctionRegistry {
    void declare(FunctionPrototypeNode node);

    void usage(FunctionPrototypeNode prototypeNode, FunctionCallNode node);

    FunctionPrototypeNode resolve(Identifier identifier, FunctionPrototypeMatcher prototypeMatcher);

    List<FunctionPrototypeNode> resolve(String identifier, Identifier.Mode mode);

    Usage<FunctionPrototypeNode> resolve(FunctionPrototypeNode node);

    List<Usage<FunctionPrototypeNode>> getUsedFunctions();

    boolean dereference(FunctionCallNode node);

    boolean dereference(FunctionPrototypeNode node);

    FunctionRegistry remap(Node base);
}
