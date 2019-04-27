package com.tazadum.glsl.optimizer;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.optimizer.pipeline.BranchingOptimizerPipeline;
import com.tazadum.glsl.optimizer.pipeline.OptimizerPipeline;
import com.tazadum.glsl.optimizer.pipeline.SingleShaderOptimizerPipeline;

public abstract class BaseOptimizerTest extends BaseTest {
    protected OptimizerPipeline pipeline;

    protected abstract OptimizerType[] getOptimizerTypes();

    protected void testInit() {
        testInit(0, true);
    }

    protected void testInit(int branchDepth) {
        testInit(branchDepth, true);
    }

    protected void testInit(int branchDepth, boolean useBuiltIn) {
        super.testInit(useBuiltIn, useBuiltIn);

        if (branchDepth > 0) {
            TreePruner pruner = TreePruner.byIterationDepth(branchDepth);
            this.pipeline = new BranchingOptimizerPipeline(pruner, outputConfig, 20, getOptimizerTypes());
        } else {
            this.pipeline = new SingleShaderOptimizerPipeline(outputConfig, getOptimizerTypes());
        }
    }

    protected Node optimize(String source) {
        return optimizeBranch(source).getNode();
    }

    protected Branch optimizeBranch(String source) {
        Node node = compile(parserContext, source);

        OptimizerContext context = new OptimizerContext(parserContext);
        Branch branch = pipeline.optimize(context, node, true, null);
        parserContext = branch.getContext();
        return branch;
    }
}
