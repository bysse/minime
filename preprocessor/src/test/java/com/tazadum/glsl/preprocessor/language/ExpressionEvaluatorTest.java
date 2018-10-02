package com.tazadum.glsl.preprocessor.language;

import com.tazadum.glsl.preprocessor.LogKeeper;
import com.tazadum.glsl.preprocessor.PreprocessorException;
import com.tazadum.glsl.preprocessor.language.ast.flow.IfFlowNode;
import com.tazadum.glsl.preprocessor.model.ExpressionEvaluator;
import com.tazadum.glsl.preprocessor.model.MacroRegistry;
import com.tazadum.glsl.preprocessor.parser.TestUtil;
import com.tazadum.glsl.util.SourcePosition;
import com.tazadum.glsl.util.SourcePositionId;
import org.antlr.v4.runtime.ParserRuleContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static com.tazadum.glsl.preprocessor.model.BoolIntLogic.isTrue;
import static org.junit.jupiter.api.Assertions.*;

class ExpressionEvaluatorTest {
    private MacroRegistry registry;
    private ExpressionEvaluator evaluator;

    @BeforeEach
    public void globalSetup() {
        registry = new MacroRegistry();
        registry.define("EXIST", "1");
        evaluator = new ExpressionEvaluator(registry);
    }

    @ParameterizedTest(name = "True #{index} : {0}")
    @MethodSource("getTrueExpressions")
    void testTrueExpressions(String source) {
        IfFlowNode node = parse(IfFlowNode.class, "#if " + source);
        int value = node.getExpression().accept(evaluator);
        assertTrue(isTrue(value));
    }

    private static String[] getTrueExpressions() {
        return new String[]{"1", "2", "-1+2", "(1<<2) - 4 || 1", "1 < 5 && 5 > 1", "defined(EXIST)"};
    }

    @ParameterizedTest(name = "False #{index} : {0}")
    @MethodSource("getFalseExpressions")
    void testFalseExpressions(String source) {
        IfFlowNode node = parse(IfFlowNode.class, "#if " + source);
        int value = node.getExpression().accept(evaluator);
        assertFalse(isTrue(value));
    }

    private static String[] getFalseExpressions() {
        return new String[]{"0", "1-1", "(1<<2) - 4", "1 == (3*1)", "defined(NOT_EXIST)"};
    }

    @Test
    @DisplayName("Things that should fail")
    void testFailuresExpressions() {
        assertThrows(PreprocessorException.class, () -> {
            IfFlowNode node = parse(IfFlowNode.class, "#if 1 == EXIST");
            int value = node.getExpression().accept(evaluator);
        }, "Symbols are not allowed after macro substitution");
    }

    private <T extends Node> T parse(Class<T> type, String source) {
        ParserRuleContext context = TestUtil.parse(source);

        Node node = context.accept(new PreprocessorVisitor(SourcePositionId.create("test", SourcePosition.TOP), new LogKeeper()));
        assertNotNull(node, "Resulting node should not be null");

        if (!type.isAssignableFrom(node.getClass())) {
            fail("The resulting AST node is not of type " + type.getSimpleName() + " it's of type " + node.getClass().getSimpleName());
        }
        return (T) node;
    }
}
