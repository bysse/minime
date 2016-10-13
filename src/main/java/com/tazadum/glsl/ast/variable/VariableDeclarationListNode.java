package com.tazadum.glsl.ast.variable;

import com.tazadum.glsl.ast.ASTVisitor;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.parser.type.FullySpecifiedType;
import com.tazadum.glsl.util.CloneUtils;

public class VariableDeclarationListNode extends ParentNode {
    private FullySpecifiedType fst;

    public VariableDeclarationListNode(FullySpecifiedType fst) {
        this.fst = fst;
    }

    public VariableDeclarationListNode(ParentNode parentNode, FullySpecifiedType fst) {
        super(parentNode);
        this.fst = fst;
    }

    public void setFullySpecifiedType(FullySpecifiedType fst) {
        this.fst = fst;
    }

    public FullySpecifiedType getFullySpecifiedType() {
        return fst;
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        return CloneUtils.cloneChildren(this, new VariableDeclarationListNode(fst));
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitVariableDeclarationList(this);
    }
}
