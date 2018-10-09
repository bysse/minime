package com.tazadum.glsl.language.ast;


import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.util.SourcePositionId;

public class StatementListNode extends ParentNode {
    public StatementListNode(SourcePositionId position) {
        super(position);
    }

    @Override
    public StatementListNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new StatementListNode(getSourcePositionId()));
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitStatementList(this);
    }
}
