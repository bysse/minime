package com.tazadum.glsl.language.ast.unresolved;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.traits.HasSharedState;
import com.tazadum.glsl.language.ast.type.ArraySpecifierListNode;
import com.tazadum.glsl.util.SourcePosition;

public class UnresolvedVariableDeclarationNode extends FixedChildParentNode implements HasSharedState {
    protected String identifier;
    protected boolean shared;

    public UnresolvedVariableDeclarationNode(SourcePosition position, UnresolvedTypeNode typeNode, String identifier, ArraySpecifierListNode arraySpecifier, Node initializer) {
        this(position, null, typeNode, identifier, arraySpecifier, initializer);
    }

    public UnresolvedVariableDeclarationNode(SourcePosition position, ParentNode newParent, UnresolvedTypeNode typeNode, String identifier, ArraySpecifierListNode arraySpecifier, Node initializer) {
        super(position, 3, newParent);
        this.identifier = identifier;

        setChild(0, typeNode);
        setChild(1, arraySpecifier);
        setChild(2, initializer);
    }

    public String getIdentifier() {
        return identifier;
    }

    public UnresolvedTypeNode getTypeNode() {
        return getChildAs(0);
    }

    public ArraySpecifierListNode getArraySpecifier() {
        return getChildAs(1);
    }

    public Node getInitializer() {
        return getChild(2);
    }

    @Override
    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitVariableDeclaration(this);
    }
}
