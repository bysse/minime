package com.tazadum.glsl.language.type;

import com.tazadum.glsl.exception.TypeException;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.parser.Usage;

public interface TypeRegistry {
    /**
     * Declares a new fully specified type.
     *
     * @param fsType The non-null type to declare.
     */
    void declare(FullySpecifiedType fsType);

    /**
     * Registers that a node uses a certain type.
     *
     * @param context The context in which the node is.
     * @param type    The type that is used.
     * @param node    The Node that is using the type.
     */
    void usage(GLSLContext context, GLSLType type, Node node);

    /**
     * Resolves a type from it's name.
     *
     * @param name The name of the type.
     * @return The type with the given name.
     * @throws TypeException If the type couldn't be found.
     */
    FullySpecifiedType resolve(String name) throws TypeException;

    /**
     * Returns all usages of a type.
     *
     * @param fst The type to search for.
     * @return The usages of a type.
     * * @throws TypeException If the type couldn't be found.
     */
    Usage<GLSLType> usagesOf(FullySpecifiedType fst) throws TypeException;

    /**
     * Remap all node based information onto a new AST and return it as a new TypeRegistry.
     *
     * @param destinationRoot The root of the new AST.
     */
    TypeRegistry remap(Node destinationRoot);
}
