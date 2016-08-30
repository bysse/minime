package com.tazadum.glsl.ast.function;

import com.tazadum.glsl.ast.Identifier;
import com.tazadum.glsl.ast.ParentNode;

public class FunctionDeclarationNode extends ParentNode {
    private Identifier identifier;

    public Identifier getIdentifier() {
        return identifier;
    }
}
