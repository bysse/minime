package com.tazadum.glsl.ast.variable;

import com.tazadum.glsl.ast.LeafNode;
import com.tazadum.glsl.ast.ParentNode;

public class VariableNode extends LeafNode {
    /**
     * The VariableDeclarationNode is not a child in the AST.
     * It's only a reference to the declaration of the variable.
     */
    private VariableDeclarationNode declarationNode;

    public VariableNode(VariableDeclarationNode declarationNode) {
        this.declarationNode = declarationNode;
    }

    public VariableDeclarationNode getDeclarationNode() {
        return declarationNode;
    }

    @Override
    public LeafNode clone(ParentNode newParent) {
        // TODO: we might need to resolve the declarationNode
        return new VariableNode(declarationNode);
    }
}
