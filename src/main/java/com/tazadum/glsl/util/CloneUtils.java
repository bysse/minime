package com.tazadum.glsl.util;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ParentNode;

/**
 * Created by Erik on 2016-10-07.
 */
public class CloneUtils {
    public static <T extends Node> T clone(T node, ParentNode parent) {
        if (node == null) {
            return null;
        }
        return (T) node.clone(parent);
    }

    public static <T extends Node> T clone(T node) {
        if (node == null) {
            return null;
        }
        return (T) node.clone(null);
    }

    public static <T extends ParentNode> T cloneChildren(T source, T clone) {
        for (int i = 0; i < source.getChildCount(); i++) {
            final Node clonedChild = clone(source.getChild(i), clone);
            clone.setChild(i, clonedChild);
        }
        return clone;
    }
}
