package com.tazadum.glsl.language.ast.unresolved;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.traits.UnresolvedNode;
import com.tazadum.glsl.language.type.TypeQualifierList;
import com.tazadum.glsl.util.SourcePosition;

public class UnresolvedTypeNode extends FixedChildParentNode implements UnresolvedNode {
    private TypeQualifierList qualifiers;

    public UnresolvedTypeNode(SourcePosition position, TypeQualifierList qualifiers, UnresolvedTypeSpecifierNode typeSpecifier) {
        this(position, null, qualifiers, typeSpecifier);
    }

    public UnresolvedTypeNode(SourcePosition position, ParentNode parentNode, TypeQualifierList qualifiers, UnresolvedTypeSpecifierNode typeSpecifier) {
        super(position, 1, parentNode);
        this.qualifiers = qualifiers;
        setChild(0, typeSpecifier);
    }

    public TypeQualifierList getQualifiers() {
        return qualifiers;
    }

    public UnresolvedTypeSpecifierNode getTypeSpecifier() {
        return getChildAs(0);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitTypeNode(this);
    }
}
