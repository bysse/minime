package com.tazadum.glsl.optimizer;

import com.tazadum.glsl.parser.ParserContext;

public class OptimizerContext {
    private ParserContext parserContext;
    private BranchRegistry branchRegistry;

    public OptimizerContext(ParserContext parserContext) {
        this(parserContext, new BranchRegistry());
    }

    public OptimizerContext(ParserContext parserContext, BranchRegistry branchRegistry) {
        this.parserContext = parserContext;
        this.branchRegistry = branchRegistry;
    }

    public ParserContext parserContext() {
        return parserContext;
    }

    public BranchRegistry branchRegistry() {
        return branchRegistry;
    }
}
