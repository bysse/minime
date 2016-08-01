package com.tazadum.glsl.parser;

import com.tazadum.glsl.ast.Node;

import java.util.HashSet;
import java.util.Set;

public class Usage {
    private Set<Node> nodes;

    public Usage() {
        this.nodes = new HashSet<>();
    }

    public void add(Node node) {
        nodes.add(node);
    }

    // TODO: add methods for sorting and clipping
}
