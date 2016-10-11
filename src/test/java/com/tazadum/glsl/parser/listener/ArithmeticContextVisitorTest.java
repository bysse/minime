package com.tazadum.glsl.parser.listener;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.arithmetic.*;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.language.NumericOperator;
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
public class ArithmeticContextVisitorTest {
    private ParserContext parserContext;

    @Before
    public void setUp() {
        this.parserContext = TestUtils.parserContext();
    }

    @Test
    public void test_numeric_simple_operations() {
        assertEquals(NumericOperator.ADD, parse(NumericOperationNode.class, "1 + 1").getOperator());
        assertEquals(NumericOperator.SUB, parse(NumericOperationNode.class, "1 - 1").getOperator());
        assertEquals(NumericOperator.MUL, parse(NumericOperationNode.class, "1 * 1").getOperator());
        assertEquals(NumericOperator.DIV, parse(NumericOperationNode.class, "1 / 1").getOperator());
    }

    @Test
    public void test_numeric_adv() {
        NumericOperationNode add = parse(NumericOperationNode.class, "1 + 2 + 3");
        NumericOperationNode sub = parse(NumericOperationNode.class, "1 - 2 - 3");
        NumericOperationNode mul = parse(NumericOperationNode.class, "1 * 2 * 3");
        NumericOperationNode div = parse(NumericOperationNode.class, "1 / 2 / 3");

        assertEquals(NumericOperator.ADD, add.getOperator());
        assertEquals(NumericOperator.SUB, sub.getOperator());
        assertEquals(NumericOperator.MUL, mul.getOperator());
        assertEquals(NumericOperator.DIV, div.getOperator());

        assertTrue(add.getLeft() instanceof NumericOperationNode);
        assertTrue(add.getRight() instanceof IntLeafNode);

        assertTrue(sub.getLeft() instanceof NumericOperationNode);
        assertTrue(sub.getRight() instanceof IntLeafNode);

        assertTrue(mul.getLeft() instanceof NumericOperationNode);
        assertTrue(mul.getRight() instanceof IntLeafNode);

        assertTrue(div.getLeft() instanceof NumericOperationNode);
        assertTrue(div.getRight() instanceof IntLeafNode);
    }

    @Test
    public void test_numeric_prefix() {
        parse(PrefixOperationNode.class, "++1");
        parse(PrefixOperationNode.class, "--1");
    }

    @Test
    public void test_numeric_postfix() {
        parse(PostfixOperationNode.class, "1++");
        parse(PostfixOperationNode.class, "1--");
    }

    @Test
    public void test_unary() {
        parse(UnaryOperationNode.class, "-1++");
        parse(UnaryOperationNode.class, "+1--");
    }

    private <T extends Node> T parse(Class<T> type, String source) {
        try {
            final CommonTokenStream stream = TestUtils.tokenStream(source);
            final GLSLParser parser = TestUtils.parser(stream);
            final ContextVisitor visitor = new ContextVisitor(parserContext);
            Node node = parser.numeric_expression().accept(visitor);
            assertTrue("Expected node type " + type.getSimpleName() + " but got " + node.getClass().getSimpleName(), type.isAssignableFrom(node.getClass()));
            return (T) node;
        } catch (Exception e) {
            for (Token token : TestUtils.getTokens(TestUtils.tokenStream(source))) {
                System.out.println(token);
            }
            throw e;
        }
    }
}
