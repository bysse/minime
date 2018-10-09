package com.tazadum.glsl.language.model;

import com.tazadum.glsl.language.HasToken;
import com.tazadum.glsl.parser.GLSLParser;

public enum LogicalOperator implements HasToken {
    AND("&&", GLSLParser.AND_OP),
    XOR("^^", GLSLParser.XOR_OP),
    OR("||", GLSLParser.OR_OP),;

    private final String token;
    private final int tokenId;

    LogicalOperator(String token, int tokenId) {
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
