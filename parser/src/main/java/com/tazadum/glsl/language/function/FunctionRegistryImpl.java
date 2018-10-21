package com.tazadum.glsl.language.function;

import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.function.FunctionCallNode;
import com.tazadum.glsl.language.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.parser.Usage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    private FunctionRegistryImpl(ConcurrentMap<String, List<FunctionPrototypeNode>> functionMap, ConcurrentMap<FunctionPrototypeNode, Usage<FunctionPrototypeNode>> usageMap) {
        this.functionMap = functionMap;
        this.usageMap = usageMap;
    }

    @Override
    public void declareFunction(FunctionPrototypeNode node) {
        functionMap.computeIfAbsent(node.getIdentifier().original(), (identifier) -> new ArrayList<>()).add(node);
    }

    @Override
    public void registerFunctionCall(FunctionPrototypeNode prototypeNode, FunctionCallNode node) {
        final Usage<FunctionPrototypeNode> usage = usageMap.computeIfAbsent(prototypeNode, Usage::new);
        usage.add(node);
    }

    public FunctionPrototypeNode resolve(Identifier identifier, GLSLType... parameters) {
        return resolve(identifier, new FunctionPrototypeMatcher(FunctionPrototypeMatcher.ANY, parameters));
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
    public List<FunctionPrototypeNode> resolve(String identifier, Identifier.Mode mode) {
        final List<FunctionPrototypeNode> prototypeNodes = new ArrayList<>();

        for (List<FunctionPrototypeNode> prototypeNodeList : functionMap.values()) {
            for (FunctionPrototypeNode prototypeNode : prototypeNodeList) {
                if (identifier.equals(prototypeNode.getIdentifier().token(mode))) {
                    prototypeNodes.add(prototypeNode);
                }
            }
        }
        return prototypeNodes;
    }

    @Override
    public Usage<FunctionPrototypeNode> resolve(FunctionPrototypeNode node) {
        return usageMap.computeIfAbsent(node, Usage::new);
    }

    @Override
    public List<Usage<FunctionPrototypeNode>> getUsedFunctions() {
        final List<Usage<FunctionPrototypeNode>> list = new ArrayList<>();
        for (Map.Entry<FunctionPrototypeNode, Usage<FunctionPrototypeNode>> entry : usageMap.entrySet()) {
            if (entry.getValue().getUsageNodes().isEmpty()) {
                continue;
            }
            list.add(entry.getValue());
        }
        return list;
    }

    @Override
    public boolean dereference(FunctionCallNode node) {
        final FunctionPrototypeNode declarationNode = node.getDeclarationNode();
        if (declarationNode == null) {
            return false;
        }

        // remove mAny usage of the variable
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

    @Override
    public FunctionRegistry remap(Node base) {
        final ConcurrentMap<String, List<FunctionPrototypeNode>> functionMapRemapped = new ConcurrentHashMap<>();
        functionMap.forEach((name, list) -> functionMapRemapped.put(name, remap(base, list)));

        final ConcurrentMap<FunctionPrototypeNode, Usage<FunctionPrototypeNode>> usageMapRemapped = new ConcurrentHashMap<>();
        usageMap.forEach((prototype, usage) -> {
            if (!prototype.getPrototype().isBuiltIn()) {
                prototype = CloneUtils.remap(base, prototype);
            }

            usageMapRemapped.put(prototype, usage.remap(base, prototype));
        });

        return new FunctionRegistryImpl(functionMapRemapped, usageMapRemapped);
    }

    @Override
    public boolean isEmpty() {
        return functionMap.isEmpty();
    }

    private List<FunctionPrototypeNode> remap(Node base, List<FunctionPrototypeNode> nodes) {
        final List<FunctionPrototypeNode> remapped = new ArrayList<>(nodes.size());
        for (FunctionPrototypeNode node : nodes) {
            if (node.getPrototype().isBuiltIn()) {
                // built in function, reuse node
                remapped.add(node);
            } else {
                remapped.add(CloneUtils.remap(base, node));
            }
        }
        return remapped;
    }

}
