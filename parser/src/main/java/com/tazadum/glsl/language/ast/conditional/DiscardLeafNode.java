package com.tazadum.glsl.language.ast.conditional;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.LeafNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.type.GLSLType;

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
