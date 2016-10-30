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
public class IdentifierShortenerTest {
    final private OutputSizeDecider decider = new OutputSizeDecider();

    private ParserContext parserContext;
    private Output output;
    private IdentifierShortener identifierShortener;
    private TypeChecker typeChecker;
    private OutputConfig config;

    @Before
    public void setup() {
        parserContext = TestUtils.parserContext();
        output = new Output();
        identifierShortener = new IdentifierShortener();
        typeChecker = new TypeChecker();
        config = new OutputConfig();
        config.setNewlines(false);
        config.setIndentation(0);
        config.setIdentifiers(IdentifierOutput.Replaced);
        decider.getConfig().setMaxDecimals(3);
    }

    @Test
    public void test_basic_1() {
        assertEquals("vec2 o;void main(){float a=o.x;}", optimize("vec2 apanjapan;void main(){float a=apanjapan.x;}"));
    }

    @Test
    public void test_basic_2() {
        assertEquals("int i(){return 3;}void main(){int a=i();}", optimize("int f(){return 3;}void main(){int a=f();}"));
    }

    @Test
    public void test_advanced_1() {
        assertEquals("int i(){int e=0;return e;}int r(){int n=0;for(int t=0;t<5;t++){n=t;}return n;}void main(){int o=r()+i();float x=float(o);}",
                optimize("int g(){int X=0;return X;}int f(){int Z=0;for(int X=0;X<5;X++){Z=X;}return Z;}void main(){int a=f()+g();float x=float(a);}"));
    }

    private String optimize(String source) {
        try {
            final CommonTokenStream stream = TestUtils.tokenStream(source);
            final GLSLParser parser = TestUtils.parser(stream);
            final ContextVisitor visitor = new ContextVisitor(parserContext);
            Node node = parser.translation_unit().accept(visitor);
            typeChecker.check(parserContext, node);

            config.setIdentifiers(IdentifierOutput.None);
            identifierShortener.analyse(parserContext, node, config);

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
