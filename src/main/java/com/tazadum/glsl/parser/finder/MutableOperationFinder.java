package com.tazadum.glsl.parser.finder;

import com.tazadum.glsl.ast.MutatingOperation;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.ast.StatementListNode;

/**
 * Created by Erik on 2016-10-23.
 */
public class MutableOperationFinder {
    public static MutatingOperation findMutableOperation(Node node) {
        if (node instanceof MutatingOperation) {
            return (MutatingOperation) node;
        }
        final ParentNode parent = node.getParentNode();
        if (parent == null || node instanceof StatementListNode) {
            return null;
        }
        return findMutableOperation(parent);
    }
}
