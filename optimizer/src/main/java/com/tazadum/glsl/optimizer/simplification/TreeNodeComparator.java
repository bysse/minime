package com.tazadum.glsl.optimizer.simplification;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.function.FunctionCallNode;
import com.tazadum.glsl.language.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.ast.util.NodeFinder;
import com.tazadum.glsl.language.output.IdentifierOutputMode;
import com.tazadum.glsl.language.output.OutputConfig;
import com.tazadum.glsl.language.output.OutputConfigBuilder;
import com.tazadum.glsl.language.output.OutputVisitor;

public class TreeNodeComparator implements NodeComparator {
    private OutputConfig config;

    public TreeNodeComparator() {
        config = new OutputConfigBuilder()
            .identifierMode(IdentifierOutputMode.None)
            .significantDecimals(5)
            .indentation(0)
            .renderNewLines(false)
            .build();
    }

    @Override
    public boolean equals(Node a, Node b) {
        if (!safeOperations(a) || !safeOperations(b)) {
            return false;
        }

        return CloneUtils.equal(a, b);
        /*
        OutputVisitor visitor = new OutputVisitor(config);
        String sourceA = a.accept(visitor).get();
        String sourceB = b.accept(visitor).get();
        return sourceA.equals(sourceB);
        */
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
