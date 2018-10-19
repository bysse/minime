package com.tazadum.glsl.parser;

import com.tazadum.glsl.TestUtil;
import com.tazadum.glsl.exception.SourcePositionException;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.util.SourcePosition;
import org.antlr.v4.runtime.ParserRuleContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Created by erikb on 2018-10-18.
 */
class TypeVisitorTest {
    @DisplayName("Test AST Conversion")
    @ParameterizedTest(name = "test {index}: {0}")
    @MethodSource("getSnippets")
    void testAST(String source, SourcePosition sourcePosition) {
        ParserContext parserContext = TestUtil.parserContext();
        ParserRuleContext context = TestUtil.parse(source);

        Node node = TestUtil.ast(context, parserContext);
        assertNotNull(node);

        try {
            TestUtil.typeCheck(node, parserContext);

            if (sourcePosition != null) {
                fail("Expected type problem at " + sourcePosition.format());
            }
        } catch (SourcePositionException e) {
            if (sourcePosition == null) {
                e.printStackTrace();
                fail("Did not expect an exception");
            } else {
                if (!e.getSourcePosition().equals(sourcePosition)) {
                    e.printStackTrace();
                    fail("Position of exception are not matching expectations: expected " + sourcePosition.format() + ", found: " + e.getSourcePosition().format());
                }
            }
        }
    }

    private static Arguments[] getSnippets() {
        return new Arguments[]{
            ok("int a=1;float b=a;"),
            ok("int[] a={1,2,3};float b=a[2];"),
            notOk("float a=1;int b=a;", 1, 16),
        };
    }

    private static Arguments ok(String source) {
        return Arguments.of(source, null);
    }

    private static Arguments notOk(String source, int line, int column) {
        return Arguments.of(source, SourcePosition.create(line, column));
    }
}
