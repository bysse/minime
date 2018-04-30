package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.output.Output;
import com.tazadum.glsl.output.OutputConfig;
import com.tazadum.glsl.output.OutputSizeDecider;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TestUtils;
import com.tazadum.glsl.parser.type.TypeChecker;
import com.tazadum.glsl.parser.visitor.ContextVisitor;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Erik on 2016-10-20.
 */
public class FunctionInlineTest {
    final private OutputSizeDecider decider = new OutputSizeDecider();

    private ParserContext parserContext;
    private Output output;
    private FunctionInline functionInline;
    private TypeChecker typeChecker;
    private OutputConfig config;

    @BeforeEach
    public void setup() {
        parserContext = TestUtils.parserContext();
        output = new Output();
        functionInline = new FunctionInline();
        typeChecker = new TypeChecker();
        config = new OutputConfig();
        config.setNewlines(false);
        config.setIndentation(0);

        decider.getConfig().setMaxDecimals(3);
    }

    @Test
    @DisplayName("Cases that should be ignored")
    public void testIgnore() {
        assertEquals(
                "float func(float a){return 2*a;}float a=func(2);void main(){float a=func(2);}",
                optimize("float func(float a){return 2*a;}float a=func(2);void main(){float a=func(2);}")
        );
    }

    @Test
    @DisplayName("Basic inlining")
    public void testBasic() {
       assertEquals(
               "void main(){float a=func(2);}",
                optimize("float func(float a){return 2*a;}void main(){float a=func(2);}")
        );
    }

    private String optimize(String source) {
        try {
            final CommonTokenStream stream = TestUtils.tokenStream(source);
            final GLSLParser parser = TestUtils.parser(stream);
            final ContextVisitor visitor = new ContextVisitor(parserContext);
            Node node = parser.translation_unit().accept(visitor);
            typeChecker.check(parserContext, node);

            Optimizer.OptimizerResult result = functionInline.run(parserContext, decider, node);
            return output.render(result.getNode(), config).trim();
        } catch (Exception e) {
            for (Token token : TestUtils.getTokens(TestUtils.tokenStream(source))) {
                System.out.println(token);
            }
            throw e;
        }
    }
}
