package com.tazadum.glsl.language;

public enum TypeQualifier implements HasToken {
    CONST("const"),
    ATTRIBUTE("attribute"),
    VARYING("varying"),
    INVARIANT_VARYING("invariant varying"),
    UNIFORM("uniform"),
    IN("in"),
    OUT("out"),
    INOUT("inout");

    private String token;

    TypeQualifier(String token) {
        this.token = token;
    }

    @Override
    public String token() {
        return token;
    }
}
