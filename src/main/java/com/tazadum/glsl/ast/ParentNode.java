package com.tazadum.glsl.ast;

import com.tazadum.glsl.language.GLSLType;
import com.tazadum.glsl.util.CloneUtils;

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
        return CloneUtils.cloneChildren(this, new ParentNode());
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        throw new UnsupportedOperationException("Extending classes should override ParentNode::accept");
    }

    @Override
    public GLSLType getType() {
        throw new UnsupportedOperationException("Extending classes should override ParentNode::getType");
    }

    public int getChildCount() {
        return childNodes.size();
    }

    public Node getChild(int index) {
        return childNodes.get(index);
    }

    public <T extends Node> T getChild(int index, Class<T> type) {
        return (T) getChild(index);
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

    public ParentNode setChild(int index, Node node) {
        if (!equals(node.getParentNode())) {
            throw new IllegalArgumentException("The node doesn't have the correct parent set.");
        }
        node.setParentNode(this);
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
