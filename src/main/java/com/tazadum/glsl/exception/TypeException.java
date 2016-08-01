package com.tazadum.glsl.exception;

public class TypeException extends ParserException {
    public static TypeException unknownError(String details) {
        return new TypeException("Unknown type error : " + details);
    }

    public static TypeException noFields(String type) {
        return new TypeException(String.format("Type '%s' doesn't have any fields", type));
    }

    public static TypeException noSuchField(String type, String field) {
        return new TypeException(String.format("Invalid field '%s' for type '%s'", field, type));
    }


    public TypeException(String message) {
        super(message);
    }

    public TypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
