package com.tazadum.glsl.ast;

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
            if (node.getParentNode() != null) {
                node.getParentNode().removeChild(node);
            }
            node.setParentNode(this);
        }
        nodes[index] = node;
        invalidateId();
        return this;
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        throw new UnsupportedOperationException("clone is not supported");
    }

    @Override
    public int getChildCount() {
        return nodes.length;
    }

    @Override
    public Node getChild(int index) {
        if (index < 0 || index >= nodes.length) {
            throw new IllegalArgumentException("Index is outside of range");
        }
        return nodes[index];
    }

    @Override
    public ParentNode removeChild(Node node) {
        for (int i = 0; i < nodes.length; i++) {
            if (node.equals(nodes[i])) {
                nodes[i] = null;
                invalidateId();
                break;
            }
        }
        return this;
    }
}
