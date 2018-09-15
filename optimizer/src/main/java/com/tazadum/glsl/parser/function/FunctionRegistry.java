package com.tazadum.glsl.parser.function;

import com.tazadum.glsl.ast.Identifier;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.function.FunctionCallNode;
import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.parser.Usage;

import java.util.List;

public interface FunctionRegistry {
    /**
     * Declares a function in the registry.
     *
     * @param node The function prototype node.
     */
    void declareFunction(FunctionPrototypeNode node);

    /**
     * Registers a function call.
     *
     * @param prototypeNode The prototype which is called.
     * @param node          The function call node.
     */
    void registerFunctionCall(FunctionPrototypeNode prototypeNode, FunctionCallNode node);

    FunctionPrototypeNode resolve(Identifier identifier, FunctionPrototypeMatcher prototypeMatcher);

    List<FunctionPrototypeNode> resolve(String identifier, Identifier.Mode mode);

    Usage<FunctionPrototypeNode> resolve(FunctionPrototypeNode node);

    List<Usage<FunctionPrototypeNode>> getUsedFunctions();

    boolean dereference(FunctionCallNode node);

    boolean dereference(FunctionPrototypeNode node);

    /**
     * Remap the function registry after a new Node hierarchy.
     *
     * @param base
     * @return
     */
    FunctionRegistry remap(Node base);

    boolean isEmpty();
}
