package com.tazadum.glsl.ast.variable;

import com.tazadum.glsl.ast.ParentNode;
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
