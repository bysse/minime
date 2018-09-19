package com.tazadum.glsl.preprocesor.model;

/**
 * Comparison operators.
 */
public enum ComparisonOperator implements HasToken {
    LESS_THAN("<"),
    LESS_THAN_EQUALS("<="),
    EQUALS("=="),
    NOT_EQUALS("!="),
    GREATER_THAN_EQUALS(">="),
    GREATER_THAN(">"),;

    private final String token;

    ComparisonOperator(String token) {
        this.token = token;
    }

    @Override
    public String token() {
        return token;
    }
}
