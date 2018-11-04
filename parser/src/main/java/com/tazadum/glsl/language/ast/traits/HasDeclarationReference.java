package com.tazadum.glsl.language.ast.traits;

/**
 * Created by erikb on 2018-11-04.
 */
public interface HasDeclarationReference<T> {
    T getDeclarationNode();

    void setDeclarationNode(T declarationNode);
}
