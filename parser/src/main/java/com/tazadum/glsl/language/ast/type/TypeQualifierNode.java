package com.tazadum.glsl.language.ast.type;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.LeafNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.TypeQualifier;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Node for holding a single TypeQualifier
 */
public class TypeQualifierNode extends LeafNode {
    private TypeQualifier qualifier;

    public TypeQualifierNode(SourcePosition position, TypeQualifier qualifier) {
        this(position, null, qualifier);
    }

    public TypeQualifierNode(SourcePosition position, ParentNode parentNode, TypeQualifier qualifier) {
        super(position, parentNode);
        this.qualifier = qualifier;
    }

    public TypeQualifier getQualifier() {
        return qualifier;
    }

    @Override
    public LeafNode clone(ParentNode newParent) {
        return new TypeQualifierNode(getSourcePosition(), newParent, qualifier);
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
