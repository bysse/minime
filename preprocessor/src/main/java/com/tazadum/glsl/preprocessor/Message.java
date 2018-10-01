package com.tazadum.glsl.preprocessor;

public interface Message {
    interface Warning {
        String LINE_CONTINUATION = "Line continuation has trailing whitespace, this is likely an error.";
    }

    interface Error {
        String MACRO_ARG_MISMATCH = "Number of arguments does not match the macro definition.";
        String VERSION_NOT_FIRST = "The #version directive must occur in a shader before anything else, except for comments and white space.";
    }
}
