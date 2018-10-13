package com.tazadum.glsl.language.ast.unresolved;

import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.struct.StructDeclarationNode;
import com.tazadum.glsl.language.ast.traits.UnresolvedNode;
import com.tazadum.glsl.language.ast.type.ArraySpecifierListNode;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Created by erikb on 2018-10-13.
 */
public class UnresolvedTypeSpecifierNode extends FixedChildParentNode implements UnresolvedNode {
    private String typeOrIdentifier;

    public UnresolvedTypeSpecifierNode(SourcePosition position, String typeOrIdentifier, StructDeclarationNode structDeclaration, ArraySpecifierListNode arraySpecifier) {
        this(position, null, typeOrIdentifier, structDeclaration, arraySpecifier);
    }

    public UnresolvedTypeSpecifierNode(SourcePosition position, ParentNode parentNode, String typeOrIdentifier, StructDeclarationNode structDeclaration, ArraySpecifierListNode arraySpecifier) {
        super(position, 2, parentNode);
        this.typeOrIdentifier = typeOrIdentifier;

        setChild(0, structDeclaration);
        setChild(1, arraySpecifier);
    }

    public String getTypeOrIdentifier() {
        return typeOrIdentifier;
    }

    public StructDeclarationNode getStructDeclaration() {
        return getChildAs(0);
    }

    public ArraySpecifierListNode getArraySpecifier() {
        return getChildAs(1);
    }
}
