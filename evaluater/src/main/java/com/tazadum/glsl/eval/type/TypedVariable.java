package com.tazadum.glsl.eval.type;

import com.tazadum.glsl.language.type.GLSLType;

public interface TypedVariable {
    GLSLType getType();

    Object getValue();
}
