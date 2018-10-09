package com.tazadum.glsl.language.ast.conditional;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.LeafNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.type.GLSLType;

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

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitContinue(this);
    }

    @Override
    public GLSLType getType() {
        return null;
    }
}
