package com.tazadum.glsl.language;

/**
 * @author erikb
 * @since 2016-07-31
 */
public enum BuiltInType implements HasToken {
    VOID("void"),
    FLOAT("float"),
    INT("int"),
    BOOL("bool"),
    VEC2("vec2"),
    VEC3("vec3"),
    VEC4("vec4"),
    BVEC2("bvec2"),
    BVEC3("bvec3"),
    BVEC4("bvec4"),
    IVEC2("ivec2"),
    IVEC3("ivec3"),
    IVEC4("ivec4"),
    MAT2("mat2"),
    MAT3("mat3"),
    MAT4("mat4"),
    SAMPLER2D("sampler2D"),
    SAMPLERCUBE("samplerCube");

    private String token;

    BuiltInType(String token) {
        this.token = token;
    }

    @Override
    public String getToken() {
        return token;
    }
}
