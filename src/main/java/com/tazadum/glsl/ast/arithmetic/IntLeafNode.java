package com.tazadum.glsl.ast.arithmetic;

import com.tazadum.glsl.ast.LeafNode;
import com.tazadum.glsl.ast.ParentNode;

public class IntLeafNode extends LeafNode {
    private final Numeric value;

    public IntLeafNode(Numeric value) {
        this(null, value);
    }

    public IntLeafNode(ParentNode parentNode, Numeric value) {
        super(parentNode);
        this.value = value;
    }

    public Numeric getValue() {
        return value;
    }

    @Override
    public LeafNode clone(ParentNode newParent) {
        return new IntLeafNode(newParent, value);
    }
}
