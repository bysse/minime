package com.tazadum.glsl.eval.type;

import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.PredefinedType;

public class TypedFloat implements TypedVariable {
    private Float value;

    public TypedFloat(Float value) {
        this.value = value;
    }

    @Override
    public GLSLType getType() {
        return PredefinedType.FLOAT;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
