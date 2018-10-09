package com.tazadum.glsl.exception;

import com.tazadum.glsl.util.SourcePosition;

public class ParserException extends RuntimeException {
    private SourcePosition sourcePosition;

    public ParserException(SourcePosition sourcePosition, String message) {
        super(message);
        this.sourcePosition = sourcePosition;
    }

    public ParserException(SourcePosition sourcePosition, String message, Throwable cause) {
        super(message, cause);
        this.sourcePosition = sourcePosition;
    }
}
