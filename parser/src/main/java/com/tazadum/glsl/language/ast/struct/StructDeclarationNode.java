package com.tazadum.glsl.language.ast.struct;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.traits.UnresolvedNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationListNode;
import com.tazadum.glsl.util.SourcePosition;

public class StructDeclarationNode extends ParentNode implements UnresolvedNode {
    private String identifier;

    public StructDeclarationNode(SourcePosition position, String identifier) {
        this(position, null, identifier);
    }

    public StructDeclarationNode(SourcePosition position, ParentNode parentNode, String identifier) {
        super(position, parentNode);
        this.identifier = identifier;
    }

    /**
     * Returns the struct identifier or null if it's an anonymous struct.
     */
    public String getIdentifier() {
        return identifier;
    }

    public void addFieldDeclaration(VariableDeclarationListNode fieldList) {
        addChild(fieldList);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitStructDeclarationNode(this);
    }
}
