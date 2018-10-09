package com.tazadum.glsl.parser;


import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.util.CloneUtils;

import java.util.*;

public class Usage<T> {
    private T target;
    private List<Node> nodes;

    public Usage(T target) {
        this.target = target;
        this.nodes = new ArrayList<>();
    }

    public T getTarget() {
        return target;
    }

    public void add(Node node) {
        nodes.add(node);
    }

    public List<Node> getUsageNodes() {
        // clean up the nodes first
        nodes.removeIf(node -> node.getParentNode() == null);
        return nodes;
    }

    public Set<Node> getUsagesBetween(int from, int to) {
        final Set<Node> set = new TreeSet<>();
        for (Node node : nodes) {
            final int id = node.getId();
            if (from <= id && id <= to) {
                set.add(node);
            }
        }
        return set;
    }

    public boolean remove(Node node) {
        ListIterator<Node> iterator = nodes.listIterator();
        while (iterator.hasNext()) {
            Node element = iterator.next();
            if (node.getId() == element.getId()) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public Usage<T> remap(Node base) {
        return remap(base, this.target);
    }

    public Usage<T> remap(Node base, T target) {
        final Usage<T> usage = new Usage<>(target);
        for (Node node : nodes) {
            usage.add(CloneUtils.remap(base, node));
        }
        return usage;
    }
}
