package com.tazadum.glsl.preprocessor;

public interface Message {
    interface Warning {
        String LINE_CONTINUATION = "Line continuation has trailing whitespace, this is likely an error.";
        String UNRECOGNIZED_PRAGMA = "Unrecognized pragma directive : %s";
        String LINE_NOT_SUPPORTED = "The line directive is not supported and will have no effect.";
    }

    interface Error {
        String MACRO_ARG_MISMATCH = "Number of arguments does not match the macro definition.";
        String VERSION_NOT_FIRST = "The #version directive must occur in a shader before anything else, except for comments and white space.";
        String FILE_NOT_FOUND = "File could not be found : %s";
        String FILE_NOT_OPEN = "File could not be opened : %s";
    }
}
