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
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Erik on 2016-10-20.
 */
public class DeclarationSqueezeTest {
    final private OutputSizeDecider decider = new OutputSizeDecider();

    private ParserContext parserContext;
    private Output output;
    private DeclarationSqueeze declarationSqueeze;
    private TypeChecker typeChecker;
    private OutputConfig config;

    @Before
    public void setup() {
        parserContext = TestUtils.parserContext();
        output = new Output();
        declarationSqueeze = new DeclarationSqueeze();
        typeChecker = new TypeChecker();
        config = new OutputConfig();
        config.setNewlines(false);

        decider.getConfig().setMaxDecimals(3);
    }

    @Test
    public void test_basic_1() {
        assertEquals("vec3 a,b,c;", optimize("vec3 a;vec3 b;vec3 c;"));
    }

    @Test
    public void test_basic_2() {
        assertEquals("float a,b=2.,c;", optimize("float a;float b=2.;float c;"));
    }

    @Test
    public void test_basic_3() {
        assertEquals("float a,b=2.,c=b;", optimize("float a;float b=2.;float c=b;"));
    }

    @Test
    public void test_squeeze_1() {
        assertEquals("float a,b,c=a;int x=a;", optimize("float a;float b;int x=a;float c=a;"));
    }

    @Test
    public void test_squeeze_2() {
        assertEquals("float a,b;int x=a++;float c=a;", optimize("float a;float b;int x=a++;float c=a;"));
    }

    @Test
    public void test_uniform_1() {
        assertEquals("uniform int a;void main(){int b,c=a;}", optimize("uniform int a;void main(){int b;int c=a;}"));
    }

    @Test
    public void test_context_1() {
        assertEquals("int a;void main(){int b,c=a;}", optimize("int a;void main(){int b;int c=a;}"));
    }

    private String optimize(String source) {
        try {
            final CommonTokenStream stream = TestUtils.tokenStream(source);
            final GLSLParser parser = TestUtils.parser(stream);
            final ContextVisitor visitor = new ContextVisitor(parserContext);
            Node node = parser.translation_unit().accept(visitor);
            typeChecker.check(parserContext, node);

            //node.accept(new IdVisitor());

            Optimizer.OptimizerResult result = declarationSqueeze.run(parserContext, decider, node);
            return output.render(result.getNode(), config).trim();
        } catch (Exception e) {
            for (Token token : TestUtils.getTokens(TestUtils.tokenStream(source))) {
                System.out.println(token);
            }
            throw e;
        }
    }
}