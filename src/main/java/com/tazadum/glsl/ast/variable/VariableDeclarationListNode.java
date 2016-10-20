package com.tazadum.glsl.ast.variable;

import com.tazadum.glsl.ast.ASTVisitor;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.language.GLSLType;
import com.tazadum.glsl.parser.type.FullySpecifiedType;
import com.tazadum.glsl.util.CloneUtils;

public class VariableDeclarationListNode extends ParentNode {
    private FullySpecifiedType type;

    public VariableDeclarationListNode(FullySpecifiedType type) {
        this.type = type;
    }

    public VariableDeclarationListNode(ParentNode parentNode, FullySpecifiedType type) {
        super(parentNode);
        this.type = type;
    }

    public void setFullySpecifiedType(FullySpecifiedType fst) {
        this.type = fst;
    }

    public FullySpecifiedType getFullySpecifiedType() {
        return type;
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new VariableDeclarationListNode(type));
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitVariableDeclarationList(this);
    }

    @Override
    public GLSLType getType() {
        return type.getType();
    }
}
