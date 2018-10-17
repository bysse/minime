package com.tazadum.glsl.language.ast.type;

import com.tazadum.glsl.language.ast.LeafNode;
import com.tazadum.glsl.language.ast.struct.StructDeclarationNode;
import com.tazadum.glsl.language.type.FullySpecifiedType;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Node for holding a fully specified type in the AST.
 */
public class TypeNode extends LeafNode {
    private final FullySpecifiedType fullySpecifiedType;
    private final StructDeclarationNode structDeclarationNode;

    public TypeNode(SourcePosition position, FullySpecifiedType fullySpecifiedType, StructDeclarationNode structDeclarationNode) {
        super(position, null);
        this.fullySpecifiedType = fullySpecifiedType;
        this.structDeclarationNode = structDeclarationNode;
    }

    public FullySpecifiedType getFullySpecifiedType() {
        return fullySpecifiedType;
    }

    public StructDeclarationNode getStructDeclaration() {
        return structDeclarationNode;
    }
}
