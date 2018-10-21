package com.tazadum.glsl.language.ast.util;

import com.tazadum.glsl.language.ast.*;
import com.tazadum.glsl.language.ast.expression.AssignmentNode;
import com.tazadum.glsl.language.ast.function.FunctionCallNode;
import com.tazadum.glsl.language.ast.traits.MutatingOperation;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Created by Erik on 2016-10-23.
 */
public class NodeFinder {
    /**
     * Searches through all parents to a node until the predicate return true or
     * we run out of nodes.
     *
     * @param node      Node to start the search with ie inclusive.
     * @param predicate The condition to satify.
     * @return The found node or null.
     */
    public static Node findParent(Node node, Predicate<Node> predicate) {
        while (node != null) {
            if (predicate.test(node)) {
                return node;
            }
            node = node.getParentNode();
        }
        return null;
    }

    /**
     * Searches through the parents of a node until it hits a statement list
     * to try and find out if the statement contains a mutable operation.
     *
     * @param node Node to search from, inclusive.
     * @return The mutating node or null if nothing was found.
     */
    public static MutatingOperation findNearestMutableOperation(Node node) {
        if (node instanceof MutatingOperation) {
            return (MutatingOperation) node;
        }
        final ParentNode parent = node.getParentNode();
        if (parent == null || node instanceof StatementListNode) {
            return null;
        }
        return findNearestMutableOperation(parent);
    }

    /**
     * Searches through the parents of a node until it hits a statement list
     * to try and find out if the node is part of a function call.
     *
     * @param node Node to search from, inclusive.
     * @return The function node or null if nothing was found.
     */
    public static FunctionCallNode findNearestFunctionCall(Node node) {
        if (node instanceof FunctionCallNode) {
            return (FunctionCallNode) node;
        }
        final ParentNode parent = node.getParentNode();
        if (parent == null || node instanceof StatementListNode) {
            return null;
        }
        return findNearestFunctionCall(parent);
    }

    /**
     * Checks if a node is part of an assignment or other mutating operation.
     *
     * @param node Node to search from, inclusive.
     * @return True if the node is mutated otherwise false.
     */
    public static boolean isMutated(Node node) {
        final MutatingOperation operation = NodeFinder.findNearestMutableOperation(node);
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

    /**
     * Find all nodes in the tree of a certain type.
     *
     * @param node     The node to start the search from, inclusive.
     * @param nodeType The type of the node to find.
     * @param <T>      The subtype to Node to search for.
     * @return A Set of all nodes that were found.
     */
    public static <T extends Node> Set<T> findAll(Node node, Class<T> nodeType) {
        final NodeFinderVisitor<T> visitor = new NodeFinderVisitor<>(nodeType);
        node.accept(visitor);
        return visitor.getResult();
    }

    /**
     * Returns true as soon as 'needle' is found in the 'haystack'.
     *
     * @param needle   The node to search for.
     * @param haystack The node tree to search in, inclusive search.
     * @return True if the node was in the tree, otherwise false.
     */
    public static boolean isNodeInTree(Node needle, Node haystack) {
        final NodeDetectorVisitor visitor = new NodeDetectorVisitor(needle);
        haystack.accept(visitor);
        return visitor.isFound();
    }

    private static class NodeDetectorVisitor extends DefaultASTVisitor<Boolean> {
        private Node needle;
        private boolean found;

        NodeDetectorVisitor(Node needle) {
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

    private static class NodeFinderVisitor<T extends Node> extends DefaultASTVisitor<Boolean> {
        private Class<T> nodeType;
        private Set<T> result;

        NodeFinderVisitor(Class<T> nodeType) {
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
