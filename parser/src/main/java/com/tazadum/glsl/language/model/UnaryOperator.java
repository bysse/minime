package com.tazadum.glsl.language.model;

import com.tazadum.glsl.language.HasToken;
import com.tazadum.glsl.parser.GLSLParser;

/**
 * Created by Erik on 2016-10-07.
 */
public enum UnaryOperator implements HasToken {
    INCREASE("++", GLSLParser.INC_OP),
    DECREASE("--", GLSLParser.DEC_OP),
    MINUS("-", GLSLParser.DASH),
    BANG("!", GLSLParser.BANG), // boolean negation
    TILDE("~", GLSLParser.TILDE), // one’s complement operator
    PLUS("+", GLSLParser.PLUS),;

    private final String token;
    private final int tokenId;

    UnaryOperator(String token, int tokenId) {
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
