package com.tazadum.glsl.language.ast.variable;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.model.ArraySpecifiers;
import com.tazadum.glsl.language.type.FullySpecifiedType;
import com.tazadum.glsl.util.SourcePosition;

public class ParameterDeclarationNode extends VariableDeclarationNode {
    public ParameterDeclarationNode(SourcePosition position, FullySpecifiedType fst, String identifier, ArraySpecifiers arraySpecifiers) {
        super(position, false, fst, identifier, arraySpecifiers, null);
    }

    protected ParameterDeclarationNode(SourcePosition position, ParentNode newParent, FullySpecifiedType fst, Identifier identifier, ArraySpecifiers arraySpecifiers) {
        super(position, newParent, false, fst, identifier, arraySpecifiers, null);
    }

    @Override
    public ParentNode clone(ParentNode newParent) {
        final ParameterDeclarationNode node = new ParameterDeclarationNode(getSourcePosition(), newParent, type, identifier, null);
        node.setInitializer(CloneUtils.clone(getInitializer(), node));
        return node;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitParameterDeclaration(this);
    }
}
