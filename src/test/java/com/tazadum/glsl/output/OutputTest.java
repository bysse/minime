package com.tazadum.glsl.output;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OutputTest {
    private ParserContext parserContext;
    private Output output;
    private OutputConfig config;

    public static Collection<String> loadShaders() {
        return TestUtils.loadShaders("preformatted");
    }

    @BeforeEach
    public void setup() {
        parserContext = TestUtils.parserContext();
        output = new Output();
        config = new OutputConfig();
    }

    @ParameterizedTest
    @MethodSource("loadShaders")
    public void testShaderOutput(String shaderSource) {
        testOutput(shaderSource);
    }

    private void testOutput(String shader) {
        final Node node = TestUtils.parse(parserContext, Node.class, shader);
        final String renderedShader = output.render(node.clone(null), config);
        //System.out.println(renderedShader);
        assertEquals(shader, renderedShader);
    }
}
