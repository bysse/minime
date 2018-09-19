package com.tazadum.glsl.preprocessor.model;

/**
 * Comparison operators.
 */
public enum RelationalOperator implements HasToken {
    LESS_THAN("<"),
    LESS_THAN_EQUALS("<="),
    EQUALS("=="),
    NOT_EQUALS("!="),
    GREATER_THAN_EQUALS(">="),
    GREATER_THAN(">"),;

    private final String token;

    RelationalOperator(String token) {
        this.token = token;
    }

    @Override
    public String token() {
        return token;
    }
}
