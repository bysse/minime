package com.tazadum.glsl.ast;

/**
 * @author erikb
 * @since 2016-07-31
 */
public interface Node extends Comparable<Node> {
    int NO_NODE_ID = 0;

    int getId();

    ParentNode getParentNode();

    void setParentNode(ParentNode parentNode);

    Node clone(ParentNode newParent);

    default int compareTo(Node o) {
        return o.getId() - getId();
    }
}
