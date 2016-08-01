package com.tazadum.glsl.language;

public enum PrecisionQualifier implements HasToken {
    HIGH_PRECISION("highp"),
    MEDIUM_PRECISION("mediump"),
    LOW_PRECISION("lowp");

    private String token;

    private PrecisionQualifier(String token) {
        this.token = token;
    }

    @Override
    public String token() {
        return token;
    }
}
