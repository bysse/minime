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
 * Created by erikb on 2019-03-15.
 */
class SnippetTest {
    @DisplayName("Test AST Conversion")
    @ParameterizedTest(name = "test {index}: {0}")
    @MethodSource("getSnippets")
    void testAST(String source, SourcePosition sourcePosition) {
        ParserContext parserContext = TestUtil.parserContext();
        ParserRuleContext context = TestUtil.parse(source);

        try {
            Node node = TestUtil.ast(context, parserContext);
            assertNotNull(node);

            TestUtil.typeCheck(node, parserContext);

            if (sourcePosition != null) {
                fail("No exception thrown! Expected type problem at " + sourcePosition.format());
            }
        } catch (SourcePositionException e) {
            if (sourcePosition == null) {
                e.printStackTrace();
                fail(e.getSourcePosition().format() + ": Did not expect an exception");
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
                ok("layout(binding=1) buffer P { vec4 pos[]; };void main(){pos[0]=vec4(1);}"),
                ok("layout(binding=1) buffer P { vec4 pos[]; } p;void main(){p.pos[0]=vec4(1);}"),
        };
    }

    private static Arguments ok(String source) {
        return Arguments.of(source, null);
    }

    private static Arguments notOk(String source, int line, int column) {
        return Arguments.of(source, SourcePosition.create(line, column));
    }
}
