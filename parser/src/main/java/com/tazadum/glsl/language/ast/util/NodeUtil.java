package com.tazadum.glsl.language.ast.util;


import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.variable.ArrayIndexNode;
import com.tazadum.glsl.language.ast.variable.FieldSelectionNode;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Created by erikb on 2018-10-13.
 */
public class NodeUtil {
    public static <T extends Node> T cast(Node node) {
        return (T) node;
    }

    public static SourcePosition getSourcePosition(Node node) {
        if (node instanceof ArrayIndexNode) {
            return getSourcePosition(((ArrayIndexNode) node).getExpression());
        }
        if (node instanceof FieldSelectionNode) {
            return getSourcePosition(((FieldSelectionNode) node).getExpression());
        }
        return node.getSourcePosition();
    }
}
