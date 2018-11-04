package com.tazadum.glsl.language.variable;

import com.tazadum.glsl.exception.VariableException;
import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.ast.variable.VariableNode;
import com.tazadum.glsl.language.context.ContextAware;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.parser.Usage;
import com.tazadum.glsl.parser.variables.VariableSet;

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
    void registerVariableUsage(GLSLContext context, String identifier, Node node) throws VariableException;

    /**
     * Register a variable usage from an already resolved VariableNode.
     *
     * @param node The VariableNode.
     */
    void registerVariableUsage(VariableNode node);

    ResolutionResult resolve(GLSLContext context, String identifier, Identifier.Mode mode) throws VariableException;

    Usage<VariableDeclarationNode> resolve(VariableDeclarationNode declarationNode);

    List<Usage<VariableDeclarationNode>> getAllVariables();

    boolean dereference(VariableNode node);

    boolean dereference(VariableDeclarationNode node);

    boolean dereference(GLSLContext context);

    Map<GLSLContext, VariableRegistryContext> getDeclarationMap();

    VariableRegistry remap(ContextAware contextAware, Node base);

    boolean isEmpty();

    /**
     * Apply a whole variable set to the registry.
     */
    void apply(GLSLContext glslContext, VariableSet variableSet);
}
