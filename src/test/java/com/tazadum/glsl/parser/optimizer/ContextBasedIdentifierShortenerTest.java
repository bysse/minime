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

import static org.junit.Assert.assertEquals;

/**
 * Created by Erik on 2016-10-20.
 */
public class ContextBasedIdentifierShortenerTest {
    final private OutputSizeDecider decider = new OutputSizeDecider();

    private ParserContext parserContext;
    private Output output;
    private ContextBasedIdentifierShortener identifierShortener;
    private TypeChecker typeChecker;
    private OutputConfig config;

    @Before
    public void setup() {
        parserContext = TestUtils.parserContext();
        output = new Output();
        identifierShortener = new ContextBasedIdentifierShortener();
        typeChecker = new TypeChecker();
        config = new OutputConfig();
        config.setNewlines(false);
        config.setIndentation(0);
        config.setIdentifiers(IdentifierOutput.Replaced);
        decider.getConfig().setMaxDecimals(3);
    }

    @Test
    public void test_basic_1() {
        assertEquals("vec2 o;void main(){float v=o.x;}", optimize("vec2 apanjapan;void main(){float a=apanjapan.x;}"));
    }

    @Test
    public void test_basic_2() {
        assertEquals("int i(){return 3;}void main(){int n=i();}", optimize("int f(){return 3;}void main(){int a=f();}"));
    }

    @Test
    public void test_advanced_1() {
        assertEquals("float t=0.;void main(out float o){float a=t+o;}", optimize("float X=0.;void main(out float Y){float i=X+Y;}"));
    }

    @Test
    public void test_advanced_2() {
        assertEquals("vec2 x=0;vec3 v=0;vec3 c(){return vec3(0.);}void main(out vec3 e){vec3 y=v;vec2 o=x+e.xy+e.xy+c().xy;}",
                optimize("" +
                        "vec2 X=0;" +
                        "vec3 V=0;" +
                        "vec3 Z(){return vec3(0.);}" +
                        "void main(out vec3 Y){" +
                        "vec3 A = V;" +
                        "vec2 i=X+Y.xy+Y.xy+Z().xy;" +
                        "}"));
    }

    private String optimize(String source) {
        try {
            final CommonTokenStream stream = TestUtils.tokenStream(source);
            final GLSLParser parser = TestUtils.parser(stream);
            final ContextVisitor visitor = new ContextVisitor(parserContext);
            Node node = parser.translation_unit().accept(visitor);
            typeChecker.check(parserContext, node);

            config.setIdentifiers(IdentifierOutput.None);
            identifierShortener.updateIdentifiers(parserContext, node, config);

            config.setIdentifiers(IdentifierOutput.Replaced);
            return output.render(node, config).trim();
        } catch (Exception e) {
            for (Token token : TestUtils.getTokens(TestUtils.tokenStream(source))) {
                System.out.println(token);
            }
            throw e;
        }
    }
}
