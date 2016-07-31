package com.tazadum.glsl.ast;

public class LeafNode implements Node {
    private ParentNode parentNode;

    public LeafNode() {
        this(null);
    }

    public LeafNode(ParentNode parentNode) {
        this.parentNode = parentNode;
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
    public LeafNode clone(ParentNode newParent) {
        return new LeafNode(newParent);
    }
}
