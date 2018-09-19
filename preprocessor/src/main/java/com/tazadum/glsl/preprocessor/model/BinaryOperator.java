package com.tazadum.glsl.preprocessor.model;

/**
 * Unary operators.
 */
public enum BinaryOperator implements HasToken {
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    MOD("%"),
    LEFT_SHIFT("<<"),
    RIGHT_SHIFT(">>"),
    BITWISE_XOR("^"),
    BITWISE_OR("|"),
    BITWISE_AND("&");

    private final String token;

    BinaryOperator(String token) {
        this.token = token;
    }

    @Override
    public String token() {
        return token;
    }
}
