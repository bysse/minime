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
    public int getId() {
        if (parentNode == null) {
            cachedId = 1;
        }
        if (cachedId < 0) {
            cachedId = parentNode.getChildId(this);
            if (cachedId < 0) {
                cachedId *= -1;
            }
        }
        return cachedId;
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
        node.setParentNode(this);
        childNodes.add(node);
        invalidateId();
        return this;
    }

    public ParentNode setChild(int index, Node node) {
        node.setParentNode(this);
        childNodes.set(index, node);
        invalidateId();
        return this;
    }

    public ParentNode removeChild(Node node) {
        node.setParentNode(null);
        childNodes.remove(node);
        invalidateId();
        return this;
    }

    int getChildId(Node node) {
        int id = getId();

        if (id == NO_NODE_ID) {
            throw new RuntimeException("Node is not part of this tree");
        }

        for (int i=0;i<getChildCount();i++) {
            final Node childNode = getChild(i);

            id += 1;
            if (childNode == null) {
                continue;
            }

            if (node.equals(childNode)) {
                return id;
            }

            if (childNode instanceof ParentNode) {
                final int childId = ((ParentNode) childNode).getChildId(node);
                if (childId < 0) {
                    id = -childId;
                } else {
                    return childId;
                }
            }
        }

        return -id;
    }

    protected void invalidateId() {
        cachedId = -1;
        if (parentNode != null) {
            parentNode.invalidateId();
        }
    }
}
