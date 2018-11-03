package com.tazadum.glsl.cli.options;

import com.tazadum.glsl.preprocessor.model.HasToken;

/**
 * Created by erikb on 2018-10-26.
 */
public enum OutputFormat implements HasToken {
    PLAIN("plain"),
    SHADERTOY("shadertoy"),
    C_HEADER("c"),;

    private final String token;

    OutputFormat(String token) {
        this.token = token;
    }

    public String token() {
        return token;
    }
}
