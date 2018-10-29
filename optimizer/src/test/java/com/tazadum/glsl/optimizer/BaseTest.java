package com.tazadum.glsl.optimizer;

import com.tazadum.glsl.TestUtil;
import com.tazadum.glsl.language.ast.ASTConverter;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.output.OutputConfig;
import com.tazadum.glsl.language.output.OutputConfigBuilder;
import com.tazadum.glsl.language.output.OutputRenderer;
import com.tazadum.glsl.parser.GLSLParser;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TypeVisitor;
import com.tazadum.glsl.util.SourcePosition;
import com.tazadum.glsl.util.SourcePositionId;
import com.tazadum.glsl.util.SourcePositionMapper;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public abstract class BaseTest {
    protected ParserContext parserContext;
    protected OutputRenderer output;
    protected OutputConfig outputConfig;
    protected OutputSizeDecider decider;

    protected ParserRuleContext extractContext(GLSLParser parser) {
        return parser.translation_unit();
    }

    protected void testInit() {
        parserContext = TestUtil.parserContext();
        outputConfig = new OutputConfigBuilder()
            .renderNewLines(false)
            .indentation(0)
            .significantDecimals(3)
            .build();

        this.output = new OutputRenderer();
        this.decider = new OutputSizeDecider(3);
    }

    protected String toString(Node node) {
        return output.render(node, outputConfig);
    }

    protected String toString(Node node, OutputConfig outputConfig) {
        return output.render(node, outputConfig);
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
