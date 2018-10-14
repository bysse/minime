package com.tazadum.glsl.language.ast.type;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.TypeQualifier;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Node for holding a single TypeQualifier
 */
public class TypeQualifierListNode extends ParentNode {
    public TypeQualifierListNode(SourcePosition position) {
        this(position, null);
    }

    public TypeQualifierListNode(SourcePosition position, ParentNode parentNode) {
        super(position, parentNode);
    }

    public TypeQualifier getQualifier(int index) {
        return getChildAs(index);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        TypeQualifierListNode node = new TypeQualifierListNode(getSourcePosition(), newParent);
        return CloneUtils.cloneChildren(this, node);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitTypeQualifierListNode(this);
    }

    @Override
    public GLSLType getType() {
        return null;
    }
}
