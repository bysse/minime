package com.tazadum.glsl.ast;

import java.util.ArrayList;
import java.util.List;

public class FixedChildParentNode extends ParentNode {
    protected final Node[] nodes;

    public FixedChildParentNode(int children) {
        this(children, null);
    }

    public FixedChildParentNode(int children, ParentNode parentNode) {
        super(parentNode);
        this.nodes = new Node[children];
    }

    @Override
    public ParentNode addChild(Node node) {
        throw new UnsupportedOperationException("addChild is not supported");
    }

    @Override
    public ParentNode setChild(int index, Node node) {
        if (index < 0 || index >= nodes.length) {
            throw new IllegalArgumentException("Index is outside of range");
        }

        if (node != null) {
            node.setParentNode(this);
        }
        nodes[index] = node;
        return this;
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        throw new UnsupportedOperationException("clone is not supported");
    }

    @Override
    public int getChildCount() {
        int count = 0;
        for (Node node : nodes) {
            if (node != null) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Node getChild(int index) {
        if (index < 0 || index >= nodes.length) {
            throw new IllegalArgumentException("Index is outside of range");
        }
        return nodes[index];
    }

    @Override
    public Iterable<Node> getChildren() {
        List<Node> list = new ArrayList<>();
        for (Node node : nodes) {
            if (node != null) {
                list.add(node);
            }
        }
        return list;
    }

    @Override
    public ParentNode removeChild(Node node) {
        throw new UnsupportedOperationException("removeChild is not supported");
    }
}
