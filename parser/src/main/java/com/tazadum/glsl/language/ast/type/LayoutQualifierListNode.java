package com.tazadum.glsl.language.ast.type;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.TypeQualifier;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Node for holding a single LayoutQualidier
 */
public class LayoutQualifierListNode extends ParentNode {
    public LayoutQualifierListNode(SourcePosition position) {
        this(position, null);
    }

    public LayoutQualifierListNode(SourcePosition position, ParentNode parentNode) {
        super(position, parentNode);
    }

    public TypeQualifier getQualifier(int index) {
        return getChildAs(index);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        LayoutQualifierListNode node = new LayoutQualifierListNode(getSourcePosition(), newParent);
        return CloneUtils.cloneChildren(this, node);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitLayoutQualifierListNode(this);
    }

    @Override
    public GLSLType getType() {
        return null;
    }
}
