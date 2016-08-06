package com.tazadum.glsl.ast;

import com.tazadum.glsl.parser.type.FullySpecifiedType;

public class VariableDeclarationListNode extends ParentNode {
    private FullySpecifiedType fst;

    public void setFullySpecifiedType(FullySpecifiedType fst) {
        this.fst = fst;
    }

    public FullySpecifiedType getFullySpecifiedType() {
        return fst;
    }
}
