package com.tazadum.glsl.parser;

import com.tazadum.glsl.TestUtil;
import com.tazadum.glsl.language.type.Numeric;
import org.antlr.v4.runtime.ParserRuleContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by erikb on 2018-10-16.
 */
class ConstExpressionEvaluatorVisitorTest {
    @Test
    void test() {
        ParserRuleContext context = TestUtil.parse("1+3", GLSLParser::constant_expression);
        Numeric result = TestUtil.ast(context, TestUtil.parserContext()).accept(new ConstExpressionEvaluatorVisitor());
        assertEquals(4, result.getValue());
    }
}
