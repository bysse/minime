package com.tazadum.glsl.language.ast.unresolved;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.traits.HasSharedState;
import com.tazadum.glsl.util.SourcePosition;

public class UnresolvedVariableDeclarationListNode extends ParentNode implements HasSharedState {
    private boolean shared;

    public UnresolvedVariableDeclarationListNode(SourcePosition position) {
        super(position);
    }

    public UnresolvedVariableDeclarationListNode(SourcePosition position, ParentNode parentNode) {
        super(position, parentNode);
    }

    public void addVariableDeclaration(UnresolvedVariableDeclarationNode node) {
        addChild(node);
    }

    public UnresolvedVariableDeclarationNode getVariableDeclaration(int index) {
        return getChildAs(index);
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
        return visitor.visitVariableDeclarationList(this);
    }
}
