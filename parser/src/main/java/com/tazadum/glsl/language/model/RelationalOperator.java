package com.tazadum.glsl.language.model;

import com.tazadum.glsl.language.HasToken;
import com.tazadum.glsl.parser.GLSLParser;

public enum RelationalOperator implements HasToken {
    LessThan("<", GLSLParser.LEFT_ANGLE),
    GreaterThan(">", GLSLParser.RIGHT_ANGLE),
    LessThanOrEqual("<=", GLSLParser.LE_OP),
    GreaterThanOrEqual(">=", GLSLParser.GE_OP),
    Equal("==", GLSLParser.EQUAL),
    NotEqual("!=", GLSLParser.NE_OP),;

    private final String token;
    private final int tokenId;

    RelationalOperator(String token, int tokenId) {
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
