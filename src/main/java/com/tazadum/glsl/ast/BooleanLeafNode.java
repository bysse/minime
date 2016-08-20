package com.tazadum.glsl.ast;

public class BooleanLeafNode extends LeafNode {
    private final boolean value;

    public BooleanLeafNode(boolean value) {
        this(null, value);
    }

    public BooleanLeafNode(ParentNode parentNode, boolean value) {
        super(parentNode);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public LeafNode clone(ParentNode newParent) {
        return new BooleanLeafNode(newParent, value);
    }
}
