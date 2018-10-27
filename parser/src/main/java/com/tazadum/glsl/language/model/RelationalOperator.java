package com.tazadum.glsl.language.model;

import com.tazadum.glsl.language.type.Numeric;
import com.tazadum.glsl.parser.GLSLParser;
import com.tazadum.glsl.preprocessor.model.HasToken;

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

    public Boolean apply(Numeric a, Numeric b) {
        final int compVal = a.getValue().compareTo(b.getValue());

        switch (this) {
            case Equal:
                return compVal == 0;
            case NotEqual:
                return compVal != 0;
            case GreaterThan:
                return compVal > 0;
            case GreaterThanOrEqual:
                return compVal >= 0;
            case LessThan:
                return compVal < 0;
            case LessThanOrEqual:
                return compVal <= 0;
        }

        throw new UnsupportedOperationException("Unsupported operation " + name());
    }
}
