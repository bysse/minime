package com.tazadum.glsl.language;

public enum RelationalOperator implements HasToken {
    LEFT_ANGLE("<"),
    RIGHT_ANGLE(">"),
    LE("<="),
    GE_OP(">="),
    EQ_OP("=="),
    NE_OP("!="),
    ;

    private String token;

    RelationalOperator(String token) {
        this.token = token;
    }

    @Override
    public String token() {
        return token;
    }
}
