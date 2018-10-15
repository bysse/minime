package com.tazadum.glsl.language.ast.unresolved;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.util.SourcePosition;

public class UnresolvedStructFieldListNode extends ParentNode {
    public UnresolvedStructFieldListNode(SourcePosition position) {
        this(position, null);
    }

    public UnresolvedStructFieldListNode(SourcePosition position, ParentNode parentNode) {
        super(position, parentNode);
    }

    public void addFieldNode(UnresolvedStructFieldNode node) {
        addChild(node);
    }

    public UnresolvedStructFieldNode getFieldNode(int index) {
        return getChildAs(index);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitUnresolvedStructFieldListNode(this);
    }
}
