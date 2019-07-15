package com.tazadum.glsl.optimizer;

import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.optimizer.pipeline.BranchingOptimizerPipeline;
import com.tazadum.glsl.optimizer.pipeline.OptimizerPipeline;
import com.tazadum.glsl.optimizer.pipeline.SingleShaderOptimizerPipeline;
import com.tazadum.glsl.preprocessor.language.GLSLVersion;
import com.tazadum.glsl.stage.PreprocessorStage;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        verifyIdConsistency(branch.getNode(), 1);

        return branch;
    }

    private int verifyIdConsistency(Node node, int expectedId) {
        assertEquals(expectedId, node.getId(), "Node id sequence is not in order");

        if (node instanceof ParentNode) {
            ParentNode parent = (ParentNode) node;
            for (int i = 0; i < parent.getChildCount(); i++) {
                Node child = parent.getChild(i);
                if (child == null) {
                    continue;
                }
                expectedId = verifyIdConsistency(child, expectedId + 1);
            }
        }

        return expectedId;
    }
}
