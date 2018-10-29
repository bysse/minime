package com.tazadum.glsl.optimizer;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.optimizer.pipeline.BranchingOptimizerPipeline;
import com.tazadum.glsl.optimizer.pipeline.OptimizerPipeline;
import com.tazadum.glsl.optimizer.pipeline.SingleShaderOptimizerPipeline;

public abstract class BaseOptimizerTest extends BaseTest {
    protected OptimizerPipeline pipeline;

    protected abstract OptimizerType[] getOptimizerTypes();

    protected void testInit() {
        testInit(0);
    }

    protected void testInit(int branchDepth) {
        super.testInit();

        if (branchDepth > 0) {
            TreePruner pruner = TreePruner.byIterationDepth(branchDepth);
            this.pipeline = new BranchingOptimizerPipeline(pruner, outputConfig, getOptimizerTypes());
        } else {
            this.pipeline = new SingleShaderOptimizerPipeline(outputConfig, getOptimizerTypes());
        }
    }

    protected Node optimize(String source) {
        Node node = compile(parserContext, source);

        OptimizerContext context = new OptimizerContext(parserContext);
        return pipeline.optimize(context, node, true, null).getNode();
    }
}
