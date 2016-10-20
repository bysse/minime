package com.tazadum.glsl.ast.arithmetic;

import com.tazadum.glsl.ast.ASTVisitor;
import com.tazadum.glsl.ast.LeafNode;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.language.BuiltInType;
import com.tazadum.glsl.language.GLSLType;
import com.tazadum.glsl.language.Numeric;

public class IntLeafNode extends LeafNode {
    private final Numeric value;

    public IntLeafNode(Numeric value) {
        this(null, value);
    }

    public IntLeafNode(ParentNode parentNode, Numeric value) {
        super(parentNode);
        this.value = value;
    }

    public Numeric getValue() {
        return value;
    }

    @Override
    public LeafNode clone(ParentNode newParent) {
        return new IntLeafNode(newParent, value);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitInt(this);
    }

    @Override
    public GLSLType getType() {
        return BuiltInType.INT;
    }
}
