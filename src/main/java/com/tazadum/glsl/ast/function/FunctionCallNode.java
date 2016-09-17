package com.tazadum.glsl.ast.function;

import com.tazadum.glsl.ast.Identifier;
import com.tazadum.glsl.ast.ParentNode;

public class FunctionCallNode extends ParentNode {
    private Identifier identifier;
    private FunctionPrototypeNode declarationNode;

    public FunctionCallNode(String functionName) {
        this.identifier = new Identifier(functionName);
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public void setDeclarationNode(FunctionPrototypeNode declarationNode) {
        this.declarationNode = declarationNode;
    }

    public FunctionPrototypeNode getDeclarationNode() {
        return declarationNode;
    }

    @Override
    public String toString() {
        return identifier.toString();
    }
}
