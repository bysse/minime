package com.tazadum.glsl.parser.visitor;

import com.tazadum.glsl.TestUtil;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.arithmetic.NumericLeafNode;
import com.tazadum.glsl.language.ast.arithmetic.NumericOperationNode;
import com.tazadum.glsl.language.ast.arithmetic.PostfixOperationNode;
import com.tazadum.glsl.language.ast.arithmetic.PrefixOperationNode;
import com.tazadum.glsl.language.model.NumericOperator;
import com.tazadum.glsl.parser.GLSLParser;
import com.tazadum.glsl.parser.ParserContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author erikb
 * @since 2016-07-31
 */
public class ArithmeticContextVisitorTest {
    private ParserContext parserContext;

    @BeforeEach
    public void setUp() {
        this.parserContext = TestUtil.parserContext();
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
        assertTrue(add.getRight() instanceof NumericLeafNode);

        assertTrue(sub.getLeft() instanceof NumericOperationNode);
        assertTrue(sub.getRight() instanceof NumericLeafNode);

        assertTrue(mul.getLeft() instanceof NumericOperationNode);
        assertTrue(mul.getRight() instanceof NumericLeafNode);

        assertTrue(div.getLeft() instanceof NumericOperationNode);
        assertTrue(div.getRight() instanceof NumericLeafNode);
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
    public void test_prefix() {
        parse(PrefixOperationNode.class, "-1++");
        parse(PrefixOperationNode.class, "+1--");
    }

    private <T extends Node> T parse(Class<T> type, String source) {
        try {

            final ParserRuleContext context = TestUtil.parse(source, GLSLParser::numeric_expression);
            final ParserContext parserContext = TestUtil.parserContext();
            final Node node = TestUtil.ast(context, parserContext);
            assertTrue(type.isAssignableFrom(node.getClass()), "Expected node type " + type.getSimpleName() + " but got " + node.getClass().getSimpleName());
            return (T) node;
        } catch (Exception e) {
            for (Token token : TestUtil.getTokens(TestUtil.tokenStream(source))) {
                System.out.println(token);
            }
            throw e;
        }
    }
}
