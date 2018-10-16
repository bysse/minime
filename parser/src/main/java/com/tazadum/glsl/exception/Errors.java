package com.tazadum.glsl.exception;

import com.tazadum.glsl.language.type.PredefinedType;

import java.util.Objects;

import static java.lang.String.format;

/**
 * Created by erikb on 2018-10-09.
 */
public class Errors {
    public static class Type {
        public static final String NOT_A_CONST_EXPRESSION = "Expected a constant expression.";
        public static final String ARRAY_SPECIFIER_NOT_CONSTANT = "Array specifier is not a constant expression";

        public static String UNKNOWN_TYPE_ERROR(String details) {
            return "Unknown type error : " + details;
        }

        public static String NON_INTEGER_ARRAY_LENGTH() {
            return "Array size must be expressed as an integer.";
        }

        public static String NO_SUCH_FIELD(String fieldName, String typeName) {
            return format("Can't find field '%s' in type '%s'", fieldName, typeName);
        }

        public static String ILLEGAL_SWIZZLE(char component) {
            return format("Illegal component '%s' in swizzle", Objects.toString(component));
        }

        public static String UNKNOWN_TYPE(String type) {
            return "Unknown type '" + type + "'";
        }

        public static String CUSTOM_TYPE_NOT_RESOLVED(String typeName) {
            return format("The custom type %s has not been resolved yet", typeName);
        }

        public static String INCOMPATIBLE_OP_TYPES(PredefinedType a, PredefinedType b) {
            return format("Incompatible operand types '%s' and '%s'.", a.token(), b.token());
        }
    }

    public static class Syntax {
        public static String TYPE_DOES_NOT_SUPPORT_PRECISION(String typeName) {
            return format("The type '%s' does not support precision declarations", typeName);
        }
    }
}
