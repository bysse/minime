package com.tazadum.glsl.parser.listener;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.StatementListNode;
import com.tazadum.glsl.ast.function.FunctionDefinitionNode;
import com.tazadum.glsl.ast.variable.VariableDeclarationListNode;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TestUtils;
import com.tazadum.glsl.parser.visitor.ContextVisitor;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author erikb
 * @since 2016-07-31
 */
public class ContextVisitorTest {
    private ParserContext parserContext;

    @Before
    public void setUp() {
        this.parserContext = TestUtils.parserContext();
    }

    @Test
    public void test_simple_1() {
        StatementListNode node = parse(StatementListNode.class,
            "uniform vec4 color;" +
                "void main() {" +
                "  gl_FragColor = color;" +
                "}");

        assertEquals(2, node.getChildCount());
        assertEquals(VariableDeclarationListNode.class, node.getChild(0).getClass());
        assertEquals(FunctionDefinitionNode.class, node.getChild(1).getClass());
    }

    @Test
    public void test_unary_arithmetic() {
        StatementListNode node = parse(StatementListNode.class,
                "void main() {" +
                        "float v = 2.0f;" +
                        "float r = -v;" +
                        "float g = r++ * r--;"+
                        "float b = ++g - g--;"+
                        "gl_FragColor = vec3(r, g, b);" +
                "}");

        assertEquals(2, node.getChildCount());
        assertEquals(VariableDeclarationListNode.class, node.getChild(0).getClass());
        assertEquals(FunctionDefinitionNode.class, node.getChild(1).getClass());
    }


    private <T extends Node> T parse(Class<T> type, String source) {
        try {
            final CommonTokenStream stream = TestUtils.tokenStream(source);
            final GLSLParser parser = TestUtils.parser(stream);
            final ContextVisitor visitor = new ContextVisitor(parserContext);
            Node node = parser.translation_unit().accept(visitor);
            assertTrue("Expected node type " + type.getSimpleName(), type.isAssignableFrom(node.getClass()));
            return (T) node;
        } catch (Exception e) {
            for (Token token : TestUtils.getTokens(TestUtils.tokenStream(source))) {
                System.out.println(token);
            }
            throw e;
        }
    }
}
