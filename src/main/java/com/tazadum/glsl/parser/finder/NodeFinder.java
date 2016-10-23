package com.tazadum.glsl.parser.finder;

import com.tazadum.glsl.ast.DefaultASTVisitor;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ParentNode;

/**
 * Created by Erik on 2016-10-23.
 */
public class NodeFinder {
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
