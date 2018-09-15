package com.tazadum.glsl.ast.variable;

import com.tazadum.glsl.ast.ASTVisitor;
import com.tazadum.glsl.ast.Identifier;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.parser.type.FullySpecifiedType;
import com.tazadum.glsl.util.CloneUtils;

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
