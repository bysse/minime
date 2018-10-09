package com.tazadum.glsl.language.model;

import com.tazadum.glsl.language.HasToken;
import com.tazadum.glsl.language.type.TypeQualifier;
import com.tazadum.glsl.parser.GLSLParser;

/**
 * Shader storage blocks, variables declared within shader storage blocks and variables declared as
 * image types (the basic opaque types with “image” in their keyword), can be further qualified with
 * one or more of the following memory qualifiers:
 */
public enum MemoryQualifier implements TypeQualifier, HasToken {
    /**
     * Memory variable where reads and writes are coherent with reads and writes from other
     * shader invocations
     */
    COHERENT("coherent", GLSLParser.COHERENT),

    /**
     * Memory variable whose underlying value may be changed at any point during shader
     * execution by some source other than the current shader invocation
     */
    VOLATILE("volatile", GLSLParser.VOLATILE),

    /**
     * Memory variable where use of that variable is the only way to read and write the underlying
     * memory in the relevant shader stage
     */
    RESTRICT("restrict", GLSLParser.RESTRICT),

    /**
     * Memory variable that can be used to read the underlying memory, but cannot be used to write
     * the underlying memory
     */
    READONLY("readonly", GLSLParser.READONLY),

    /**
     * Memory variable that can be used to write the underlying memory, but cannot be used to read
     * the underlying memory
     */
    WRITEONLY("writeonly", GLSLParser.WRITEONLY),;

    private final String token;
    private final int tokenId;

    MemoryQualifier(String token, int tokenId) {
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

    @Override
    public String toString() {
        return token();
    }
}
