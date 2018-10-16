package com.tazadum.glsl.language.ast.unresolved;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.type.TypeQualifierListNode;
import com.tazadum.glsl.util.SourcePosition;

public class UnresolvedStructFieldDeclarationNode extends FixedChildParentNode {
    public UnresolvedStructFieldDeclarationNode(SourcePosition position, TypeQualifierListNode qualifiers, UnresolvedTypeSpecifierNode typeSpecifier) {
        this(position, null, qualifiers, typeSpecifier);
    }

    public UnresolvedStructFieldDeclarationNode(SourcePosition position, ParentNode parentNode, TypeQualifierListNode qualifiers, UnresolvedTypeSpecifierNode typeSpecifier) {
        super(position, 3, parentNode);

        setChild(0, qualifiers);
        setChild(1, typeSpecifier);
    }

    public TypeQualifierListNode getTypeQualifiers() {
        return getChildAs(0);
    }

    public UnresolvedTypeSpecifierNode getTypeSpecifier() {
        return getChildAs(1);
    }

    public void setFieldList(UnresolvedStructFieldListNode node) {
        setChild(2, node);
    }

    public UnresolvedStructFieldListNode getFieldList() {
        return getChildAs(2);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitUnresolvedStructFieldDeclarationNode(this);
    }
}
