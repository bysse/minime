package com.tazadum.glsl.language.ast.type;

import com.tazadum.glsl.exception.BadImplementationException;
import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.LeafNode;
import com.tazadum.glsl.language.model.ArraySpecifiers;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Node for holding a fully specified type in the AST.
 */
public class TypeSpecifierNode extends LeafNode {
    private final GLSLType customType;
    private final GLSLType baseType;
    private final ArraySpecifiers arraySpecifiers;

    public TypeSpecifierNode(SourcePosition position, GLSLType customType, GLSLType baseType) {
        this(position, customType, baseType, null);
    }

    public TypeSpecifierNode(SourcePosition position, GLSLType customType, GLSLType baseType, ArraySpecifiers arraySpecifiers) {
        super(position);
        this.customType = customType;
        this.baseType = baseType;
        this.arraySpecifiers = arraySpecifiers;
    }

    public GLSLType getCustomType() {
        return customType;
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
