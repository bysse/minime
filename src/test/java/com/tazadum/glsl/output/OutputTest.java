package com.tazadum.glsl.output;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TestUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Erik on 2016-10-13.
 */
public class OutputTest {
    private ParserContext parserContext;
    private Output output;
    private OutputConfig config;

    @Before
    public void setup() {
        parserContext = TestUtils.parserContext();
        output = new Output();

        config = new OutputConfig();
    }

    @Test
    public void testSimple() {
        testOutput("uniform vec3 color;\n" +
                        "void main() {\n" +
                        "gl_FragColor = vec4(color, 1);\n" +
                        "}\n");
    }


    private void testOutput(String shader) {
        final Node node = TestUtils.parse(parserContext, Node.class, shader);
        final String renderedShader = output.render(node, config);
        System.out.println(renderedShader);
        assertEquals(shader, renderedShader);
    }
}