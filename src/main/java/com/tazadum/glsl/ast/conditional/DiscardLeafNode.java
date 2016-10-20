package com.tazadum.glsl.ast.conditional;

import com.tazadum.glsl.ast.ASTVisitor;
import com.tazadum.glsl.ast.LeafNode;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.language.GLSLType;

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

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitDiscard(this);
    }

    @Override
    public GLSLType getType() {
        return null;
    }
}
