package com.tazadum.glsl.language.ast.struct;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.LeafNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.type.FullySpecifiedType;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Indicates that a type will be specified further down the source listing.
 */
public class TypeDeclarationNode extends LeafNode {
    private FullySpecifiedType type;

    public TypeDeclarationNode(SourcePosition position, FullySpecifiedType type) {
        super(position);
        this.type = type;

        assert type != null : "Type is null";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypeDeclarationNode that = (TypeDeclarationNode) o;

        return type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    @Override
    public LeafNode clone(ParentNode newParent) {
        return new TypeDeclarationNode(getSourcePosition(), type);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitTypeDeclaration(this);
    }

    @Override
    public GLSLType getType() {
        return type.getType();
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
