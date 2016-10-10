package com.tazadum.glsl.language;

public enum AssignmentOperator implements HasToken {
    EQUAL("="),
    MUL_ASSIGN("*="),
    DIV_ASSIGN("/="),
    ADD_ASSIGN("+="),
    SUB_ASSIGN("-="),
    ;

    private String token;

    AssignmentOperator(String token) {
        this.token = token;
    }

    @Override
    public String token() {
        return token;
    }
}
