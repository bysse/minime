package com.tazadum.glsl.exception;

public class ParserException extends RuntimeException {
    public static ParserException notSupported(String feature) {
        return new ParserException(String.format("'%s' is not supported", feature));
    }

    public ParserException(String message) {
        super(message);
    }

    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
