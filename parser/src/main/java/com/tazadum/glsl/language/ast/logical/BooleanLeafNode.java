package com.tazadum.glsl.language.ast.logical;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.LeafNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.PredefinedType;
import com.tazadum.glsl.util.SourcePositionId;

public class BooleanLeafNode extends LeafNode {
    private final boolean value;

    public BooleanLeafNode(SourcePositionId position, boolean value) {
        this(position, null, value);
    }

    public BooleanLeafNode(SourcePositionId position, ParentNode parentNode, boolean value) {
        super(position, parentNode);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public LeafNode clone(ParentNode newParent) {
        return new BooleanLeafNode(getSourcePositionId(), newParent, value);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitBoolean(this);
    }

    @Override
    public GLSLType getType() {
        return PredefinedType.BOOL;
    }
}
