package com.tazadum.glsl.ast;

import com.tazadum.glsl.language.GLSLType;

/**
 * @author erikb
 * @since 2016-07-31
 */
public interface Node extends Comparable<Node> {
    int NO_NODE_ID = 0;

    int getId();

    int calculateId(int id);

    ParentNode getParentNode();

    void setParentNode(ParentNode parentNode);

    Node clone(ParentNode newParent);

    <T> T accept(ASTVisitor<T> visitor);

    GLSLType getType();

    Node find(int id);

    default int compareTo(Node o) {
        return o.getId() - getId();
    }
}
