package com.tazadum.glsl.language.ast.traits;

import com.tazadum.glsl.language.ast.Node;

/**
 * Created by erikb on 2018-10-16.
 */
public interface HasConstState {
    boolean isConstant();

    default void setConstant(boolean constant) {
    }

    static boolean isConst(Node node) {
        if (node == null) {
            return true;
        }
        return node instanceof HasConstState && ((HasConstState) node).isConstant();
    }
}
