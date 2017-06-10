package com.tazadum.glsl.parser.finder;

import com.tazadum.glsl.ast.DefaultASTVisitor;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.function.FunctionCallNode;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Erik on 2016-10-23.
 */
public class FunctionFinder {
    public static SortedSet<FunctionCallNode> findFunctionCalls(Node node) {
        final FunctionFinderVisitor visitor = new FunctionFinderVisitor();
        node.accept(visitor);
        return visitor.getNodes();
    }

    private static class FunctionFinderVisitor extends DefaultASTVisitor<Void> {
        private SortedSet<FunctionCallNode> nodes = new TreeSet<>();


        @Override
        public Void visitFunctionCall(FunctionCallNode node) {
            nodes.add(node);
            return null;
        }

        SortedSet<FunctionCallNode> getNodes() {
            return nodes;
        }
    }
}
