package com.tazadum.glsl.language.ast.type;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.PredefinedType;
import com.tazadum.glsl.util.SourcePosition;

public class ArraySpecifierListNode extends ParentNode {
    public ArraySpecifierListNode(SourcePosition position) {
        this(position, null);
    }

    public ArraySpecifierListNode(SourcePosition position, ParentNode parentNode) {
        super(position, parentNode);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        ArraySpecifierListNode node = new ArraySpecifierListNode(getSourcePosition(), null);
        return CloneUtils.cloneChildren(this, node);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitArrayTypeListNode(this);
    }

    @Override
    public GLSLType getType() {
        return PredefinedType.UINT;
    }
}
