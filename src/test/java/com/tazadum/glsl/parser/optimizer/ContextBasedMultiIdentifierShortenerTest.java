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

    private static final String SHADER_1 = "  uniform vec3 iResolution;\n" +
            "    void main(void) {\n" +
            "        vec2 uv = gl_FragCoord.xy / iResolution.xy;\n" +
            "        vec3 color = vec3(fract(5.0*(uv.x + uv.y)));\n" +
            "        gl_FragColor = vec4(color, 10 * (1 + sin(6*iResolution.z)));\n" +
            "    }";

    private static String SHADER_2 = "uniform vec3 iResolution;\n" +
            "uniform sampler2D image;\n" +
            "float WIDTH = iResolution.x;\n" +
            "float HEIGHT = iResolution.y;\n" +
            "const float GA = 2.399; \n" +
            "const mat2 rot = mat2(cos(GA),sin(GA),-sin(GA),cos(GA));\n" +
            "vec3 dof(sampler2D tex,vec2 uv,float rad) {\n" +
            "    vec3 acc=vec3(0);\n" +
            "    vec2 pixel=vec2(.002*HEIGHT/WIDTH,.002),angle=vec2(0,rad);\n" +
            "    for (int j=0;j<80;j++) {  \n" +
            "        rad += 1./rad;\n" +
            "        angle*=rot;\n" +
            "        acc+=texture(tex,uv+pixel*(rad-1.)*angle).xyz;\n" +
            "    }\n" +
            "    return acc/80.;\n" +
            "}\n" +
            "void main(void) {\n" +
            "    vec2 uv=gl_FragCoord.xy/vec2(WIDTH, HEIGHT);\n" +
            "    gl_FragColor=vec4(dof(image,uv,texture(image,uv).w),1.);\n" +
            "}";

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

        assertEquals("vec2 a=vec2(1);", output.render(node2, config));
    }

    @Test
    public void test_basic_2() {
        config.setIdentifiers(IdentifierOutput.None);
        Node node1 = compile(TestUtils.parserContext(), "uniform vec2 X; void main(){ for (int Y=0;Y<10;Y++) { X.x += 1; }}");
        Node node2 = compile(TestUtils.parserContext(), "uniform vec2 X; void main() { gl_FragColor = vec3(X.xy, 0); }");

        do {
            identifierShortener.apply();

            config.setIdentifiers(IdentifierOutput.Replaced);

            System.out.println(output.render(node1, config));
            System.out.println(output.render(node2, config));

        } while(identifierShortener.permutateIdentifiers());
    }

    @Test
    public void test_basic_3() {
        config.setIdentifiers(IdentifierOutput.None);
        Node node1 = compile(TestUtils.parserContext(), SHADER_1);

        identifierShortener.apply();
        config.setIdentifiers(IdentifierOutput.Replaced);
        System.out.println(output.render(node1, config));
    }

    @Test
    public void test_basic_4() {
        config.setIdentifiers(IdentifierOutput.None);
        config.setNewlines(true);

        Node node1 = compile(TestUtils.parserContext(),SHADER_1);
        Node node2 = compile(TestUtils.parserContext(),SHADER_2);

        identifierShortener.apply();

        do {
            config.setIdentifiers(IdentifierOutput.Replaced);

            System.out.println(output.render(node1, config));
            System.out.println(output.render(node2, config));

        } while(identifierShortener.permutateIdentifiers());
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