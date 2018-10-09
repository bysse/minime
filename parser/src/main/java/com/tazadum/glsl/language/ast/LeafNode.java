package com.tazadum.glsl.language.ast;

import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

/**
 * @author erikb
 * @since 2016-07-31
 */
public class LeafNode implements Node {
    private SourcePosition position;
    private ParentNode parentNode;
    private int cachedId = -1;

    public LeafNode(SourcePosition position) {
        this(position, null);
    }

    public LeafNode(SourcePosition position, ParentNode parentNode) {
        this.position = position;
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

    @Override
    public SourcePosition getSourcePosition() {
        return position;
    }
}
