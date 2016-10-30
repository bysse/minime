package com.tazadum.glsl.parser.variable;

import com.tazadum.glsl.ast.Identifier;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.ast.variable.VariableNode;
import com.tazadum.glsl.exception.VariableException;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.Usage;
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

    public Map<GLSLContext, VariableRegistryContext> getDeclarationMap() {
        return declarationMap;
    }

    @Override
    public void declare(GLSLContext context, VariableDeclarationNode variableNode) {
        declarationMap.computeIfAbsent(context, VariableRegistryContext::new).declare(variableNode);
        usageMap.computeIfAbsent(variableNode, Usage::new);
    }

    @Override
    public void usage(GLSLContext context, String identifier, Node node) {
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
    public ResolutionResult resolve(GLSLContext context, String identifier, Identifier.Mode mode) {
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
                logger.debug("Removing declaration of {}", node);

                // remove all usages of the node
                Usage<VariableDeclarationNode> usage = usageMap.get(node);
                if (usage != null) {
                    for (Node reference : usage.getUsageNodes()) {
                        if (reference instanceof VariableNode) {
                            logger.debug("Setting declaration reference to null for {}", reference);
                            ((VariableNode) reference).setDeclarationNode(null);
                        }
                    }
                }
                return true;
            }
        }

        return false;
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
