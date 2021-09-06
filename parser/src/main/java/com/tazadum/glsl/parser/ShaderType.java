package com.tazadum.glsl.parser;

import com.tazadum.glsl.preprocessor.model.HasToken;

/**
 * Created by erikb on 2018-10-22.
 */
public enum ShaderType implements HasToken {
    VERTEX("v", 0),
    FRAGMENT("f", 1),
    GEOMETRY("g", 2),
    COMPUTE("c", 3),
    TESSELLATION_CONTROL("tc", 4),
    TESSELLATION_EVALUATION("te", 5),
    SHADER_TOY("st", 11),
    ;

    private final String token;
    private final int tokenId;

    ShaderType(String token, int tokenId) {
        this.token = token;
        this.tokenId = tokenId;
    }

    public String token() {
        return token;
    }

    public int tokenId() {
        return tokenId;
    }
}
