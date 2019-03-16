package com.tazadum.glsl.language.ast.struct;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.traits.UnresolvedNode;
import com.tazadum.glsl.language.model.ArraySpecifiers;
import com.tazadum.glsl.language.type.TypeQualifierList;
import com.tazadum.glsl.util.SourcePosition;

public class InterfaceBlockNode extends FixedChildParentNode implements UnresolvedNode {
    private final TypeQualifierList qualifiers;
    private final Identifier blockIdentifier;
    private final Identifier identifier;
    private final ArraySpecifiers arraySpecifiers;

    public InterfaceBlockNode(SourcePosition position, TypeQualifierList qualifiers, StructDeclarationNode structDeclaration, String blockIdentifier, String identifier, ArraySpecifiers arraySpecifiers) {
        this(position, null, qualifiers, structDeclaration, blockIdentifier, identifier, arraySpecifiers);
    }

    public InterfaceBlockNode(SourcePosition position, ParentNode parentNode, TypeQualifierList qualifiers, StructDeclarationNode structDeclaration, String blockIdentifier, String identifier, ArraySpecifiers arraySpecifiers) {
        super(position, 1, parentNode);
        this.qualifiers = qualifiers;
        this.blockIdentifier = Identifier.orNull(blockIdentifier);
        this.identifier = Identifier.orNull(identifier);
        this.arraySpecifiers = arraySpecifiers;

        setChild(0, structDeclaration);
    }

    /**
     * Returns the external interface block name or null.
     */
    public Identifier getBlockIdentifier() {
        return blockIdentifier;
    }

    /**
     * Returns the interface block instance name or null.
     */
    public Identifier getIdentifier() {
        return identifier;
    }

    public TypeQualifierList getTypeQualifier() {
        return qualifiers;
    }

    public ArraySpecifiers getArraySpecifier() {
        return arraySpecifiers;
    }

    public StructDeclarationNode getInterfaceStruct() {
        return getChildAs(0);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitInterfaceBlockNode(this);
    }
}
