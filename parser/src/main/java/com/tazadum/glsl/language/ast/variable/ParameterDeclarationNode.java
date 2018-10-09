package com.tazadum.glsl.language.ast.variable;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.FullySpecifiedType;

public class ParameterDeclarationNode extends VariableDeclarationNode {
    public ParameterDeclarationNode(FullySpecifiedType fst, String identifier, Node arraySpecifier) {
        super(false, fst, identifier, arraySpecifier, null);
    }

    protected ParameterDeclarationNode(ParentNode newParent, FullySpecifiedType fst, Identifier identifier, Node arraySpecifier, Node initializer) {
        super(newParent, false, fst, identifier, arraySpecifier, initializer);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        final ParameterDeclarationNode node = new ParameterDeclarationNode(newParent, type, identifier, null, null);
        node.setArraySpecifier(CloneUtils.clone(getArraySpecifier(), node));
        node.setInitializer(CloneUtils.clone(getInitializer(), node));
        return node;

    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitParameterDeclaration(this);
    }
}
