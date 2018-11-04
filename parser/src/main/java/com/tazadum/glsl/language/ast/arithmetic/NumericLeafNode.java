package com.tazadum.glsl.language.ast.arithmetic;

import com.tazadum.glsl.language.ast.ASTVisitor;
import com.tazadum.glsl.language.ast.LeafNode;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.traits.HasConstState;
import com.tazadum.glsl.language.ast.traits.HasNumeric;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.Numeric;
import com.tazadum.glsl.util.SourcePosition;

public class NumericLeafNode extends LeafNode implements HasNumeric, HasConstState {
    private final Numeric value;

    public NumericLeafNode(SourcePosition position, Numeric value) {
        this(position, null, value);
    }

    public NumericLeafNode(SourcePosition position, ParentNode parentNode, Numeric value) {
        super(position, parentNode);
        this.value = value;
    }

    public Numeric getValue() {
        return value;
    }

    @Override
    public LeafNode clone(ParentNode newParent) {
        return new NumericLeafNode(getSourcePosition(), newParent, value);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitNumeric(this);
    }

    @Override
    public GLSLType getType() {
        return value.getType();
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    public String toString() {
        return value.getValue().toPlainString();
    }
}
