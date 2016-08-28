package com.tazadum.glsl.ast.variable;

import com.tazadum.glsl.ast.LeafNode;

public class VariableNode extends LeafNode {
    private VariableDeclarationNode declarationNode;

    public VariableNode(VariableDeclarationNode declarationNode) {
        this.declarationNode = declarationNode;
    }

    public VariableDeclarationNode getDeclarationNode() {
        return declarationNode;
    }
}
