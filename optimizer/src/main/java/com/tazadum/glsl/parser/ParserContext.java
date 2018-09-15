package com.tazadum.glsl.parser;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.parser.function.FunctionRegistry;
import com.tazadum.glsl.parser.optimizer.BranchRegistry;
import com.tazadum.glsl.parser.type.TypeRegistry;
import com.tazadum.glsl.parser.variable.VariableRegistry;

/**
 * @author erikb
 * @since 2016-07-31
 */
public interface ParserContext extends ContextAware {
    TypeRegistry getTypeRegistry();

    VariableRegistry getVariableRegistry();

    FunctionRegistry getFunctionRegistry();

    BranchRegistry getBranchRegistry();

    /**
     * Dereference all variable / function usages and declarations
     * that are found in the sub-tree from this context.
     *
     * @param node Sub-tree to dereference.
     */
    void dereferenceTree(Node node);

    /**
     * Add refernces in the current context for all variable / function
     * uses and declarations.
     *
     * @param node The sub-tree to reference.
     */
    void referenceTree(Node node);

    /**
     * Resolves the context for any given node by searching
     * through all parent nodes. If no suitable context is found
     * the global context will be returned.
     *
     * @param node Node to find the context for.
     * @return The context of the node.
     */
    GLSLContext findContext(Node node);

    /**
     * Remap the context to another Node hierarchy.
     *
     * @param base Base of the Node hierarchy.
     * @return A new ParserContext implementation.
     */
    ParserContext remap(Node base);
}
