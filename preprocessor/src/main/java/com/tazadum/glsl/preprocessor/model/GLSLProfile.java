package com.tazadum.glsl.preprocessor.model;

/**
 * Until version 3.0, all versions of OpenGL were fully backwards compatible with earlier ones.
 * Code written against OpenGL 1.1 could execute just fine on OpenGL 2.1 implementations.
 * OpenGL version 3.0 introduced the idea of deprecating functionality. Many OpenGL functions
 * were declared deprecated, which means that users should avoid using them because they may be
 * removed from later API versions. OpenGL 3.1 removed almost all of the functionality deprecated
 * in OpenGL 3.0. This includes the Fixed Function Pipeline. However, since many implementations
 * support the deprecated and removed features anyway, some implementations want to be able to
 * provide a way for users of higher GL versions to gain access to the old APIs. Several
 * techniques were tried, and it has settled down into a division between Core and Compatibility
 * contexts.
 */
public enum GLSLProfile implements HasToken {
    CORE("core"),
    COMPATIBILITY("compatibility"),
    ES("es");

    private String token;

    GLSLProfile(String token) {
        this.token = token;
    }

    public String token() {
        return token;
    }
}
