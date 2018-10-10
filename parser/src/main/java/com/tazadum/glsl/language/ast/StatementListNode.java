package com.tazadum.glsl.language.ast;


import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.util.SourcePosition;

public class StatementListNode extends ParentNode {
    public StatementListNode(SourcePosition position) {
        super(position);
    }

    @Override
    public StatementListNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new StatementListNode(getSourcePosition()));
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitStatementList(this);
    }
}
