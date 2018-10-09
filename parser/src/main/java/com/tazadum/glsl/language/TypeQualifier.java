package com.tazadum.glsl.language;

import com.tazadum.glsl.parser.GLSLParser;

public enum TypeQualifier implements HasToken {
    CONST("const", GLSLParser.CONST),
    IN("in", GLSLParser.IN),
    OUT("out", GLSLParser.OUT),
    INOUT("inout", GLSLParser.INOUT),
    ATTRIBUTE("attribute", GLSLParser.ATTRIBUTE),
    VARYING("varying", GLSLParser.ATTRIBUTE),
    CENTROID("centroid", GLSLParser.CENTROID),
    PATCH("patch", GLSLParser.PATCH),
    SAMPLE("sample", GLSLParser.SAMPLE),
    UNIFORM("uniform", GLSLParser.UNIFORM),
    BUFFER("buffer", GLSLParser.BUFFER),
    SHARED("shared", GLSLParser.SHARED),
    COHERENT("coherent", GLSLParser.COHERENT),
    VOLATILE("volatile", GLSLParser.VOLATILE),
    RESTRICT("restrict", GLSLParser.RESTRICT),
    READONLY("readonly", GLSLParser.READONLY),
    WRITEONLY("writeonly", GLSLParser.WRITEONLY),;

    private final String token;
    private final int tokenId;

    TypeQualifier(String token, int tokenId) {
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
