package com.tazadum.glsl.language.ast.variable;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.traits.HasSharedState;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.FullySpecifiedType;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

import java.util.Objects;

public class VariableDeclarationListNode extends ParentNode implements HasSharedState {
    private FullySpecifiedType type;
    private boolean shared;

    public VariableDeclarationListNode(SourcePosition position, FullySpecifiedType type) {
        super(position);
        this.type = type;
    }

    public VariableDeclarationListNode(SourcePosition position, ParentNode parentNode, FullySpecifiedType type) {
        super(position, parentNode);
        this.type = type;
    }

    public void setFullySpecifiedType(FullySpecifiedType fst) {
        this.type = fst;
    }

    public FullySpecifiedType getFullySpecifiedType() {
        return type;
    }

    @Override
    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new VariableDeclarationListNode(getSourcePosition(), newParent, type));
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitVariableDeclarationList(this);
    }

    @Override
    public GLSLType getType() {
        return type.getType();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < getChildCount(); i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(Objects.toString(getChild(i)));
        }
        return getId() + ": " + type + " " + builder;
    }
}
