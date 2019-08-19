package com.tazadum.glsl.exception;

import com.tazadum.glsl.language.type.GLSLType;

import java.util.Objects;

import static java.lang.String.format;

/**
 * Created by erikb on 2018-10-09.
 */
public class Errors {
    public static class Coarse {
        public static String NOT_SUPPORTED(String what) {
            return "Not Supported! " + what;
        }

        public static String UNKNOWN_SYMBOL(String identifier) {
            return UNKNOWN_SYMBOL(identifier, null);
        }

        public static String UNKNOWN_SYMBOL(String identifier, Extras extras) {
            return format("Unknown symbol '%s'.", identifier) + Extras.format(extras);
        }

        public static String UNKNOWN_FUNCTION(String identifier, Extras extras) {
            return format("Unknown function '%s'.", identifier) + Extras.format(extras);
        }

        public static String NO_SUCH_FIELD(String fieldName) {
            return format("No such field '%s'.", fieldName);
        }

        public static String NO_SUCH_FIELD(String fieldName, String type) {
            return format("No such field '%s' in type '%s'.", fieldName, type);
        }

        public static String ILLEGAL_SWIZZLE(char component) {
            return ILLEGAL_SWIZZLE(Objects.toString(component));
        }

        public static String ILLEGAL_SWIZZLE(String component) {
            return format("Illegal swizzle '%s'", component);
        }

        public static String SYNTAX_ERROR(Extras extras) {
            return "Syntax error." + Extras.format(extras);
        }

        public static String SYNTAX_ERROR(String near, Extras extras) {
            return format("Syntax error near '%s'.", near) + Extras.format(extras);
        }

        public static String NOT_CONST_EXPRESSION(Extras extras) {
            return "Not a const expression." + Extras.format(extras);
        }

        public static String FUNCTION_NOT_CONST_EXPRESSION(String functionName, Extras extras) {
            return format("Function '%s' can't be used to form a constant expression", functionName) + Extras.format(extras);
        }

        public static String INCOMPATIBLE_TYPES(GLSLType a, GLSLType b, Extras extras) {
            return format("The types '%s' <- '%s' are not compatible.", a.token(), b.token()) + Extras.format(extras);
        }

        public static String INCOMPATIBLE_TYPE(GLSLType a, Extras extras) {
            return format("Invalid type '%s'.", a.token()) + Extras.format(extras);
        }
    }

    public enum Extras {
        NO_CONVERSION("No acceptable type conversion could be found."),
        NO_MATCHING_FUNCTION_FOUND("No matching function prototype found."),

        EXPECTED_BOOL("Expected operands of type bool."),
        EXPECTED_INTEGER_SCALAR("Expected operands of type int or uint."),
        EXPECTED_INTEGERS("Expected operands of type int, uint, ivec*, uvec*."),
        EXPECTED_NON_NEGATIVE_INTEGER("Expected operand to be a non-negative integer of type int or uint."),

        EXPECTED_SCALAR("Expected operands of scalar type"),
        EXPECTED_NON_OPAQUE("Expected operands of non-opaque type scalar, vector or matrix."),
        EXPECTED_RETURN("Expected return"),

        NOT_INDEXABLE("The expression is not of type array or matrix."),
        ARRAY_INDEX_NOT_INT("Array indices must be expressed as a non-negative integer of type int or uint."),
        ARRAY_INDEX_OUT_OF_BOUNDS("Array indices out of bounds.", "%d >= %d"),
        MATRIX_INDEX_NOT_INT("Matrix column indices must be expressed as a non-negative integer of type int or uint."),
        VECTOR_INDEX_NOT_INT("Vector row indices must be expressed as a non-negative integer of type int or uint."),

        INVALID_STRUCT_DECLARATION("Struct declaration is not valid at this location."),
        INVALID_SWIZZLE("No valid swizzle type could be created."),
        INVALID_SWIZZLE_FOR_TYPE("Type does not have any fields."),
        INVALID_SWIZZLE_SCALAR("Swizzle operations not allowed on scalar types."),
        RECURSION_NOT_SUPPORTED("Function call causes a recursion loop. This is not supported in GLSL."),
        LVALUE_IS_CONST("Lvalue is const and can't be assigned."),

        PRECISION_NOT_SUPPORTED("The type does not support precision declarations."),

        INITIALIZER_TOO_SMALL("Too little data in initialization."),
        INITIALIZER_TOO_BIG("Too much data in initialization."),
        INITIALIZER_MATRIX_TOO_SMALL("Too few columns in initialization."),
        INITIALIZER_MATRIX_TOO_BIG("Too many columns in initialization."),
        INITIALIZER_ON_PARAMETER("Parameters are not allowed to have initializers."),
        INITIALIZER_HAS_UNKNOWN_SIZE("The size of the initializer could not be determined."),

        OPAQUE_TYPE_LVALUE("Opaque types cannot be treated as l-values."),
        QUALIFIER_BOTH_IN_AND_OUT("A variable cannot be declared with both the in and the out qualifiers."),

        VECTOR_DIM_DIFFERENT("Vector lengths are different."),
        MATRIX_VECTOR_DIM_DIFFERENT("Vector and matrix dimensions are wrong for this operation."),
        MATRIX_DIM_DIFFERENT("Matrix dimensions are wrong for this operation."),

        TERNARY_TYPES_NOT_COMPATIBLE("Each branch in a ternary expression must have compatible types."),;

        public static String format(Extras extras) {
            if (extras == null) {
                return "";
            }
            return " " + extras.getMessage();
        }

        private final String message;
        private final String details;

        Extras(String message) {
            this(message, null);
        }

        Extras(String message, String details) {
            this.message = message;
            this.details = details;
        }

        public String getMessage() {
            return message;
        }

        public String details(Object... args) {
            return " : " + String.format(details, args);
        }
    }
}
