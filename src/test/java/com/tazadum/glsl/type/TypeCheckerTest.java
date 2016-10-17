package com.tazadum.glsl.type;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

/**
 * Created by Erik on 2016-10-17.
 */
@RunWith(Parameterized.class)
public class TypeCheckerTest {
    private final String shaderSource;

    private ParserContext parserContext;
    private TypeChecker typeChecker;

    public TypeCheckerTest(String name, String shaderSource) {
        this.shaderSource = shaderSource;
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> shaders() {
        return TestUtils.loadShaders("type_test");
    }

    @Before
    public void setup() {
        parserContext = TestUtils.parserContext();
        typeChecker = new TypeChecker();
    }

    @Test
    public void testShaderOutput() {
        testOutput(shaderSource);
    }

    private TypeLookup testOutput(String shader) {
        final Node node = TestUtils.parse(parserContext, Node.class, shader);
        return typeChecker.check(parserContext, node);
    }
}