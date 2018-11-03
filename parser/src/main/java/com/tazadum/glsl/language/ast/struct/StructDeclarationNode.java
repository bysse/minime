package com.tazadum.glsl.language.ast.struct;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.traits.UnresolvedNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationListNode;
import com.tazadum.glsl.util.SourcePosition;

public class StructDeclarationNode extends ParentNode implements UnresolvedNode {
    private Identifier identifier;

    public StructDeclarationNode(SourcePosition position, String identifier) {
        this(position, null, Identifier.orNull(identifier));
    }

    public StructDeclarationNode(SourcePosition position, ParentNode parentNode, Identifier identifier) {
        super(position, parentNode);
        this.identifier = identifier;
    }

    /**
     * Returns the struct identifier or null if it's an anonymous struct.
     */
    public Identifier getIdentifier() {
        return identifier;
    }

    public void addFieldDeclaration(VariableDeclarationListNode fieldList) {
        addChild(fieldList);
    }

    public VariableDeclarationListNode getFieldDeclarationList(int index) {
        return getChildAs(index);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        StructDeclarationNode node = new StructDeclarationNode(getSourcePosition(), newParent, new Identifier(identifier));
        return CloneUtils.cloneChildren(this, node);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitStructDeclarationNode(this);
    }
}
