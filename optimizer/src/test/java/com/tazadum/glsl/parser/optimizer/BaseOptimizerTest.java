package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.GLSLOptimizerContext;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.output.Output;
import com.tazadum.glsl.output.OutputConfig;
import com.tazadum.glsl.output.OutputSizeDecider;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TestUtils;
import com.tazadum.glsl.parser.visitor.ContextVisitor;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public abstract class BaseOptimizerTest {
    protected OptimizerPipeline pipeline;
    protected GLSLOptimizerContext optimizerContext;

    protected Output output;
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
        outputConfig = new OutputConfig();
        outputConfig.setNewlines(false);
        outputConfig.setIndentation(0);

        if (branchDepth > 0) {
            TreePruner pruner = TreePruner.byIterationDepth(branchDepth);
            this.pipeline = new BranchingOptimizerPipeline(pruner, outputConfig, getOptimizerTypes());
        } else {
            this.pipeline = new SingleShaderOptimizerPipeline(outputConfig, getOptimizerTypes());
        }
        this.optimizerContext = new GLSLOptimizerContext("test-shader");
        this.output = new Output();

        decider = new OutputSizeDecider();
        decider.getConfig().setMaxDecimals(3);

        this.showDebug = false;
    }

    protected String optimize(String source) {
        try {
            ParserContext parserContext = optimizerContext.parserContext();

            final CommonTokenStream stream = TestUtils.tokenStream(source);
            final GLSLParser parser = TestUtils.parser(stream);
            final ContextVisitor visitor = new ContextVisitor(parserContext);
            Node node = extractContext(parser).accept(visitor);
            optimizerContext.typeChecker().check(parserContext, node);

            pipeline.setDebugOutput(showDebug);
            Node result = pipeline.optimize(optimizerContext, node, true);
            return output.render(result, outputConfig).trim();
        } catch (Exception e) {
            System.out.println("Dumping source tokens:");
            for (Token token : TestUtils.getTokens(TestUtils.tokenStream(source))) {
                System.out.println("  " + token);
            }
            throw e;
        }
    }
}
