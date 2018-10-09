package com.tazadum.glsl.language.model;

import com.tazadum.glsl.language.HasToken;
import com.tazadum.glsl.parser.GLSLParser;

/**
 * Any single-precision floating-point declaration, integer declaration, or sampler declaration can have the
 * type preceded by one of these precision qualifiers.
 */
public enum PrecisionQualifier implements TypeQualifier, HasToken {
    HIGH_PRECISION("highp", GLSLParser.HIGH_PRECISION),
    MEDIUM_PRECISION("mediump", GLSLParser.MEDIUM_PRECISION),
    LOW_PRECISION("lowp", GLSLParser.LOW_PRECISION);

    private final String token;
    private final int id;

    PrecisionQualifier(String token, int id) {
        this.token = token;
        this.id = id;
    }

    @Override
    public String token() {
        return token;
    }

    @Override
    public int tokenId() {
        return id;
    }

    @Override
    public String toString() {
        return token();
    }
}
