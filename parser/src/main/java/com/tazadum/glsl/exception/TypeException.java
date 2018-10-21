package com.tazadum.glsl.exception;

public class TypeException extends ParserException {
    public TypeException(String message) {
        super(message);
    }

    public TypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
