package com.tazadum.glsl.ast.function;

import com.tazadum.glsl.ast.Identifier;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.parser.type.FullySpecifiedType;

public class FunctionPrototypeNode extends ParentNode {
    private Identifier identifier;
    private FullySpecifiedType returnType;

    public FunctionPrototypeNode(String functionName, FullySpecifiedType returnType) {
        this.identifier = new Identifier(functionName);
        this.returnType = returnType;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public FullySpecifiedType getReturnType() {
        return returnType;
    }
}
