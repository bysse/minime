package com.tazadum.glsl.ast.arithmetic;

import com.tazadum.glsl.ast.ASTVisitor;
import com.tazadum.glsl.ast.HasNumeric;
import com.tazadum.glsl.ast.LeafNode;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.language.BuiltInType;
import com.tazadum.glsl.language.GLSLType;
import com.tazadum.glsl.language.Numeric;

public class FloatLeafNode extends LeafNode implements HasNumeric {
    private final Numeric value;

    public FloatLeafNode(Numeric value) {
        this(null, value);
    }

    public FloatLeafNode(ParentNode parentNode, Numeric value) {
        super(parentNode);
        this.value = value;
    }

    public Numeric getValue() {
        return value;
    }

    @Override
    public LeafNode clone(ParentNode newParent) {
        return new FloatLeafNode(newParent, value);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitFloat(this);
    }

    @Override
    public GLSLType getType() {
        return BuiltInType.FLOAT;
    }
}
