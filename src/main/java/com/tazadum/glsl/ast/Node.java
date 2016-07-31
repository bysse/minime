package com.tazadum.glsl.ast;

public interface Node extends Comparable<Node> {
    int NO_NODE_ID = 0;

    int getId();

    ParentNode getParentNode();

    Node clone(ParentNode newParent);

    default int compareTo(Node o) {
        return o.getId() - getId();
    }
}
