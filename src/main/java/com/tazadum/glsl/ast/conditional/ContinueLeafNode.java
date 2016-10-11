package com.tazadum.glsl.ast.conditional;

import com.tazadum.glsl.ast.LeafNode;
import com.tazadum.glsl.ast.ParentNode;

public class ContinueLeafNode extends LeafNode {

    public ContinueLeafNode() {
        this(null);
    }

    public ContinueLeafNode(ParentNode parentNode) {
        super(parentNode);
    }

    @Override
    public LeafNode clone(ParentNode newParent) {
        return new ContinueLeafNode(newParent);
    }
}
