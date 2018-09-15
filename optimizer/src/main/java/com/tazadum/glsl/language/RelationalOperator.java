package com.tazadum.glsl.language;

public enum RelationalOperator implements HasToken {
    LessThan("<"),
    GreaterThan(">"),
    LessThanOrEqual("<="),
    GreaterThanOrEqual(">="),
    Equal("=="),
    NotEqual("!="),;

    private String token;

    RelationalOperator(String token) {
        this.token = token;
    }

    @Override
    public String token() {
        return token;
    }
}
