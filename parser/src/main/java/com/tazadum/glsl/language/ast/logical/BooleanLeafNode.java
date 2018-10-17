package com.tazadum.glsl.language.ast.logical;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.LeafNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.traits.HasConstState;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.PredefinedType;
import com.tazadum.glsl.util.SourcePosition;

public class BooleanLeafNode extends LeafNode implements HasConstState {
    private final boolean value;

    public BooleanLeafNode(SourcePosition position, boolean value) {
        this(position, null, value);
    }

    public BooleanLeafNode(SourcePosition position, ParentNode parentNode, boolean value) {
        super(position, parentNode);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public LeafNode clone(ParentNode newParent) {
        return new BooleanLeafNode(getSourcePosition(), newParent, value);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitBoolean(this);
    }

    @Override
    public GLSLType getType() {
        return PredefinedType.BOOL;
    }

    @Override
    public boolean isConstant() {
        return true;
    }
}
