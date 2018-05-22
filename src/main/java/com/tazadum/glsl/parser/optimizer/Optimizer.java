package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.parser.ParserContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erik on 2016-10-20.
 */
public interface Optimizer {
    String name();

    OptimizerResult run(ParserContext parserContext, OptimizationDecider optimizationDecider, Node node);

    default OptimizerResult runWithOptimizer(OptimizerVisitor visitor, ParserContext parserContext, Node node) {
        int changes, totalChanges = 0;
        do {
            visitor.reset();
            Node accept = node.accept(visitor);
            changes = visitor.getChanges();
            totalChanges += changes;

            if (accept != null) {
                node = accept;
            }
        } while (changes > 0);

        return new OptimizerResult(totalChanges, new OptimizerBranch(parserContext, node));
    }

    class OptimizerResult {
        private int changes = 0;
        private List<OptimizerBranch> branches = new ArrayList<>();

        public OptimizerResult(int changes, OptimizerBranch node) {
            this.changes = changes;
            this.branches.add(node);
        }

        public OptimizerResult(int changes, List<OptimizerBranch> branches) {
            this.changes = changes;
            this.branches = branches;
        }

        public int getChanges() {
            return changes;
        }

        public boolean hasBranches() {
            return branches.size() > 1;
        }

        public List<OptimizerBranch> getBranches() {
            return branches;
        }
    }

    class OptimizerBranch {
        private ParserContext context;
        private Node node;

        public OptimizerBranch(ParserContext context, Node node) {
            this.context = context;
            this.node = node;
        }

        public ParserContext getContext() {
            return context;
        }

        public Node getNode() {
            return node;
        }
    }

}
