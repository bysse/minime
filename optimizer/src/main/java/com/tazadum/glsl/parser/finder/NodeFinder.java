package com.tazadum.glsl.parser.finder;

import com.tazadum.glsl.ast.*;
import com.tazadum.glsl.ast.expression.AssignmentNode;
import com.tazadum.glsl.ast.function.FunctionCallNode;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

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

    public static Node findParent(Node node, Predicate<Node> predicate) {
        while (node != null) {
            if (predicate.test(node)) {
                return node;
            }
            node = node.getParentNode();
        }
        return null;
    }

    public static boolean isMutated(Node node) {
        final MutatingOperation operation = NodeFinder.findMutableOperation(node);
        if (operation == null) {
            return false;
        }
        if (operation instanceof AssignmentNode) {
            // check if the variable usage is in the assignment part of an AssignmentNode
            final Node assignment = ((AssignmentNode) operation).getLeft();
            return NodeFinder.isNodeInTree(node, assignment);
        }
        // The MutatingOperation was not an AssignmentNode of the 'good' type
        return true;
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

    public static <T> Set<T> findAll(Node node, Class<T> nodeType) {
        final NodeFinderVisitor<T> visitor = new NodeFinderVisitor<>(nodeType);
        node.accept(visitor);
        return visitor.getResult();
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

        @Override
        protected void visitLeafNode(LeafNode node) {
            if (needle.equals(node)) {
                found = true;
            }
        }
    }

    private static class NodeFinderVisitor<T> extends DefaultASTVisitor<Boolean> {
        private Class<T> nodeType;
        private Set<T> result;

        public NodeFinderVisitor(Class<T> nodeType) {
            this.nodeType = nodeType;
            this.result = new HashSet<>();
        }

        public Set<T> getResult() {
            return result;
        }

        @Override
        protected <P extends ParentNode> void visitChildren(P node) {
            if (nodeType.isAssignableFrom(node.getClass())) {
                result.add((T) node);
            }
            super.visitChildren(node);
        }

        @Override
        protected void visitLeafNode(LeafNode leafNode) {
            if (nodeType.isAssignableFrom(leafNode.getClass())) {
                result.add((T) leafNode);
            }
            super.visitLeafNode(leafNode);
        }
    }
}
