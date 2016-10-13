package com.tazadum.glsl.ast.logical;

import com.tazadum.glsl.ast.ASTVisitor;
import com.tazadum.glsl.ast.LeafNode;
import com.tazadum.glsl.ast.ParentNode;

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

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitBoolean(this);
    }
}
