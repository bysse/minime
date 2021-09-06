package com.tazadum.glsl.language.model;

import com.tazadum.glsl.language.type.TypeQualifier;
import com.tazadum.glsl.parser.GLSLParser;
import com.tazadum.glsl.preprocessor.model.HasToken;

/**
 * To ensure that a particular output variable is invariant, it is necessary to use the invariant
 * qualifier.
 */
public enum InvariantQualifier implements TypeQualifier, HasToken {
    INVARIANT("invariant", GLSLParser.INVARIANT),
    ;

    private final String token;
    private final int id;

    InvariantQualifier(String token, int id) {
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
