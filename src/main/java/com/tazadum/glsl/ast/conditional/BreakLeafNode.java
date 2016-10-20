package com.tazadum.glsl.ast.conditional;

import com.tazadum.glsl.ast.ASTVisitor;
import com.tazadum.glsl.ast.LeafNode;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.language.GLSLType;

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

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitBreak(this);
    }

    @Override
    public GLSLType getType() {
        return null;
    }
}
