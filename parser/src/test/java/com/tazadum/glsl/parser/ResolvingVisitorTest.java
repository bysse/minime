package com.tazadum.glsl.parser;

import com.tazadum.glsl.TestUtil;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.util.SourcePosition;
import org.antlr.v4.runtime.ParserRuleContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Created by erikb on 2018-10-16.
 */
class ResolvingVisitorTest {
    @DisplayName("Type resolution")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("getSource")
    void testString(String source, SourcePosition sourcePosition) {
        ParserRuleContext context = TestUtil.parse(source);
        Node node = TestUtil.ast(context);
        try {
            TestUtil.resolve(node, TestUtil.parserContext());

            if (sourcePosition != null) {
                fail("Type resolution should fail at " + sourcePosition.format());
            }
        } catch (Exception e) {
            if (sourcePosition == null) {
                fail("Type resolution failed");
            }
        }
    }

    private static Arguments[] getSource() {
        return new Arguments[]{
            Arguments.of("int a[1+1];", null),
            Arguments.of("int a=1.0;", SourcePosition.create(0, 5)),
        };
    }
}
