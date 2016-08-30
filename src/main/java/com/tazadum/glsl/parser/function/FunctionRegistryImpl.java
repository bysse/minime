package com.tazadum.glsl.parser.function;

import com.tazadum.glsl.ast.function.FunctionCallNode;
import com.tazadum.glsl.ast.function.FunctionDeclarationNode;
import com.tazadum.glsl.exception.ParserException;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.Usage;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FunctionRegistryImpl implements FunctionRegistry {
    private ConcurrentMap<String, FunctionDeclarationNode> functionMap;
    private ConcurrentMap<FunctionDeclarationNode, Usage<FunctionDeclarationNode>> usageMap;

    public FunctionRegistryImpl() {
        functionMap = new ConcurrentHashMap<>();
        usageMap = new ConcurrentHashMap<>();
    }

    @Override
    public void declare(FunctionDeclarationNode node) {
        functionMap.put(node.getIdentifier().original(), node);
    }

    @Override
    public void usage(GLSLContext context, String identifier, FunctionCallNode node) {
        final FunctionDeclarationNode declarationNode = functionMap.get(identifier);
        if (declarationNode == null) {
            throw new ParserException("Unresolved identifier " + identifier);
        }

        usageMap.computeIfAbsent(declarationNode, Usage::new).add(context, node);
    }

    @Override
    public FunctionDeclarationNode resolve(String identifier) {
        return functionMap.get(identifier);
    }

    @Override
    public Usage<FunctionDeclarationNode> usagesOf(FunctionDeclarationNode node) {
        return usageMap.get(node);
    }
}
