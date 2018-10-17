package com.tazadum.glsl.language.ast.type;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.LeafNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.TypeQualifierList;
import com.tazadum.glsl.util.SourcePosition;

import java.util.List;

/**
 * Node for holding a single TypeQualifier declaration.
 */
public class TypeQualifierDeclarationNode extends LeafNode {
    private final TypeQualifierList qualifiers;
    private final List<String> identifiers;

    public TypeQualifierDeclarationNode(SourcePosition position, TypeQualifierList qualifiers, List<String> identifiers) {
        this(position, null, qualifiers, identifiers);
    }

    public TypeQualifierDeclarationNode(SourcePosition position, ParentNode parentNode, TypeQualifierList qualifiers, List<String> identifiers) {
        super(position, parentNode);
        this.qualifiers = qualifiers;
        this.identifiers = identifiers;
    }

    public TypeQualifierList getQualifiers() {
        return qualifiers;
    }

    @Override
    public TypeQualifierDeclarationNode clone(ParentNode newParent) {
        return new TypeQualifierDeclarationNode(getSourcePosition(), newParent, null, identifiers);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitTypeQualifierDeclarationNode(this);
    }

    @Override
    public GLSLType getType() {
        return null;
    }
}
