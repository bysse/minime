package com.tazadum.glsl.optimizer.inline;

import com.tazadum.glsl.exception.BadImplementationException;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.ast.variable.VariableNode;
import com.tazadum.glsl.language.context.GLSLContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple context based lookup for variables where the declarations are being renamed.
 * Created by erikb on 2018-11-03.
 */
public class ContextAwareLookup {
    private final GLSLContext parentContext;
    private final Map<GLSLContext, ContextLookup> contextMap;

    public ContextAwareLookup(GLSLContext parentContext) {
        this.parentContext = parentContext;
        this.contextMap = new HashMap<>();
    }

    public VariableDeclarationNode resolve(VariableNode variableNode) {
        if (variableNode.getDeclarationNode() == null) {
            // no declaration node, we are fffffffuuuuuuuuuuuu
            throw new BadImplementationException("VariableNode has been disconnected");
        }

        GLSLContext context = findContext(variableNode);
        ContextLookup lookup = contextMap.get(context);

        if (lookup == null) {
            // this variable is not declared in this lookup, it can be a global variable
            return null;
        }

        String identifier = variableNode.getDeclarationNode().getIdentifier().original();
        return lookup.resolve(identifier);
    }


    /**
     * Declares a variable in the lookup.
     * This method is intended to be called with a cloned node that isn't attached
     * to it's new parent yet.
     *
     * @param declarationNode The variable declaration.
     * @param identifier      The new identifier for the variable
     */
    public void declare(VariableDeclarationNode declarationNode, String identifier) {
        final String original = declarationNode.getIdentifier().original();

        GLSLContext context = findContext(declarationNode);
        ContextLookup lookup = createContext(context);
        lookup.declare(declarationNode, original, identifier);
    }

    private ContextLookup createContext(GLSLContext context) {
        ContextLookup lookup = contextMap.get(context);
        if (lookup != null) {
            return lookup;
        }

        // check if the context is the top most context
        if (context == null || context == parentContext) {
            lookup = new ContextLookup(null);
            contextMap.put(context, lookup);
            return lookup;
        }

        // find the parent context of the provided context
        GLSLContext parentContext = findContext(((Node) context).getParentNode());
        ContextLookup parentLookup = createContext(parentContext);
        lookup = new ContextLookup(parentLookup);
        contextMap.put(context, lookup);

        return lookup;
    }

    private GLSLContext findContext(Node node) {
        if (node instanceof GLSLContext) {
            return (GLSLContext) node;
        }
        if (node == null || node.getParentNode() == null) {
            return parentContext;
        }
        return findContext(node.getParentNode());
    }

    private static class ContextLookup {
        ContextLookup parent;
        Map<String, VariableDeclarationNode> originalMap;
        Map<String, VariableDeclarationNode> renamedMap;

        ContextLookup(ContextLookup parent) {
            this.parent = parent;
            this.originalMap = new HashMap<>();
            this.renamedMap = new HashMap<>();
        }

        void declare(VariableDeclarationNode declarationNode, String original, String renamed) {
            originalMap.put(original, declarationNode);
            renamedMap.put(renamed, declarationNode);
        }

        VariableDeclarationNode resolve(String identifier) {
            VariableDeclarationNode declarationNode = originalMap.get(identifier);
            if (declarationNode == null) {
                declarationNode = renamedMap.get(identifier);
                if (declarationNode == null) {
                    if (parent == null) {
                        return null;
                    }
                    return parent.resolve(identifier);
                }
            }
            return declarationNode;
        }


    }
}
