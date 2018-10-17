package com.tazadum.glsl.language.ast.type;

import com.tazadum.glsl.exception.BadImplementationException;
import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.LeafNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.model.ArraySpecifiers;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.util.SourcePosition;

public class ArraySpecifierNode extends LeafNode {
    private ArraySpecifiers specifiers = new ArraySpecifiers();

    public ArraySpecifierNode(SourcePosition position) {
        this(position, null);
    }

    public ArraySpecifierNode(SourcePosition position, ParentNode parentNode) {
        super(position, parentNode);
    }

    public ArraySpecifiers getArraySpecifiers() {
        return specifiers;
    }

    @Override
    public ArraySpecifierNode clone(ParentNode newParent) {
        return new ArraySpecifierNode(getSourcePosition(), null);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        throw new BadImplementationException("The node shouldn't be in the AST");
    }

    @Override
    public GLSLType getType() {
        return null;
    }
}
