package com.tazadum.glsl.language.ast;

import com.tazadum.glsl.language.ast.traits.HasSourcePosition;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

/**
 * @author erikb
 * @since 2016-07-31
 */
public interface Node extends Comparable<Node>, HasSourcePosition {
    int NO_NODE_ID = 0;

    int getId();

    int calculateId(int id);

    /**
     * Returns the source position of the node.
     */
    SourcePosition getSourcePosition();

    ParentNode getParentNode();

    void setParentNode(ParentNode parentNode);

    Node clone(ParentNode newParent);

    <T> T accept(ASTVisitor<T> visitor);

    GLSLType getType();

    Node find(int id);

    default int compareTo(Node o) {
        return o.getId() - getId();
    }

    default boolean hasEqualId(Node n) {
        return getId() == n.getId();
    }
}
