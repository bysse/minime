package com.tazadum.glsl.language;

public enum LogicalOperator implements HasToken {
    AND_OP("&&"),
    XOR_OP("^^"),
    OR_OP("||"),
    ;

    private String token;

    LogicalOperator(String token) {
        this.token = token;
    }

    @Override
    public String token() {
        return token;
    }
}
