package com.tazadum.glsl.preprocessor.language;

/**
 * All different types of preprocessor declarations
 */
public enum DeclarationType {
    NO_OP,
    EXTENSION,
    VERSION,
    LINE,
    PRAGMA,
    PRAGMA_INCLUDE,
    IF,
    IF_DEFINED,
    IF_NOT_DEFINED,
    ELSE,
    ELSE_IF,
    END_IF,
    UNDEF,
    DEFINE, ERROR;
}
