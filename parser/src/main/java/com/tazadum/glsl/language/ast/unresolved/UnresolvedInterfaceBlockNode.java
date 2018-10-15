package com.tazadum.glsl.language.ast.unresolved;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.traits.UnresolvedNode;
import com.tazadum.glsl.language.ast.type.ArraySpecifierListNode;
import com.tazadum.glsl.language.ast.type.TypeQualifierListNode;
import com.tazadum.glsl.util.SourcePosition;

public class UnresolvedInterfaceBlockNode extends FixedChildParentNode implements UnresolvedNode {
    private String identifier;

    public UnresolvedInterfaceBlockNode(SourcePosition position, TypeQualifierListNode qualifiers, UnresolvedStructDeclarationNode structDeclaration, String identifier, ArraySpecifierListNode arraySpecifier) {
        this(position, null, qualifiers, structDeclaration, identifier, arraySpecifier);
    }

    public UnresolvedInterfaceBlockNode(SourcePosition position, ParentNode parentNode, TypeQualifierListNode qualifiers, UnresolvedStructDeclarationNode structDeclaration, String identifier, ArraySpecifierListNode arraySpecifier) {
        super(position, 3, parentNode);
        this.identifier = identifier;

        setChild(0, qualifiers);
        setChild(1, structDeclaration);
        setChild(2, arraySpecifier);
    }

    /**
     * Returns the interface block instance name or null.
     */
    public String getIdentifier() {
        return identifier;
    }

    public TypeQualifierListNode getTypeQualifier() {
        return getChildAs(0);
    }

    public UnresolvedStructDeclarationNode getInterfaceStruct() {
        return getChildAs(1);
    }

    public ArraySpecifierListNode getArraySpecifier() {
        return getChildAs(2);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitUnresolvedInterfaceBlockNode(this);
    }
}
