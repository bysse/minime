package com.tazadum.glsl.language.ast;

import com.tazadum.glsl.language.type.GLSLType;

/**
 * Created by Erik on 2016-10-20.
 */
public interface HasMutableType {
    void setType(GLSLType type);
}
