package com.tazadum.glsl.language.ast.variable;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.type.FullySpecifiedType;
import com.tazadum.glsl.util.SourcePosition;

public class ParameterDeclarationNode extends VariableDeclarationNode {
    public ParameterDeclarationNode(SourcePosition position, FullySpecifiedType fst, String identifier, Node arraySpecifier) {
        super(position, false, fst, identifier, arraySpecifier, null);
    }

    protected ParameterDeclarationNode(SourcePosition position, ParentNode newParent, FullySpecifiedType fst, Identifier identifier, Node arraySpecifier, Node initializer) {
        super(position, newParent, false, fst, identifier, arraySpecifier, initializer);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        final ParameterDeclarationNode node = new ParameterDeclarationNode(getSourcePosition(), newParent, type, identifier, null, null);
        node.setArraySpecifier(CloneUtils.clone(getArraySpecifier(), node));
        node.setInitializer(CloneUtils.clone(getInitializer(), node));
        return node;

    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitParameterDeclaration(this);
    }
}
