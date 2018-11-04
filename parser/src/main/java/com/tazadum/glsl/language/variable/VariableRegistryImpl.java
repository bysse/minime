package com.tazadum.glsl.language.variable;

import com.tazadum.glsl.exception.VariableException;
import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.struct.InterfaceBlockNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.ast.variable.VariableNode;
import com.tazadum.glsl.language.context.ContextAware;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.parser.Usage;
import com.tazadum.glsl.parser.variables.VariableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VariableRegistryImpl implements VariableRegistry {
    private final Logger logger = LoggerFactory.getLogger(VariableRegistryImpl.class);

    private Map<GLSLContext, VariableRegistryContext> declarationMap;
    private Map<VariableDeclarationNode, Usage<VariableDeclarationNode>> usageMap;

    public VariableRegistryImpl() {
        this.declarationMap = new ConcurrentHashMap<>();
        this.usageMap = new ConcurrentHashMap<>();
    }

    private VariableRegistryImpl(Map<GLSLContext, VariableRegistryContext> declarationMap, Map<VariableDeclarationNode, Usage<VariableDeclarationNode>> usageMap) {
        this.declarationMap = declarationMap;
        this.usageMap = usageMap;
    }

    public Map<GLSLContext, VariableRegistryContext> getDeclarationMap() {
        return declarationMap;
    }

    @Override
    public void declareVariable(GLSLContext context, VariableDeclarationNode variableNode) {
        declarationMap.computeIfAbsent(context, VariableRegistryContext::new).declare(variableNode);
        usageMap.computeIfAbsent(variableNode, Usage::new);
    }

    @Override
    public void registerVariableUsage(GLSLContext context, String identifier, Node node) throws VariableException {
        final VariableRegistryContext variableContext = resolveContext(context, identifier, Identifier.Mode.Original);
        if (variableContext == null) {
            throw new VariableException("Unregistered context for identifier " + identifier);
        }

        final VariableDeclarationNode declarationNode = variableContext.resolve(identifier, Identifier.Mode.Original);
        if (declarationNode == null) {
            throw new VariableException("Undeclared identifier " + identifier);
        }

        usageMap.computeIfAbsent(declarationNode, Usage::new).add(node);
    }

    @Override
    public void registerVariableUsage(VariableNode node) {
        VariableDeclarationNode declarationNode = node.getDeclarationNode();
        if (declarationNode == null) {
            throw new IllegalStateException("no declaration was set on the VariableNode");
        }

        usageMap.computeIfAbsent(declarationNode, Usage::new).add(node);
    }

    @Override
    public ResolutionResult resolve(GLSLContext context, String identifier, Identifier.Mode mode) throws VariableException {
        final VariableRegistryContext variableContext = resolveContext(context, identifier, mode);
        if (variableContext == null) {
            throw new VariableException("Unregistered context for identifier " + identifier);
        }

        VariableDeclarationNode declarationNode = variableContext.resolve(identifier, mode);
        if (declarationNode == null) {
            throw new VariableException("Undeclared identifier " + identifier);
        }

        // add usage to the result
        final Usage<VariableDeclarationNode> variableUsage = usageMap.computeIfAbsent(declarationNode, Usage::new);
        return new ResolutionResultImpl(context, declarationNode, variableUsage);
    }

    @Override
    public Usage<VariableDeclarationNode> resolve(VariableDeclarationNode declarationNode) {
        return usageMap.computeIfAbsent(declarationNode, Usage::new);
    }

    @Override
    public List<Usage<VariableDeclarationNode>> getAllVariables() {
        final List<Usage<VariableDeclarationNode>> list = new ArrayList<>();
        for (Map.Entry<VariableDeclarationNode, Usage<VariableDeclarationNode>> entry : usageMap.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }

    @Override
    public boolean dereference(VariableNode node) {
        final VariableDeclarationNode declarationNode = node.getDeclarationNode();
        if (declarationNode == null) {
            return false;
        }

        // remove any usage of the variable
        final Usage<VariableDeclarationNode> usage = usageMap.get(declarationNode);
        if (usage == null) {
            return false;
        }

        return usage.remove(node);
    }

    @Override
    public boolean dereference(VariableDeclarationNode node) {
        for (Map.Entry<GLSLContext, VariableRegistryContext> entry : declarationMap.entrySet()) {
            if (entry.getValue().undeclare(node)) {
                logger.debug("Removing variable {}", node.getIdentifier().original());

                // remove all usages of the node
                Usage<VariableDeclarationNode> usage = usageMap.get(node);
                if (usage != null) {
                    for (Node reference : usage.getUsageNodes()) {
                        if (reference instanceof VariableNode) {
                            logger.trace("  +  Setting declaration reference to null for usage of variable {}", reference);
                            ((VariableNode) reference).setDeclarationNode(null);
                        }
                    }
                }
                // remove the entry
                usageMap.remove(node);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean dereference(GLSLContext context) {
        VariableRegistryContext registryContext = declarationMap.get(context);
        if (registryContext != null) {
            for (VariableDeclarationNode declarationNode : registryContext.getVariables()) {
                dereference(declarationNode);
            }
            declarationMap.remove(context);
            return true;
        }
        return false;
    }

    @Override
    public VariableRegistry remap(ContextAware contextAware, Node base) {
        final Map<VariableDeclarationNode, Usage<VariableDeclarationNode>> usageMapCopy = new ConcurrentHashMap<>();
        usageMap.forEach((declaration, usage) -> {
            if (!declaration.isBuiltIn()) {
                // only remap custom variables since built-in are not part of the AST
                declaration = CloneUtils.remap(base, declaration);
            }

            if (!usage.getUsageNodes().isEmpty()) {
                // only remap variables in use
                usageMapCopy.put(declaration, usage.remap(base, declaration));
            }
        });

        final Map<GLSLContext, VariableRegistryContext> declarationMapCopy = new ConcurrentHashMap<>();
        declarationMap.forEach((context, registry) -> {
            VariableRegistryContext registryContext = registry.remap(contextAware, base, usageMap);
            declarationMapCopy.put(registryContext.getContext(), registryContext);
        });

        return new VariableRegistryImpl(declarationMapCopy, usageMapCopy);
    }

    @Override
    public boolean isEmpty() {
        return declarationMap.isEmpty() && usageMap.isEmpty();
    }

    @Override
    public void apply(GLSLContext glslContext, VariableSet variableSet) {
        for (VariableDeclarationNode declarationNode : variableSet.getDeclarations()) {
            declareVariable(glslContext, declarationNode);
        }

        for (InterfaceBlockNode declarationNode : variableSet.getInterfaceBlocks()) {
            // TODO: implement
            System.err.println("Implement interface node registrations");
        }
    }

    private VariableRegistryContext resolveContext(GLSLContext context, String identifier, Identifier.Mode mode) {
        final VariableRegistryContext variableContext = declarationMap.get(context);
        if (variableContext != null) {
            final VariableDeclarationNode declarationNode = variableContext.resolve(identifier, mode);
            if (declarationNode != null) {
                return variableContext;
            }
        }

        final GLSLContext parentContext = context.getParent();
        if (parentContext == null) {
            return null;
        }

        return resolveContext(parentContext, identifier, mode);
    }
}
