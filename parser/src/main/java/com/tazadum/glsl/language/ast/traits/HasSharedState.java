package com.tazadum.glsl.language.ast.traits;

/**
 * Created by erikb on 2018-09-14.
 */
public interface HasSharedState {
    String getSharedUnit();

    void setSharedUnit(String unit);
}
