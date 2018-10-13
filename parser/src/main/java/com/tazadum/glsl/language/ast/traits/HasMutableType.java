package com.tazadum.glsl.language.ast.traits;

import com.tazadum.glsl.language.type.GLSLType;

/**
 * Indicates that the type can be changed on the nodes with this interface.
 * Created by Erik on 2016-10-20.
 */
public interface HasMutableType {
    void setType(GLSLType type);
}
