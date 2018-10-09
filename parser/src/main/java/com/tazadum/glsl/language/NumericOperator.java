package com.tazadum.glsl.language;

import com.tazadum.glsl.parser.GLSLParser;

public enum NumericOperator implements HasToken {
    MUL("*", GLSLParser.STAR),
    DIV("/", GLSLParser.SLASH),
    ADD("+", GLSLParser.PLUS),
    SUB("-", GLSLParser.DASH),;

    private final String token;
    private final int tokenId;

    NumericOperator(String token, int tokenId) {
        this.token = token;
        this.tokenId = tokenId;
    }

    @Override
    public String token() {
        return token;
    }


    @Override
    public int tokenId() {
        return tokenId;
    }
}
