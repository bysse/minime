package com.tazadum.glsl.language.ast;

import com.tazadum.glsl.util.SourcePositionId;

public class FixedChildParentNode extends ParentNode {
    protected final Node[] nodes;

    public FixedChildParentNode(SourcePositionId position, int children) {
        this(position, children, null);
    }

    public FixedChildParentNode(SourcePositionId position, int children, ParentNode parentNode) {
        super(position, parentNode);
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

    public ParentNode replaceChild(Node replace, Node replacement) {
        replace.setParentNode(null);
        for (int i = 0; i < nodes.length; i++) {
            if (replace.equals(nodes[i])) {
                nodes[i] = replacement;
                replacement.setParentNode(this);
                invalidateId();
                break;
            }
        }
        return this;
    }
}
