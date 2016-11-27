package com.tazadum.glsl.ast;

import com.tazadum.glsl.language.GLSLType;

/**
 * @author erikb
 * @since 2016-07-31
 */
public class LeafNode implements Node {
    private ParentNode parentNode;
    private int cachedId = -1;

    public LeafNode() {
        this(null);
    }

    public LeafNode(ParentNode parentNode) {
        this.parentNode = parentNode;
    }

    @Override
    public int getId() {
        if (cachedId < 0) {
            if (parentNode == null) {
                return 1;
            } else {
                parentNode.invalidateId();
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
    public LeafNode clone(ParentNode newParent) {
        throw new UnsupportedOperationException("Extending classes should override LeafNode::clone");
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        throw new UnsupportedOperationException("Extending classes should override LeafNode::accept");
    }

    @Override
    public GLSLType getType() {
        return null;
    }

    @Override
    public Node find(int id) {
        if (getId() == id) {
            return this;
        }
        return null;
    }

    public int calculateId(int id) {
        cachedId = id;
        return id;
    }

}
