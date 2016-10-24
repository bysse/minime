package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.output.Output;
import com.tazadum.glsl.output.OutputConfig;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class OptimizerTest {
    private final String shaderSource;

    private ParserContext parserContext;
    private Output output;
    private OutputConfig config;

    public OptimizerTest(String name, String shaderSource) {
        this.shaderSource = shaderSource;
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> shaders() {
        //return TestUtils.loadShaders("shaders");
        return Collections.emptySet();
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
        assertEquals(shader, renderedShader);
    }
}