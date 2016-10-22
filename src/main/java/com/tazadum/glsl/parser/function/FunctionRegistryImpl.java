package com.tazadum.glsl.parser.function;

import com.tazadum.glsl.ast.Identifier;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.function.FunctionCallNode;
import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.parser.Usage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FunctionRegistryImpl implements FunctionRegistry {
    private final Logger logger = LoggerFactory.getLogger(FunctionRegistryImpl.class);

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

    @Override
    public Map<Identifier, Usage<FunctionPrototypeNode>> getUsedFunctions() {
        final Map<Identifier, Usage<FunctionPrototypeNode>> map = new TreeMap<>();
        for (Map.Entry<FunctionPrototypeNode, Usage<FunctionPrototypeNode>> entry : usageMap.entrySet()) {
            if (entry.getValue().getUsageNodes().isEmpty()) {
                continue;
            }
            map.put(entry.getKey().getIdentifier(), entry.getValue());
        }
        return map;
    }

    @Override
    public boolean dereference(FunctionCallNode node) {
        final FunctionPrototypeNode declarationNode = node.getDeclarationNode();
        if (declarationNode == null) {
            return false;
        }

        // remove any usage of the variable
        final Usage<FunctionPrototypeNode> usage = usageMap.get(declarationNode);
        if (usage == null) {
            return false;
        }

        return usage.remove(node);
    }

    @Override
    public boolean dereference(FunctionPrototypeNode node) {
        final List<FunctionPrototypeNode> prototypeList = functionMap.get(node.getIdentifier().original());
        if (prototypeList == null) {
            return false;
        }

        if (!prototypeList.remove(node)) {
            return false;
        }

        final Usage<FunctionPrototypeNode> usage = usageMap.get(node);
        if (usage != null) {
            for (Node reference : usage.getUsageNodes()) {
                if (reference instanceof FunctionCallNode) {
                    logger.debug("Setting declaration reference to null for {}", reference);
                    ((FunctionCallNode) reference).setDeclarationNode(null);
                }
            }
        }

        return true;
    }
}
