package com.tazadum.glsl.ast;

import com.tazadum.glsl.parser.type.FullySpecifiedType;

public class VariableDeclarationListNode extends ParentNode {
    private FullySpecifiedType type;

    public void setType(FullySpecifiedType type) {
        this.type = type;
    }

    public FullySpecifiedType getType() {
        return type;
    }
}
