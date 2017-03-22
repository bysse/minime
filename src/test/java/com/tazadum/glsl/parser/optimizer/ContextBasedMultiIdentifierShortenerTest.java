package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.output.IdentifierOutput;
import com.tazadum.glsl.output.Output;
import com.tazadum.glsl.output.OutputConfig;
import com.tazadum.glsl.output.OutputSizeDecider;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TestUtils;
import com.tazadum.glsl.parser.type.TypeChecker;
import com.tazadum.glsl.parser.visitor.ContextVisitor;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ContextBasedMultiIdentifierShortenerTest {
    final private OutputSizeDecider decider = new OutputSizeDecider();

    private Output output;
    private ContextBasedMultiIdentifierShortener identifierShortener;
    private TypeChecker typeChecker;
    private OutputConfig config;

    @Before
    public void setup() {
        output = new Output();
        identifierShortener = new ContextBasedMultiIdentifierShortener(true);
        typeChecker = new TypeChecker();
        config = new OutputConfig();
        config.setNewlines(false);
        config.setIndentation(0);
        decider.getConfig().setMaxDecimals(3);
    }

    @Test
    public void test_basic_1() {
        config.setIdentifiers(IdentifierOutput.None);
        Node node1 = compile(TestUtils.parserContext(), "float x=0.;void main(){float y=tan(0.0)*tan(0.0);}");
        Node node2 = compile(TestUtils.parserContext(), "vec2 x=vec2(1.0);");

        identifierShortener.apply();

        config.setIdentifiers(IdentifierOutput.Replaced);

        System.out.println(output.render(node1, config));
        System.out.println(output.render(node2, config));

        assertEquals("vec2 a=vec2(1.);", output.render(node2, config));
    }

    private Node compile(ParserContext parserContext, String source) {
        try {
            final CommonTokenStream stream = TestUtils.tokenStream(source);
            final GLSLParser parser = TestUtils.parser(stream);

            final ContextVisitor visitor = new ContextVisitor(parserContext);
            Node node = parser.translation_unit().accept(visitor);
            typeChecker.check(parserContext, node);
            identifierShortener.register(parserContext, node, config);
            return node;
        } catch (Exception e) {
            for (Token token : TestUtils.getTokens(TestUtils.tokenStream(source))) {
                System.out.println(token);
            }
            throw e;
        }
    }
}