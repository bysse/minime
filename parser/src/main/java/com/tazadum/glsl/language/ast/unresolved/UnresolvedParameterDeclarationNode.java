package com.tazadum.glsl.language.ast.unresolved;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.type.ArraySpecifierListNode;
import com.tazadum.glsl.util.SourcePosition;

public class UnresolvedParameterDeclarationNode extends UnresolvedVariableDeclarationNode {
    public UnresolvedParameterDeclarationNode(SourcePosition position, UnresolvedTypeNode typeNode, String identifier, ArraySpecifierListNode arraySpecifier) {
        this(position, null, typeNode, identifier, arraySpecifier);
    }

    public UnresolvedParameterDeclarationNode(SourcePosition position, ParentNode newParent, UnresolvedTypeNode typeNode, String identifier, ArraySpecifierListNode arraySpecifier) {
        super(position, newParent, typeNode, identifier, arraySpecifier, null);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitParameterDeclaration(this);
    }
}
