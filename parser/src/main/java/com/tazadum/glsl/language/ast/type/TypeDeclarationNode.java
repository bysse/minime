package com.tazadum.glsl.language.ast.type;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.struct.StructDeclarationNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.type.FullySpecifiedType;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Indicates that a type will be specified further down the source listing.
 */
public class TypeDeclarationNode extends VariableDeclarationNode {
    public TypeDeclarationNode(SourcePosition position, FullySpecifiedType type, StructDeclarationNode structDeclaration) {
        super(position, false, type, null, null, null, structDeclaration);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitTypeDeclaration(this);
    }
}
