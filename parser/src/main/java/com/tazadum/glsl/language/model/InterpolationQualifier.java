package com.tazadum.glsl.language.model;

import com.tazadum.glsl.language.type.TypeQualifier;
import com.tazadum.glsl.parser.GLSLParser;
import com.tazadum.glsl.preprocessor.model.HasToken;

/**
 * Inputs and outputs that could be interpolated can be further qualified by at most one of the following
 * interpolation qualifiers
 */
public enum InterpolationQualifier implements TypeQualifier, HasToken {
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

    @Override
    public String toString() {
        return token();
    }
}
