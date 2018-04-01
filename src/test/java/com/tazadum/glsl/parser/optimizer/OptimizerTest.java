package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.output.Output;
import com.tazadum.glsl.output.OutputConfig;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TestUtils;
import com.tazadum.glsl.util.OpenGLForTesting;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OptimizerTest {
    private static OpenGLForTesting openGL;

    private ParserContext parserContext;
    private Output output;
    private OutputConfig config;

    @BeforeAll
    public static void setupContext() {
        System.out.println("Initializing OpenGL");

        openGL = new OpenGLForTesting();
        openGL.waitForInit();
    }

    @AfterAll
    public static void destroyContext() {
    }

    public static Collection<Object[]> loadShaders() {
        return TestUtils.loadShaders("shaders");
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
        final String renderedShader = output.render(node, config);
        assertEquals(shader, renderedShader);
    }
}
