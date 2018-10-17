package com.tazadum.glsl.language.ast.type;

import com.tazadum.glsl.exception.BadImplementationException;
import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.LeafNode;
import com.tazadum.glsl.language.type.TypeQualifier;
import com.tazadum.glsl.language.type.TypeQualifierList;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Node for holding a single TypeQualifier
 */
public class TypeQualifierListNode extends LeafNode {
    private TypeQualifierList typeQualifiers = new TypeQualifierList();

    public TypeQualifierListNode(SourcePosition position) {
        super(position);
    }

    public void addTypeQualifier(TypeQualifier typeQualifier) {
        typeQualifiers.add(typeQualifier);
    }

    public TypeQualifierList getTypeQualifiers() {
        return typeQualifiers;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        throw new BadImplementationException("Node should not be in the final AST");
    }
}
