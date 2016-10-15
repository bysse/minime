package com.tazadum.glsl.language;

/**
 * Created by Erik on 2016-10-07.
 */
public enum UnaryOperator implements HasToken {
    INCREASE("++"),
    DECREASE("--"),
    MINUS("-"),
    BANG("!"),
    PLUS("+")
    ;

    private String token;

    UnaryOperator(String token) {
        this.token = token;
    }

    @Override
    public String token() {
        return token;
    }

}
