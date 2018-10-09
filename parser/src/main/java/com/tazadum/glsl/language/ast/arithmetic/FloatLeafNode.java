package com.tazadum.glsl.language.ast.arithmetic;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.HasNumeric;
import com.tazadum.glsl.language.ast.LeafNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.Numeric;
import com.tazadum.glsl.language.type.PredefinedType;
import com.tazadum.glsl.util.SourcePosition;

public class FloatLeafNode extends LeafNode implements HasNumeric {
    private final Numeric value;

    public FloatLeafNode(SourcePosition position, Numeric value) {
        this(position, null, value);
    }

    public FloatLeafNode(SourcePosition position, ParentNode parentNode, Numeric value) {
        super(position, parentNode);
        this.value = value;
    }

    public Numeric getValue() {
        return value;
    }

    @Override
    public LeafNode clone(ParentNode newParent) {
        return new FloatLeafNode(getSourcePosition(), newParent, value);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitFloat(this);
    }

    @Override
    public GLSLType getType() {
        return PredefinedType.FLOAT;
    }
}
