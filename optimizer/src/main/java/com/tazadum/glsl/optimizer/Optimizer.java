package com.tazadum.glsl.optimizer;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.parser.ParserContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erik on 2016-10-20.
 */
public interface Optimizer {
    String name();

    OptimizerResult run(ParserContext context, BranchRegistry branchRegistry, OptimizationDecider optimizationDecider, Node node);

    class OptimizerResult {
        private int changes = 0;
        private List<Branch> branches = new ArrayList<>();

        public OptimizerResult(int changes, Branch node) {
            this.changes = changes;
            this.branches.add(node);
        }

        public OptimizerResult(int changes, List<Branch> branches) {
            this.changes = changes;
            this.branches = branches;
        }

        public int getChanges() {
            return changes;
        }

        public boolean hasBranches() {
            return branches.size() > 1;
        }

        public List<Branch> getBranches() {
            return branches;
        }
    }
}
