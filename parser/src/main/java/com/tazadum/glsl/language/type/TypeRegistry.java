package com.tazadum.glsl.language.type;

import com.tazadum.glsl.exception.TypeException;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.parser.Usage;

public interface TypeRegistry {
    /**
     * Declares a type.
     *
     * @param type The non-null type to declare.
     */
    void declare(GLSLType type);

    /**
     * Undeclares struct type.
     *
     * @param structType The non-null struct type to undeclare.
     */
    void undeclare(StructType structType);

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
    GLSLType resolve(String name) throws TypeException;

    /**
     * Returns all usages of a type.
     *
     * @param type The type to search for.
     * @return The usages of a type.
     */
    Usage<GLSLType> usagesOf(GLSLType type);

    /**
     * Remap all node based information onto a new AST and return it as a new TypeRegistry.
     *
     * @param destinationRoot The root of the new AST.
     */
    TypeRegistry remap(Node destinationRoot);
}
