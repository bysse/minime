package com.tazadum.glsl.parser.finder;

import com.tazadum.glsl.ast.*;
import com.tazadum.glsl.ast.function.FunctionCallNode;

/**
 * Created by Erik on 2016-10-23.
 */
public class NodeFinder {
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

    public static FunctionCallNode findFunctionCall(Node node) {
        if (node instanceof FunctionCallNode) {
            return (FunctionCallNode) node;
        }
        final ParentNode parent = node.getParentNode();
        if (parent == null || node instanceof StatementListNode) {
            return null;
        }
        return findFunctionCall(parent);
    }

    public static boolean isNodeInTree(Node needle, Node haystack) {
        final VariableFinderVisitor visitor = new VariableFinderVisitor(needle);
        haystack.accept(visitor);
        return visitor.isFound();
    }

    private static class VariableFinderVisitor extends DefaultASTVisitor<Boolean> {
        private Node needle;
        private boolean found;

        public VariableFinderVisitor(Node needle) {
            this.needle = needle;
            this.found = false;
        }

        public boolean isFound() {
            return found;
        }

        @Override
        protected <T extends ParentNode> void visitChildren(T node) {
            if (found) {
                return;
            }
            if (needle.equals(node)) {
                found = true;
            }
            for (int i = 0; i < node.getChildCount(); i++) {
                node.getChild(i).accept(this);
            }
        }
    }
}
