package com.tazadum.glsl.language.ast.type;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.LeafNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.type.FullySpecifiedType;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Node for holding a fully specified type in the AST.
 */
public class TypeNode extends LeafNode {
    private final FullySpecifiedType fullySpecifiedType;

    public TypeNode(SourcePosition position, FullySpecifiedType fullySpecifiedType) {
        this(position, null, fullySpecifiedType);
    }

    public TypeNode(SourcePosition position, ParentNode parentNode, FullySpecifiedType fullySpecifiedType) {
        super(position, parentNode);
        this.fullySpecifiedType = fullySpecifiedType;
    }

    public FullySpecifiedType getFullySpecifiedType() {
        return fullySpecifiedType;
    }

    @Override
    public LeafNode clone(ParentNode newParent) {
        return new TypeNode(getSourcePosition(), newParent, fullySpecifiedType);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitTypeNode(this);
    }

    @Override
    public GLSLType getType() {
        return fullySpecifiedType.getType();
    }
}
