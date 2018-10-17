package com.tazadum.glsl.language.ast.type;

import com.tazadum.glsl.exception.BadImplementationException;
import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.FixedChildParentNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.struct.StructDeclarationNode;
import com.tazadum.glsl.language.model.ArraySpecifiers;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Node for holding a fully specified type in the AST.
 */
public class TypeSpecifierNode extends FixedChildParentNode {
    private final GLSLType baseType;
    private final ArraySpecifiers arraySpecifiers;

    public TypeSpecifierNode(SourcePosition position, GLSLType baseType) {
        this(position, baseType, null);
    }

    public TypeSpecifierNode(SourcePosition position, GLSLType baseType, ArraySpecifiers arraySpecifiers) {
        this(position, null, null, baseType, arraySpecifiers);
    }

    public TypeSpecifierNode(SourcePosition position, StructDeclarationNode structDeclaration, GLSLType baseType, ArraySpecifiers arraySpecifiers) {
        this(position, null, structDeclaration, baseType, arraySpecifiers);
    }

    public TypeSpecifierNode(SourcePosition position, ParentNode parentNode, StructDeclarationNode structDeclaration, GLSLType baseType, ArraySpecifiers arraySpecifiers) {
        super(position, 1, parentNode);
        this.baseType = baseType;
        this.arraySpecifiers = arraySpecifiers;

        setChild(0, structDeclaration);
    }

    public StructDeclarationNode getStructDeclaration() {
        return getChildAs(0);
    }

    public GLSLType getBaseType() {
        return baseType;
    }

    public ArraySpecifiers getArraySpecifiers() {
        return arraySpecifiers;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        throw new BadImplementationException("This node shouldn't be part of the AST");
    }
}
