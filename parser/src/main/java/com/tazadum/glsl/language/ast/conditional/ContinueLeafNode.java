package com.tazadum.glsl.language.ast.conditional;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.LeafNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

public class ContinueLeafNode extends LeafNode {

    public ContinueLeafNode(SourcePosition position) {
        this(position, null);
    }

    public ContinueLeafNode(SourcePosition position, ParentNode parentNode) {
        super(position, parentNode);
    }

    @Override
    public LeafNode clone(ParentNode newParent) {
        return new ContinueLeafNode(getSourcePosition(), newParent);
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
