package com.tazadum.glsl.language.ast.conditional;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.LeafNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.type.GLSLType;

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
