package com.tazadum.glsl.parser.function;

import com.tazadum.glsl.ast.Identifier;
import com.tazadum.glsl.ast.function.FunctionCallNode;
import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.parser.Usage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FunctionRegistryImpl implements FunctionRegistry {
    private ConcurrentMap<String, List<FunctionPrototypeNode>> functionMap;
    private ConcurrentMap<FunctionPrototypeNode, Usage<FunctionPrototypeNode>> usageMap;

    public FunctionRegistryImpl() {
        functionMap = new ConcurrentHashMap<>();
        usageMap = new ConcurrentHashMap<>();
    }

    @Override
    public void declare(FunctionPrototypeNode node) {
        functionMap.computeIfAbsent(node.getIdentifier().original(), (identifier) -> new ArrayList<>()).add(node);
    }

    @Override
    public void usage(FunctionPrototypeNode prototypeNode, FunctionCallNode node) {
        final Usage<FunctionPrototypeNode> usage = usageMap.computeIfAbsent(prototypeNode, Usage::new);
        usage.add(prototypeNode, node);
    }

    @Override
    public FunctionPrototypeNode resolve(Identifier identifier, FunctionPrototypeMatcher prototypeMatcher) {
        final List<FunctionPrototypeNode> prototypeNodes = functionMap.get(identifier.original());
        if (prototypeNodes == null || prototypeNodes.isEmpty()) {
            return null;
        }

        for (FunctionPrototypeNode prototypeNode : prototypeNodes) {
            if (prototypeMatcher.matches(prototypeNode.getPrototype())) {
                return prototypeNode;
            }
        }

        return null;
    }

    @Override
    public Usage<FunctionPrototypeNode> usagesOf(FunctionPrototypeNode node) {
        return usageMap.get(node);
    }
}
