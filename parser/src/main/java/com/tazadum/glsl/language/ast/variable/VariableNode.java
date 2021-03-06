package com.tazadum.glsl.language.ast.variable;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.LeafNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.traits.HasDeclarationReference;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

import java.util.Objects;

public class VariableNode extends LeafNode implements HasDeclarationReference<VariableDeclarationNode> {
    /**
     * The VariableDeclarationNode is not a child in the AST.
     * It's only a reference to the declaration of the variable.
     */
    private VariableDeclarationNode declarationNode;

    public VariableNode(SourcePosition position, VariableDeclarationNode declarationNode) {
        super(position);
        this.declarationNode = declarationNode;
    }

    @Override
    public VariableDeclarationNode getDeclarationNode() {
        return declarationNode;
    }

    @Override
    public void setDeclarationNode(VariableDeclarationNode declarationNode) {
        this.declarationNode = declarationNode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VariableNode that = (VariableNode) o;

        return Objects.equals(declarationNode, that.declarationNode);
    }

    @Override
    public int hashCode() {
        return declarationNode != null ? declarationNode.hashCode() : 0;
    }

    @Override
    public LeafNode clone(ParentNode newParent) {
        // Note that the declaration node is not resolved at this time
        return new VariableNode(getSourcePosition(), this.declarationNode);
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
            return "'unresolved variable'";
        }
        return declarationNode.getIdentifier().original();
    }
}
