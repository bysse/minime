package com.tazadum.glsl.ast;

import com.tazadum.glsl.language.GLSLType;

/**
 * Created by Erik on 2016-10-20.
 */
public interface HasMutableType {
    void setType(GLSLType type);
}
