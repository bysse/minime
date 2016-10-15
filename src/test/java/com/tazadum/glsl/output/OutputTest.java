package com.tazadum.glsl.output;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class OutputTest {
    private final String shaderSource;

    private ParserContext parserContext;
    private Output output;
    private OutputConfig config;

    public OutputTest(String name, String shaderSource) {
        this.shaderSource = shaderSource;
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> shaders() {
        return TestUtils.loadShaders("preformatted");
    }

    @Before
    public void setup() {
        parserContext = TestUtils.parserContext();
        output = new Output();
        config = new OutputConfig();
    }

    @Test
    public void testShaderOutput() {
        testOutput(shaderSource);
    }

    private void testOutput(String shader) {
        final Node node = TestUtils.parse(parserContext, Node.class, shader);
        final String renderedShader = output.render(node, config);
        System.out.println(renderedShader);
        assertEquals(shader, renderedShader);
    }
}