package com.tazadum.glsl.optimizer;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.parser.ParserContext;

public class OptimizerContext {
    private Node node;
    private ParserContext parserContext;
    private BranchRegistry branchRegistry;

    public OptimizerContext(Node node, ParserContext parserContext) {
        this(node, parserContext, new BranchRegistry());
    }

    public OptimizerContext(Node node, ParserContext parserContext, BranchRegistry branchRegistry) {
        this.node = node;
        this.parserContext = parserContext;
        this.branchRegistry = branchRegistry;
    }

    public Node node() {
        return node;
    }

    public ParserContext parserContext() {
        return parserContext;
    }

    public BranchRegistry branchRegistry() {
        return branchRegistry;
    }
}
