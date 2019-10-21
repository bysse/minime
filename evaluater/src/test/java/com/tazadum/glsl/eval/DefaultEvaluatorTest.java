package com.tazadum.glsl.eval;

import com.tazadum.glsl.eval.type.TypedFloat;
import com.tazadum.glsl.eval.type.TypedVariable;
import com.tazadum.glsl.language.type.PredefinedType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DefaultEvaluatorTest {
    @Test
    void testVariableOnly() {
        Evaluator evaluator = EvaluatorBuilder.expression("t")
                .variable("t", PredefinedType.FLOAT)
                .build();

        evaluator.setVariable("t", new TypedFloat(1f));
        final TypedVariable value = evaluator.run();

        assertNotNull(value);
        assertEquals(PredefinedType.FLOAT, value.getType());
        assertEquals(1f, (Float)value.getValue());
    }

    @Test
    void testEvaluation() {
        Evaluator evaluator = EvaluatorBuilder.expression("cos(t)+1")
                .variable("t", PredefinedType.FLOAT)
                .build();

        evaluator.setVariable("t", new TypedFloat(0f));

        final TypedVariable value = evaluator.run();

        assertNotNull(value);
        assertEquals(PredefinedType.FLOAT, value.getType());
        assertEquals(2f, (Float)value.getValue());
    }
}