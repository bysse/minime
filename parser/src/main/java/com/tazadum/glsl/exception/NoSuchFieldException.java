package com.tazadum.glsl.exception;

import static com.tazadum.glsl.exception.Errors.Type.NO_SUCH_FIELD;

public class NoSuchFieldException extends ParserException {
    public NoSuchFieldException(String message) {
        super(message);
    }

    public NoSuchFieldException(String fieldName, String typeName) {
        super(NO_SUCH_FIELD(fieldName, typeName));
    }
}
