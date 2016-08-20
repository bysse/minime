package com.tazadum.glsl.arithmetic;

import com.tazadum.glsl.ast.LeafNode;
import com.tazadum.glsl.ast.ParentNode;

public class FloatLeafNode extends LeafNode {
    private final Numeric value;

    public FloatLeafNode(Numeric value) {
        this(null, value);
    }

    public FloatLeafNode(ParentNode parentNode, Numeric value) {
        super(parentNode);
        this.value = value;
    }

    public Numeric getValue() {
        return value;
    }

    @Override
    public LeafNode clone(ParentNode newParent) {
        return new FloatLeafNode(newParent, value);
    }
}
