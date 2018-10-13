package com.tazadum.glsl.language.ast.type;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.LeafNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.TypeQualifierList;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Node for holding a fully specified type in the AST.
 */
public class TypeNode extends LeafNode {
    private GLSLType baseType;
    private TypeQualifierList qualifiers;
    private ArraySpecifierListNode arraySpecifier;

    public TypeNode(SourcePosition position, GLSLType baseType, TypeQualifierList qualifiers, ArraySpecifierListNode arraySpecifier) {
        this(position, null, baseType, qualifiers, arraySpecifier);
    }

    public TypeNode(SourcePosition position, ParentNode parentNode, GLSLType baseType, TypeQualifierList qualifiers, ArraySpecifierListNode arraySpecifier) {
        super(position, parentNode);
        this.baseType = baseType;
        this.qualifiers = qualifiers;
        this.arraySpecifier = arraySpecifier;
    }

    public GLSLType getBaseType() {
        return baseType;
    }

    public TypeQualifierList getQualifiers() {
        return qualifiers;
    }

    public ArraySpecifierListNode getArraySpecifier() {
        return arraySpecifier;
    }

    public void setArraySpecifier(ArraySpecifierListNode arraySpecifier) {
        this.arraySpecifier = arraySpecifier;
    }

    @Override
    public LeafNode clone(ParentNode newParent) {
        return new TypeNode(getSourcePosition(), newParent, baseType, qualifiers, arraySpecifier);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitTypeNode(this);
    }

    @Override
    public GLSLType getType() {
        return baseType;
    }
}
