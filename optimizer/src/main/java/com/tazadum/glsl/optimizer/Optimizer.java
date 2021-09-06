package com.tazadum.glsl.optimizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erik on 2016-10-20.
 */
public interface Optimizer {
    String name();

    OptimizerResult run(BranchRegistry branchRegistry, OptimizationDecider optimizationDecider, Branch branch);

    class OptimizerResult {
        private int changes;
        private Branch inputBranch;
        private List<Branch> branches = new ArrayList<>();

        public OptimizerResult(int changes, Branch inputBranch) {
            this.changes = changes;
            this.inputBranch = inputBranch;
        }

        public OptimizerResult(int changes, Branch inputBranch, List<Branch> branches) {
            this.changes = changes;
            this.inputBranch = inputBranch;
            this.branches = branches;
        }

        public int getChanges() {
            return changes;
        }

        public Branch getInputBranch() {
            return inputBranch;
        }

        public boolean hasBranches() {
            return !branches.isEmpty();
        }

        public List<Branch> getBranches() {
            return branches;
        }
    }
}
