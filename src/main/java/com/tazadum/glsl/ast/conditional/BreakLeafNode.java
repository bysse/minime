package com.tazadum.glsl.ast.conditional;

import com.tazadum.glsl.ast.LeafNode;
import com.tazadum.glsl.ast.ParentNode;

public class BreakLeafNode extends LeafNode {

    public BreakLeafNode() {
        this(null);
    }

    public BreakLeafNode(ParentNode parentNode) {
        super(parentNode);
    }

    @Override
    public LeafNode clone(ParentNode newParent) {
        return new BreakLeafNode(newParent);
    }
}
