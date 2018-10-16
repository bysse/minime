package com.tazadum.glsl.language.ast.traits;

/**
 * Created by erikb on 2018-10-16.
 */
public interface HasConstState {
    boolean isConstant();

    void setConstant(boolean constant);
}
