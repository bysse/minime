package com.tazadum.glsl.exception;

import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.PredefinedType;

import java.util.Objects;

import static java.lang.String.format;

/**
 * Created by erikb on 2018-10-09.
 */
public class Errors {
    public static class Type {
        public static final String NOT_A_CONST_EXPRESSION = "Expected a constant expression.";
        public static final String NON_INTEGER_ARRAY_LENGTH = "Array size must be expressed as an integer.";

        public static String TYPE_DOES_NOT_SUPPORT_PRECISION(String typeName) {
            return format("The type '%s' does not support precision declarations", typeName);
        }

        public static String UNKNOWN_TYPE_ERROR(String details) {
            return "Unknown type error : " + details;
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

        public static String INCOMPATIBLE_OP_TYPES(GLSLType a, GLSLType b) {
            return format("Incompatible operand types '%s' and '%s'.", a.token(), b.token());
        }

        public static String INCOMPATIBLE_OP_TYPES(GLSLType a, GLSLType b, String expected) {
            return format("Incompatible operand types ['%s', '%s'], expected %s.", a.token(), b.token(), expected);
        }

        public static String INCOMPATIBLE_VECTOR_TYPES(PredefinedType a, PredefinedType b) {
            return format("Incompatible vector lengths '%s' and '%s'.", a.token(), b.token());
        }

        public static String INCOMPATIBLE_TYPE(GLSLType type) {
            return format("Incompatible type '%s'", type.token());
        }

        public static String INCOMPATIBLE_TYPE_EXPECTED(GLSLType type, String expectedType) {
            return format("Incompatible type '%s', expected %s", type.token(), expectedType);
        }
    }

    public static class Syntax {
        public static final String EXPECTED_INTEGRAL_CONSTANT_EXPRESSION = "Expected an integral constant expression.";
        public static final String EXPECTED_POSITIVE_INTEGRAL_CONSTANT_EXPRESSION = "Expected a non-negative integral constant expression";
        public static final String STRUCT_DECLARATION_NOT_VALID = "Struct declaration is not valid at this location.";

        public static final String NON_INTEGER_ARRAY_INDEX = "Array indices must be expressed as integers >= 0";
        public static final String INITIALIZER_TOO_SMALL = "Too little data in initialization";
        public static final String INITIALIZER_TOO_BIG = "Too much data in initialization";


        public static String NO_SUCH_FIELD(String fieldName, String typeName) {
            return format("Can't find field '%s' in type '%s'", fieldName, typeName);
        }

        public static String ARRAY_INDEX_OUT_OF_BOUNDS(int size, int index) {
            return format("Array index out of bounds : %d >= %d", index, size);
        }

        public static String LAYOUT_QUALIFIER_ID_NOT_CONST(String qualifierName) {
            return format("The layout qualifier '%s' does not have a constant expression value", qualifierName);
        }

        public static String CANT_RESOLVE_SYMBOL(String identifier) {
            return format("Can't resolve symbol '%s'", identifier);
        }

        public static String FUNCTION_NOT_CONST_EXP(String functionName) {
            return format("Function '%s' can't be used to form constant expressions", functionName);
        }
    }
}
