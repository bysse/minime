package com.tazadum.glsl.simplification;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ParentNode;
import com.tazadum.glsl.ast.function.FunctionCallNode;
import com.tazadum.glsl.ast.function.FunctionDefinitionNode;
import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.output.IdentifierOutput;
import com.tazadum.glsl.output.OutputConfig;
import com.tazadum.glsl.output.OutputVisitor;
import com.tazadum.glsl.parser.finder.FunctionFinder;

public class TreeNodeComparator implements NodeComparator {
    private OutputConfig config;

    public TreeNodeComparator() {
        config = new OutputConfig();
        config.setIdentifiers(IdentifierOutput.None);
        config.setMaxDecimals(5);
    }

    @Override
    public boolean equals(Node a, Node b) {
        if (!safeOperations(a) || !safeOperations(b)) {
            return false;
        }

        OutputVisitor visitor = new OutputVisitor(config);

        String sourceA = a.accept(visitor);
        String sourceB = b.accept(visitor);

        return sourceA.equals(sourceB);
    }

    private boolean safeOperations(Node node) {
        for (FunctionCallNode functionCallNode : FunctionFinder.findFunctionCalls(node)) {
            final FunctionPrototypeNode declarationNode = functionCallNode.getDeclarationNode();

            if (declarationNode.getPrototype().isBuiltIn()) {
                // the built-in functions dos not mutate global shader state
                continue;
            }

            final ParentNode functionDefinition = declarationNode.getParentNode();
            if (functionDefinition instanceof FunctionDefinitionNode) {
                final FunctionDefinitionNode definition = (FunctionDefinitionNode) functionDefinition;
                if (definition.usesGlobalState() || definition.mutatesGlobalState()) {
                    return false;
                }
            }
        }
        return true;
    }
}
