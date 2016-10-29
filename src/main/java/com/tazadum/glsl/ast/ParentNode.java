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

    private int cachedId = -1;

    public ParentNode() {
        this(null);
    }

    public ParentNode(ParentNode parentNode) {
        this.parentNode = parentNode;
        this.childNodes = new ArrayList<>();
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

    public <T extends Node> Iterable<T> getChildren(Class<T> nodeType) {
        return (Iterable<T>) childNodes;
    }

    public ParentNode addChild(Node node) {
        if (node == null) {
            throw new IllegalArgumentException("Node is null.");
        }
        if (node.getParentNode() != null) {
            node.getParentNode().removeChild(node);
        }
        node.setParentNode(this);
        childNodes.add(node);
        invalidateId();
        return this;
    }

    public ParentNode setChild(int index, Node node) {
        if (node.getParentNode() != null) {
            node.getParentNode().removeChild(node);
        }
        node.setParentNode(this);
        childNodes.set(index, node);
        invalidateId();
        return this;
    }

    public ParentNode removeChild(Node node) {
        node.setParentNode(null);
        if (childNodes.remove(node)) {
            invalidateId();
        }
        return this;
    }

    @Override
    public int getId() {
        if (cachedId < 0) {
            parentNode.invalidateId();
        }
        return cachedId;
    }

    public int calculateId(int id) {
        cachedId = id;
        for (int i = 0; i < getChildCount(); i++) {
            final Node child = getChild(i);
            if (child == null) {
                continue;
            }
            id = child.calculateId(id + 1);
        }
        return id;
    }

    protected void invalidateId() {
        if (parentNode == null) {
            calculateId(1);
        } else {
            parentNode.invalidateId();
        }
    }
}
