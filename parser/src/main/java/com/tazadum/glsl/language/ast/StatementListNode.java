package com.tazadum.glsl.language.ast;


import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.util.SourcePosition;

import java.util.Objects;

public class StatementListNode extends ParentNode {
    public StatementListNode(SourcePosition position) {
        super(position);
    }

    @Override
    public StatementListNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new StatementListNode(getSourcePosition()));
    }

    public StatementListNode insertChild(int index, Node node) {
        super.insertChild(index, node);
        return this;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitStatementList(this);
    }

    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < getChildCount(); i++) {
            if (i > 0) {
                builder.append(";\n");
            }
            builder.append(Objects.toString(getChild(i)));
        }
        return builder.toString();
    }
}
