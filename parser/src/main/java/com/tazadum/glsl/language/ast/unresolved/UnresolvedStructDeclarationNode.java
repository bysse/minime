package com.tazadum.glsl.language.ast.unresolved;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.traits.UnresolvedNode;
import com.tazadum.glsl.util.SourcePosition;

public class UnresolvedStructDeclarationNode extends ParentNode implements UnresolvedNode {
    private String identifier;

    public UnresolvedStructDeclarationNode(SourcePosition position, String identifier) {
        this(position, null, identifier);
    }

    public UnresolvedStructDeclarationNode(SourcePosition position, ParentNode parentNode, String identifier) {
        super(position, parentNode);
        this.identifier = identifier;
    }

    /**
     * Returns the struct identifier or null if it's an anonymous struct.
     */
    public String getIdentifier() {
        return identifier;
    }

    public void addFieldDeclaration(UnresolvedStructFieldDeclarationNode fieldList) {
        addChild(fieldList);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitUnresolvedStructDeclarationNode(this);
    }
}
