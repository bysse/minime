package com.tazadum.glsl.language.ast.unresolved;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.traits.UnresolvedNode;
import com.tazadum.glsl.language.ast.type.TypeQualifierListNode;
import com.tazadum.glsl.util.SourcePosition;

public class UnresolvedTypeNode extends FixedChildParentNode implements UnresolvedNode {
    public UnresolvedTypeNode(SourcePosition position, TypeQualifierListNode qualifiers, UnresolvedTypeSpecifierNode typeSpecifier) {
        this(position, null, qualifiers, typeSpecifier);
    }

    public UnresolvedTypeNode(SourcePosition position, ParentNode parentNode, TypeQualifierListNode qualifiers, UnresolvedTypeSpecifierNode typeSpecifier) {
        super(position, 2, parentNode);
        setChild(0, qualifiers);
        setChild(1, typeSpecifier);
    }

    public TypeQualifierListNode getQualifiers() {
        return getChildAs(0);
    }

    public UnresolvedTypeSpecifierNode getTypeSpecifier() {
        return getChildAs(1);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitTypeNode(this);
    }
}
