package com.tazadum.glsl.language;

import com.tazadum.glsl.parser.GLSLParser;

/**
 * Inputs and outputs that could be interpolated can be further qualified by at most one of the following
 * interpolation qualifiers
 */
public enum InterpolationQualifier implements HasToken {
    /**
     * perspective correct interpolation
     */
    SMOOTH("smooth", GLSLParser.SMOOTH),
    /**
     * no interpolation
     */
    FLAT("flat", GLSLParser.FLAT),
    /**
     * linear interpolation
     */
    NO_PERSPECTIVE("noperspective", GLSLParser.NOPERSPECTIVE);

    private final String token;
    private final int id;

    InterpolationQualifier(String token, int id) {
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
}
