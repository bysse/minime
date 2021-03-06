package com.tazadum.glsl.preprocessor.language;

import com.tazadum.glsl.preprocessor.model.HasToken;

/**
 * Unary operators.
 */
public enum UnaryOperator implements HasToken {
    PLUS("+"),
    NEGATE("-"),
    BITWISE_NOT("~"),
    NOT("!"),
    ;

    private final String token;

    UnaryOperator(String token) {
        this.token = token;
    }

    @Override
    public String token() {
        return token;
    }
}
