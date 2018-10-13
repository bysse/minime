package com.tazadum.glsl.language.ast.util;


import com.tazadum.glsl.language.ast.Node;

/**
 * Created by erikb on 2018-10-13.
 */
public class NodeUtil {
    public static <T extends Node> T cast(Node node) {
        return (T) node;
    }
}
