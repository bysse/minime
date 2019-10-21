package com.tazadum.glsl.eval;

import com.tazadum.glsl.eval.type.TypedVariable;

public interface Evaluator {
    Evaluator setVariable(String identifier, TypedVariable typedVariable);

    TypedVariable run();
}
