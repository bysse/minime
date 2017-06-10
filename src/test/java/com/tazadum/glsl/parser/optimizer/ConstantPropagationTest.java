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
public class ConstantPropagationTest {
    final private OutputSizeDecider decider = new OutputSizeDecider();

    private Node node;
    private ParserContext parserContext;
    private Output output;
    private OutputConfig outputConfig;
    private ConstantPropagation constantPropagation;
    private TypeChecker typeChecker;

    @Before
    public void setup() {
        parserContext = TestUtils.parserContext();
        output = new Output();
        constantPropagation = new ConstantPropagation();
        typeChecker = new TypeChecker();
        outputConfig = new OutputConfig();
        outputConfig.setNewlines(false);
        outputConfig.setIndentation(0);

        decider.getConfig().setMaxDecimals(3);
    }

    @Test
    public void test_constants_1() {
        assertEquals("int f(){return 1;}", optimize("const int a=1;int f(){return a;}"));
    }

    @Test
    public void test_constants_2() {
        assertEquals("int f(){return 1;}", optimize("int a=1;int f(){return a;}"));
    }

    @Test
    public void test_constants_3() {
        assertEquals("int f(){return (1+2);}", optimize("int a=(1+2);int f(){return a;}"));
    }

    @Test
    public void test_constants_4() {
        assertEquals("vec3 f(){return vec3(0);}", optimize("vec3 a=vec3(0);vec3 f(){return a;}"));
    }

    @Test
    public void test_constants_5() {
        assertEquals("float f(){return 1+1;}", optimize("float a=1;float f(){return a+a;}"));
    }

    @Test
    public void test_constants_6() {
        assertEquals("int main(){int x=0;for(int i=0;i<5;i++)x+=i;return x;}", optimize("int main(){int x=0;for(int i=0;i<5;i++)x+=i;return x;}"));
    }

    @Test
    public void test_constants_7() {
        assertEquals("int main(){vec2 a=2*(vec2(1,1)-1).xy;return a.x+a.y;}", optimize("vec2 m=vec2(1,1)-1;int main(){vec2 a=2*m.xy;return a.x+a.y;}"));
    }

    @Test
    public void test_constants_8() {
        assertEquals("int a(){return 2;}int main(){int b=a(),x=0;for(int i=0;i<5;i++)x+=i*b;return x;}", optimize("int a(){return 2;}int main(){int b=a(),x=0;for(int i=0;i<5;i++){x+=i*b;}return x;}"));
    }

    @Test
    public void test_constants_9() {
        optimize(ContextBasedMultiIdentifierShortenerTest.SHADER_2);

        final OutputConfig config = new OutputConfig();
        config.setIdentifiers(IdentifierOutput.None);
        config.setNewlines(false);
        config.setIndentation(0);
        config.setOutputConst(false);

        ContextBasedMultiIdentifierShortener shortener = new ContextBasedMultiIdentifierShortener(true);
        shortener.register(parserContext, node, config);
        shortener.apply();

        outputConfig.setNewlines(true);
        outputConfig.setIndentation(3);
        outputConfig.setIdentifiers(IdentifierOutput.Replaced);

        System.out.println(output.render(node, outputConfig));
    }

    @Test
    public void test_fail_1() {
        assertEquals("int a=1;int f(){a++;return a;}", optimize("int a=1;int f(){a++;return a;}"));
    }

    @Test
    public void test_fail_2() {
        assertEquals("int b=0;int g(){return b++;}int a=g();int f(){return a;}", optimize("int b=0;int g(){return b++;}int a=g();int f(){return a;}"));
    }

    private String optimize(String source) {
        try {
            final CommonTokenStream stream = TestUtils.tokenStream(source);
            final GLSLParser parser = TestUtils.parser(stream);
            final ContextVisitor visitor = new ContextVisitor(parserContext);
            node = parser.translation_unit().accept(visitor);
            typeChecker.check(parserContext, node);
            Optimizer.OptimizerResult result = constantPropagation.run(parserContext, decider, node);
            return output.render(result.getNode(), outputConfig);
        } catch (Exception e) {
            for (Token token : TestUtils.getTokens(TestUtils.tokenStream(source))) {
                System.out.println(token);
            }
            throw e;
        }
    }
}
