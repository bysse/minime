package com.tazadum.glsl.language.ast.unresolved;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.traits.UnresolvedNode;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Indicates that a type will be specified further down the source listing.
 */
public class UnresolvedTypeDeclarationNode extends FixedChildParentNode implements UnresolvedNode {
    public UnresolvedTypeDeclarationNode(SourcePosition position, UnresolvedTypeNode typeNode) {
        super(position, 1);
        setChild(0, typeNode);
    }

    public UnresolvedTypeNode getTypeNode() {
        return getChildAs(0);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitTypeDeclaration(this);
    }
}
