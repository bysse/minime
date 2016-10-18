package com.tazadum.glsl.exception;

import com.tazadum.glsl.language.GLSLType;
import com.tazadum.glsl.parser.function.FunctionPrototypeMatcher;

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

    public static TypeException missingType() {
        return new TypeException("Missing type definition");
    }

    public static TypeException unknownType(String type) {
        return new TypeException(String.format("Unknown type '%s'", type));
    }

    public static TypeException incompatibleTypes(String type, String otherType) {
        return new TypeException(String.format("Incompatible types %s and %s", type, otherType));
    }

    public static TypeException types(GLSLType type, GLSLType otherType, String message) {
        return new TypeException(String.format("Type '%s' and '%s' %s", type, otherType, message));
    }

    public static TypeException incompatibleTypes(String function, FunctionPrototypeMatcher matcher) {
        return new TypeException(String.format("Can't find any function '%s' that matches '%s'", function, matcher.toString()));
    }

    public TypeException(String message) {
        super(message);
    }

    public TypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
