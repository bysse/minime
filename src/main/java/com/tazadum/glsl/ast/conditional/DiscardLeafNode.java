package com.tazadum.glsl.ast.conditional;

import com.tazadum.glsl.ast.LeafNode;
import com.tazadum.glsl.ast.ParentNode;

public class DiscardLeafNode extends LeafNode {

    public DiscardLeafNode() {
        this(null);
    }

    public DiscardLeafNode(ParentNode parentNode) {
        super(parentNode);
    }

    @Override
    public LeafNode clone(ParentNode newParent) {
        return new DiscardLeafNode(newParent);
    }
}
