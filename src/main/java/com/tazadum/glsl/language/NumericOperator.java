package com.tazadum.glsl.language;

public enum NumericOperator implements HasToken {
    MUL("*"),
    DIV("/"),
    ADD("+"),
    SUB("-"),;

    private String token;

    NumericOperator(String token) {
        this.token = token;
    }

    @Override
    public String token() {
        return token;
    }
}
