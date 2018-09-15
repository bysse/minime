package com.tazadum.glsl.parser.finder;

import com.tazadum.glsl.ast.DefaultASTVisitor;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.variable.VariableNode;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Erik on 2016-10-23.
 */
public class VariableFinder {
    public static SortedSet<VariableNode> findVariables(Node node) {
        final VariableFinderVisitor visitor = new VariableFinderVisitor();
        node.accept(visitor);
        return visitor.getNodes();
    }

    private static class VariableFinderVisitor extends DefaultASTVisitor<Void> {
        private SortedSet<VariableNode> nodes = new TreeSet<>();

        @Override
        public Void visitVariable(VariableNode node) {
            nodes.add(node);
            return null;
        }

        SortedSet<VariableNode> getNodes() {
            return nodes;
        }
    }
}
