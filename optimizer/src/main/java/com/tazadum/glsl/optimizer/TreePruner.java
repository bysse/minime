package com.tazadum.glsl.optimizer;

public interface TreePruner {
    boolean prune(int iterationDepth, int sizeDifference);

    static TreePruner byIterationDepth(final int maxDepth) {
        return (i, s) -> i >= maxDepth;
    }

    static TreePruner bySizeDifference(final int maxSize) {
        return (i, s) -> s >= maxSize;
    }

    static TreePruner or(TreePruner a, TreePruner b) {
        return (i, s) -> a.prune(i, s) || b.prune(i, s);
    }

}
