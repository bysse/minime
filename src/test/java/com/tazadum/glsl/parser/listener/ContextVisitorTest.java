package com.tazadum.glsl.parser.listener;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TestUtils;
import com.tazadum.glsl.parser.visitor.ContextVisitor;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.junit.Before;
import org.junit.Test;

/**
 * @author erikb
 * @since 2016-07-31
 */
public class ContextVisitorTest {
    private ParserContext parserContext;

    @Before
    public void setUp() {
        this.parserContext = TestUtils.parserContext();
    }

    @Test
    public void test_simple() {
        Node node = parse(
            "uniform vec4 color;" +
                "void main() {" +
                "gl_FragColor = color;" +
                "}");
    }

    private Node parse(String source) {
        try {
            final CommonTokenStream stream = TestUtils.tokenStream(source);
            final GLSLParser parser = TestUtils.parser(stream);
            final ContextVisitor visitor = new ContextVisitor(parserContext);
            return parser.translation_unit().accept(visitor);
        } catch (Exception e) {
            for (Token token : TestUtils.getTokens(TestUtils.tokenStream(source))) {
                System.out.println(token);
            }

            throw e;
        }
    }
}
