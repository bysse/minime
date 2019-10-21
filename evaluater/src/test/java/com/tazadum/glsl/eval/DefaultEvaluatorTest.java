package com.tazadum.glsl.eval;

import com.tazadum.glsl.eval.type.TypedFloat;
import com.tazadum.glsl.language.type.PredefinedType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultEvaluatorTest {
    @Test
    void testEvaluation() {
        Evaluator evaluator = EvaluatorBuilder.expression("cos(t)")
                .variable("t", PredefinedType.FLOAT)
                .build();

        evaluator.setVariable("t", new TypedFloat(0f));
        //final float value = evaluator.run();

        //assertEquals(1f, value);
    }
}