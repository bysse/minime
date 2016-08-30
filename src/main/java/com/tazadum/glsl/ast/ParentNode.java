package com.tazadum.glsl.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author erikb
 * @since 2016-07-31
 */
public class ParentNode implements Node {
    private ParentNode parentNode;
    private List<Node> childNodes;

    public ParentNode() {
        this(null);
    }

    public ParentNode(ParentNode parentNode) {
        this.parentNode = parentNode;
        this.childNodes = new ArrayList<>();
    }

    @Override
    public int getId() {
        if (parentNode == null) {
            return 1;
        }
        return parentNode.getChildId(this);
    }

    @Override
    public ParentNode getParentNode() {
        return parentNode;
    }

    @Override
    public void setParentNode(ParentNode parentNode) {
        this.parentNode = parentNode;
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        ParentNode clone = new ParentNode(newParent);
        for (Node child : childNodes) {
            clone.addChild(child.clone(clone));
        }
        return clone;
    }

    public int getChildCount() {
        return childNodes.size();
    }

    public Iterable<Node> getChildren() {
        return childNodes;
    }

    public <T extends Node> Iterable<T> getChildren(Class<T> nodeType) {
        return (Iterable<T>) childNodes;
    }

    public ParentNode addChild(Node node) {
        if (node == null) {
            throw new IllegalArgumentException("Node is null.");
        }
        node.setParentNode(this);
        childNodes.add(node);
        return this;
    }

    public ParentNode addChild(Node node, int index) {
        if (!equals(node.getParentNode())) {
            throw new IllegalArgumentException("The node doesn't have the correct parent set.");
        }

        childNodes.add(index, node);
        return this;
    }

    public ParentNode removeChild(Node node) {
        childNodes.remove(node);
        return this;
    }

    int getChildId(Node node) {
        int id = getId();

        for (Node childNode : getChildren()) {
            id += 1;
            if (node.equals(childNode)) {
                return id;
            }

            if (childNode instanceof ParentNode) {
                int childId = ((ParentNode) childNode).getChildId(node);
                if (childId == NO_NODE_ID) {
                    id += ((ParentNode) childNode).getChildCount();
                } else {
                    return childId;
                }
            }
        }

        return NO_NODE_ID;
    }
}
