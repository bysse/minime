package com.tazadum.glsl.parser;

import com.tazadum.glsl.ast.Node;

import java.util.HashSet;
import java.util.Set;

public class Usage<T> {
    private T target;
    private Set<Node> nodes;

    public Usage(T target) {
        this.target = target;
        this.nodes = new HashSet<>();
    }

    public T getTarget() {
        return target;
    }

    public void add(GLSLContext context, Node node) {
        nodes.add(node);
    }

    public Set<Node> getUsageNodes() {
        return nodes;
    }

    public boolean remove(Node node) {
        return nodes.remove(node);
    }

    // TODO: add methods for sorting and clipping
}
