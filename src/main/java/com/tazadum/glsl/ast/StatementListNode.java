package com.tazadum.glsl.ast;

import com.tazadum.glsl.util.CloneUtils;

public class StatementListNode extends ParentNode {
    public StatementListNode() {
    }

    @Override
    public StatementListNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new StatementListNode());
    }
}
