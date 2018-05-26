package com.tazadum.glsl.parser.variable;

import com.tazadum.glsl.ast.Identifier;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.ast.variable.VariableNode;
import com.tazadum.glsl.parser.ContextAware;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.Usage;

import java.util.List;
import java.util.Map;

public interface VariableRegistry {
    /**
     * Declares a variables in a context.
     *
     * @param context      The context to define the variable in.
     * @param variableNode The variable to declare.
     */
    void declareVariable(GLSLContext context, VariableDeclarationNode variableNode);

    /**
     * Registers a variable usage in a context.
     *
     * @param context    The context in which the variable was used.
     * @param identifier The identifier of the variable.
     * @param node       The variable usage node.
     */
    void registerVariableUsage(GLSLContext context, String identifier, Node node);

    /**
     * Register a variable usage from an already resolved VariableNode.
     *
     * @param node    The VariableNode.
     */
    void registerVariableUsage(VariableNode node);

    ResolutionResult resolve(GLSLContext context, String identifier, Identifier.Mode mode);

    Usage<VariableDeclarationNode> resolve(VariableDeclarationNode declarationNode);

    List<Usage<VariableDeclarationNode>> getAllVariables();

    boolean dereference(VariableNode node);

    boolean dereference(VariableDeclarationNode node);

    Map<GLSLContext, VariableRegistryContext> getDeclarationMap();

    VariableRegistry remap(Node base, ContextAware contextAwareRemap);

    boolean isEmpty();
}
