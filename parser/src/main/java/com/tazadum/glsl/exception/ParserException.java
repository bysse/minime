package com.tazadum.glsl.exception;

/**
 * A checked exception that should be used whenever it's not convenient or makes sense to
 * pass a SourcePosition to an exception.
 */
public class ParserException extends Exception {
    public ParserException(String message) {
        super(message);
    }

    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
