package com.tazadum.glsl.language.model;

import com.tazadum.glsl.parser.GLSLParser;
import com.tazadum.glsl.preprocessor.model.HasToken;

public enum BitOperator implements HasToken {
    SHIFT_LEFT("&&", GLSLParser.LEFT_OP),
    SHIFT_RIGHT("^^", GLSLParser.RIGHT_OP),
    AND("&", GLSLParser.AMPERSAND),
    OR("|", GLSLParser.VERTICAL_BAR),
    XOR("^", GLSLParser.CARET),;

    private final String token;
    private final int tokenId;

    BitOperator(String token, int tokenId) {
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
