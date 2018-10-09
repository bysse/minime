package com.tazadum.glsl.language;

import com.tazadum.glsl.parser.GLSLParser;

public enum AssignmentOperator implements HasToken {
    EQUAL("=", GLSLParser.EQUAL),
    MUL_ASSIGN("*=", GLSLParser.MUL_ASSIGN),
    DIV_ASSIGN("/=", GLSLParser.DIV_ASSIGN),
    ADD_ASSIGN("+=", GLSLParser.ADD_ASSIGN),
    SUB_ASSIGN("-=", GLSLParser.SUB_ASSIGN),;

    private final String token;
    private final int tokenId;

    AssignmentOperator(String token, int tokenId) {
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
