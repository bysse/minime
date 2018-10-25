package com.tazadum.glsl.parser.listener;

import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TestUtils;
import com.tazadum.glsl.parser.visitor.ContextVisitor;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogicalRelationalContextVisitorTest {
    private ParserContext parserContext;

    @Test
    public void test_void_void() {

    }

    private <T extends Node> T parse(Class<T> type, String source) {
        try {
            final CommonTokenStream stream = TestUtils.tokenStream(source);
            final GLSLParser parser = TestUtils.parser(stream);
            final ContextVisitor visitor = new ContextVisitor(parserContext);
            Node node = parser.numeric_expression().accept(visitor);
            assertTrue(type.isAssignableFrom(node.getClass()), "Expected node type " + type.getSimpleName() + " but got " + node.getClass().getSimpleName());
            return (T) node;
        } catch (Exception e) {
            for (Token token : TestUtils.getTokens(TestUtils.tokenStream(source))) {
                System.out.println(token);
            }
            throw e;
        }
    }

}
