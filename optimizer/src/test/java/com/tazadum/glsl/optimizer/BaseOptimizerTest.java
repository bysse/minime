package com.tazadum.glsl.optimizer;

import com.tazadum.glsl.TestUtil;
import com.tazadum.glsl.language.ast.ASTConverter;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.output.OutputConfig;
import com.tazadum.glsl.language.output.OutputConfigBuilder;
import com.tazadum.glsl.language.output.OutputRenderer;
import com.tazadum.glsl.optimizer.pipeline.BranchingOptimizerPipeline;
import com.tazadum.glsl.optimizer.pipeline.OptimizerPipeline;
import com.tazadum.glsl.optimizer.pipeline.SingleShaderOptimizerPipeline;
import com.tazadum.glsl.parser.GLSLParser;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TypeVisitor;
import com.tazadum.glsl.util.SourcePosition;
import com.tazadum.glsl.util.SourcePositionId;
import com.tazadum.glsl.util.SourcePositionMapper;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public abstract class BaseOptimizerTest {
    protected OptimizerPipeline pipeline;

    protected ParserContext parserContext;
    protected OutputRenderer output;
    protected OutputConfig outputConfig;
    protected OutputSizeDecider decider;

    protected boolean showDebug = false;

    protected abstract OptimizerType[] getOptimizerTypes();

    protected ParserRuleContext extractContext(GLSLParser parser) {
        return parser.translation_unit();
    }

    protected void testInit() {
        testInit(0);
    }

    protected void testInit(int branchDepth) {
        parserContext = TestUtil.parserContext();
        outputConfig = new OutputConfigBuilder()
            .renderNewLines(false)
            .indentation(0)
            .significantDecimals(3)
            .build();

        if (branchDepth > 0) {
            TreePruner pruner = TreePruner.byIterationDepth(branchDepth);
            this.pipeline = new BranchingOptimizerPipeline(pruner, outputConfig, getOptimizerTypes());
        } else {
            this.pipeline = new SingleShaderOptimizerPipeline(outputConfig, getOptimizerTypes());
        }
        this.output = new OutputRenderer();
        this.decider = new OutputSizeDecider(3);

        this.showDebug = false;
    }

    protected String toString(Node node) {
        return output.render(node, outputConfig);
    }

    protected Node optimize(String source) {
        Node node = compile(parserContext, source);

        OptimizerContext context = new OptimizerContext(node, parserContext);
        return pipeline.optimize(context, node, true);
    }

    protected Node compile(ParserContext parserContext, String source) {
        try {
            ParserRuleContext context = TestUtil.parse(source, this::extractContext);

            SourcePositionMapper mapper = new SourcePositionMapper();
            mapper.remap(SourcePosition.TOP, SourcePositionId.DEFAULT);

            Node node = context.accept(new ASTConverter(mapper, parserContext));
            node.accept(new TypeVisitor(parserContext));

            return node;
        } catch (Exception e) {
            System.out.println("Dumping source tokens:");
            for (Token token : TestUtil.getTokens(TestUtil.tokenStream(source))) {
                System.out.println("  " + token);
            }
            throw e;
        }
    }
}
