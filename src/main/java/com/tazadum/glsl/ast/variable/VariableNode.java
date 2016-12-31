package com.tazadum.glsl.ast.variable;

import com.tazadum.glsl.ast.ASTVisitor;
import com.tazadum.glsl.ast.LeafNode;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.language.GLSLType;
import com.tazadum.glsl.util.CloneUtils;

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

    public void setDeclarationNode(VariableDeclarationNode declarationNode) {
        this.declarationNode = declarationNode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VariableNode that = (VariableNode) o;

        return declarationNode != null ? declarationNode.equals(that.declarationNode) : that.declarationNode == null;
    }

    @Override
    public int hashCode() {
        return declarationNode != null ? declarationNode.hashCode() : 0;
    }

    @Override
    public LeafNode clone(ParentNode newParent) {
        // we need to resolve the declarationNode
        final Node root = CloneUtils.getRoot(newParent);
        final VariableDeclarationNode remap = CloneUtils.remap(root, this.declarationNode);

        return new VariableNode(remap);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitVariable(this);
    }

    @Override
    public GLSLType getType() {
        return declarationNode.getType();
    }

    @Override
    public String toString() {
        if (declarationNode == null) {
            return "Variable('unresolved')";
        }
        return "Variable('" + declarationNode.getIdentifier() + "')";
    }
}
