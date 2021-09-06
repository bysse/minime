package com.tazadum.glsl.language.model;

import com.tazadum.glsl.language.type.TypeQualifier;
import com.tazadum.glsl.parser.GLSLParser;
import com.tazadum.glsl.preprocessor.model.HasToken;

/**
 * Variable declarations may have at most one storage qualifier specified in front of the type.
 */
public enum StorageQualifier implements TypeQualifier, HasToken {
    /**
     * a variable whose value cannot be changed
     */
    CONST("const", GLSLParser.CONST),
    /**
     * linkage into a shader from a previous stage, variable is copied in.
     * for function parameters passed into a function.
     */
    IN("in", GLSLParser.IN),
    /**
     * linkage out of a shader to a subsequent stage, variable is copied out.
     * for function parameters passed back out of a function, but not initialized for use when passed in.
     */
    OUT("out", GLSLParser.OUT),
    /**
     * For function parameters passed both into and out of a function.
     */
    INOUT("inout", GLSLParser.INOUT),
    /**
     * compatibility profile only and vertex language only; same as in when in a
     * vertex shader
     */
    ATTRIBUTE("attribute", GLSLParser.ATTRIBUTE),
    /**
     * value does not change across the primitive being processed, uniforms
     * form the linkage between a shader, OpenGL, and the application
     */
    UNIFORM("uniform", GLSLParser.UNIFORM),
    /**
     * compatibility profile only and vertex and fragment languages only; same
     * as out when in a vertex shader and same as in when in a fragment shader
     */
    VARYING("varying", GLSLParser.VARYING),
    /**
     * value is stored in a buffer object, and can be read or written both by
     * shader invocations and the OpenGL API
     */
    BUFFER("buffer", GLSLParser.BUFFER),
    /**
     * compute shader only; variable storage is shared across all work items in a
     * local work group
     */
    SHARED("shared", GLSLParser.SHARED),
    /**
     * centroid-based interpolation
     */
    CENTROID("centroid", GLSLParser.CENTROID),
    /**
     * per-sample interpolation
     */
    SAMPLE("sample", GLSLParser.SAMPLE),
    /**
     * per-tessellation-patch attributes
     */
    PATCH("patch", GLSLParser.PATCH),
    /**
     * subroutine
     */
    SUBROUTINE("subroutine", GLSLParser.SUBROUTINE),
    ;

    private final String token;
    private final int id;

    StorageQualifier(String token, int id) {
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
