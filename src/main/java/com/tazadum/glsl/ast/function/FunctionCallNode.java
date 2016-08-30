package com.tazadum.glsl.ast.function;

import com.tazadum.glsl.ast.ParentNode;

public class FunctionCallNode extends ParentNode {
    private FunctionDeclarationNode declarationNode;

    public FunctionCallNode(FunctionDeclarationNode functionDeclarationNode) {
        this.declarationNode = functionDeclarationNode;
    }

    public FunctionDeclarationNode getDeclarationNode() {
        return declarationNode;
    }
}
