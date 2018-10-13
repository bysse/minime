package com.tazadum.glsl.language.ast.type;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.LeafNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.TypeQualifierList;
import com.tazadum.glsl.util.SourcePosition;

import java.util.List;

/**
 * Node for holding a fully specified type in the AST.
 */
public class TypeQualifierNode extends LeafNode {
    private TypeQualifierList qualifiers;
    private List<String> identifiers;

    public TypeQualifierNode(SourcePosition position, TypeQualifierList qualifiers, List<String> identifiers) {
        this(position, null, qualifiers, identifiers);
    }

    public TypeQualifierNode(SourcePosition position, ParentNode parentNode, TypeQualifierList qualifiers, List<String> identifiers) {
        super(position, parentNode);
        this.qualifiers = qualifiers;
        this.identifiers = identifiers;
    }

    public TypeQualifierList getQualifiers() {
        return qualifiers;
    }

    public List<String> getIdentifiers() {
        return identifiers;
    }

    @Override
    public LeafNode clone(ParentNode newParent) {
        return new TypeQualifierNode(getSourcePosition(), newParent, qualifiers, identifiers);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitTypeQualifierNode(this);
    }

    @Override
    public GLSLType getType() {
        return null;
    }
}
