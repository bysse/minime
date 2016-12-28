package com.tazadum.glsl.language;

public enum LogicalOperator implements HasToken {
    AND("&&"),
    XOR("^^"),
    OR("||"),;

    private String token;

    LogicalOperator(String token) {
        this.token = token;
    }

    @Override
    public String token() {
        return token;
    }
}
