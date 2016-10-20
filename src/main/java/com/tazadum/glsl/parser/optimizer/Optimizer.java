package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Node;

/**
 * Created by Erik on 2016-10-20.
 */
public interface Optimizer {
    OptimizerResult run(Node node);

    class OptimizerResult {
        private int changes;
        private Node node;

        public OptimizerResult(int changes, Node node) {
            this.changes = changes;
            this.node = node;
        }

        public int getChanges() {
            return changes;
        }

        public Node getNode() {
            return node;
        }
    }
}
