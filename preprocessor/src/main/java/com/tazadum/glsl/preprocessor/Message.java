package com.tazadum.glsl.preprocessor;

public interface Message {
    interface Warning {
        String LINE_CONTINUATION = "Line continuation has trailing whitespace, this is likely an error.";
    }
}
