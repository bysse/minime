package com.tazadum.glsl.language.function;

import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.function.FunctionCallNode;
import com.tazadum.glsl.language.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.context.ContextAware;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.parser.Usage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FunctionRegistryImpl implements FunctionRegistry {
    private final Logger logger = LoggerFactory.getLogger(FunctionRegistryImpl.class);

    private final BuiltInFunctionRegistry builtInRegistry;
    private final ConcurrentMap<String, Set<FunctionPrototypeNode>> functionMap;
    private final ConcurrentMap<FunctionPrototypeNode, Usage<FunctionPrototypeNode>> usageMap;

    public FunctionRegistryImpl(BuiltInFunctionRegistry builtInRegistry) {
        this.builtInRegistry = builtInRegistry;
        functionMap = new ConcurrentHashMap<>();
        usageMap = new ConcurrentHashMap<>();
    }

    private FunctionRegistryImpl(BuiltInFunctionRegistry builtInRegistry, ConcurrentMap<String, Set<FunctionPrototypeNode>> functionMap, ConcurrentMap<FunctionPrototypeNode, Usage<FunctionPrototypeNode>> usageMap) {
        this.builtInRegistry = builtInRegistry;
        this.functionMap = functionMap;
        this.usageMap = usageMap;
    }

    @Override
    public void declareFunction(FunctionPrototypeNode node) {
        functionMap.computeIfAbsent(node.getIdentifier().original(), (identifier) -> new HashSet<>()).add(node);
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
        final Set<FunctionPrototypeNode> prototypeNodes = functionMap.get(identifier.original());

        if (prototypeNodes != null && !prototypeNodes.isEmpty()) {
            for (FunctionPrototypeNode prototypeNode : prototypeNodes) {
                if (prototypeMatcher.matches(prototypeNode.getPrototype())) {
                    return prototypeNode;
                }
            }
        }

        return builtInRegistry.resolve(identifier, prototypeMatcher);
    }

    @Override
    public List<FunctionPrototypeNode> resolve(String identifier, Identifier.Mode mode) {
        final List<FunctionPrototypeNode> prototypeNodes = new ArrayList<>();

        for (Set<FunctionPrototypeNode> prototypeNodeList : functionMap.values()) {
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
        final String identifier = node.getIdentifier().original();
        final Set<FunctionPrototypeNode> prototypeList = functionMap.get(identifier);
        if (prototypeList == null) {
            return false;
        }

        if (!prototypeList.remove(node)) {
            // the specific prototype doesn't exist in the function map anymore
            return false;
        }

        if (prototypeList.isEmpty()) {
            // remove the entire entry
            functionMap.remove(identifier);
        }

        // clean up all function usage
        final Usage<FunctionPrototypeNode> usage = usageMap.remove(node);
        if (usage != null) {
            for (Node reference : usage.getUsageNodes()) {
                if (reference instanceof FunctionCallNode) {
                    logger.trace("Setting declaration reference to null for function call {}", reference);
                    ((FunctionCallNode) reference).setDeclarationNode(null);
                }
            }
        }

        return true;
    }

    @Override
    public FunctionRegistry remap(ContextAware contextAware, Node base) {
        final ConcurrentMap<String, Set<FunctionPrototypeNode>> functionMapRemapped = new ConcurrentHashMap<>();
        functionMap.forEach((name, set) -> functionMapRemapped.put(name, remap(base, set)));

        final ConcurrentMap<FunctionPrototypeNode, Usage<FunctionPrototypeNode>> usageMapRemapped = new ConcurrentHashMap<>();
        usageMap.forEach((prototype, usage) -> {
            if (!prototype.getPrototype().isBuiltIn()) {
                // only remap custom functions since built-in are not part of the AST
                prototype = CloneUtils.remap(base, prototype);
            }

            if (!usage.getUsageNodes().isEmpty()) {
                // only remap functions in use
                usageMapRemapped.put(prototype, usage.remap(base, prototype));
            }
        });

        return new FunctionRegistryImpl(builtInRegistry, functionMapRemapped, usageMapRemapped);
    }

    @Override
    public boolean isEmpty() {
        return functionMap.isEmpty();
    }

    private Set<FunctionPrototypeNode> remap(Node base, Set<FunctionPrototypeNode> nodes) {
        final Set<FunctionPrototypeNode> remapped = new HashSet<>(nodes.size());
        for (FunctionPrototypeNode node : nodes) {
            if (node.getPrototype().isBuiltIn()) {
                // this node is a predefined variable which is ok to reuse
                remapped.add(node);
            } else {
                remapped.add(CloneUtils.remap(base, node));
            }
        }
        return remapped;
    }

}
