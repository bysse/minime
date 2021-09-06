package com.tazadum.glsl.optimizer.simplification;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.function.FunctionCallNode;
import com.tazadum.glsl.language.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.ast.util.NodeFinder;

public class TreeNodeComparator implements NodeComparator {
    public TreeNodeComparator() {
    }

    @Override
    public boolean equals(Node a, Node b) {
        if (!safeOperations(a) || !safeOperations(b)) {
            return false;
        }

        return CloneUtils.equal(a, b);
    }

    private boolean safeOperations(Node node) {
        for (FunctionCallNode functionCallNode : NodeFinder.findAll(node, FunctionCallNode.class)) {
            final FunctionPrototypeNode declarationNode = functionCallNode.getDeclarationNode();

            if (declarationNode.getPrototype().isBuiltIn()) {
                // the built-in functions dos not mutate global shader state
                continue;
            }

            if (declarationNode.usesGlobalState() || declarationNode.mutatesGlobalState()) {
                return false;
            }
        }
        return true;
    }
}
