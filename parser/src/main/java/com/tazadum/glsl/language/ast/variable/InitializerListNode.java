package com.tazadum.glsl.language.ast.variable;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.util.SourcePosition;

public class InitializerListNode extends ParentNode {
    public InitializerListNode(SourcePosition position) {
        super(position);
    }

    public InitializerListNode(SourcePosition position, ParentNode parentNode) {
        super(position, parentNode);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitInitializerList(this);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new InitializerListNode(getSourcePosition(), newParent));
    }
}
