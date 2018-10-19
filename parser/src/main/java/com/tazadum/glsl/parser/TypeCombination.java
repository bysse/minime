package com.tazadum.glsl.parser;

import com.tazadum.glsl.exception.Errors;
import com.tazadum.glsl.exception.TypeException;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.PredefinedType;
import com.tazadum.glsl.language.type.TypeCategory;

/**
 * Created by erikb on 2018-10-18.
 */
public class TypeCombination {
    public static final GLSLType[] INTEGER_TYPES = new GLSLType[]{
        PredefinedType.INT,
        PredefinedType.UINT,
        PredefinedType.IVEC2,
        PredefinedType.IVEC3,
        PredefinedType.IVEC4,
        PredefinedType.UVEC2,
        PredefinedType.UVEC3,
        PredefinedType.UVEC4
    };

    public static GLSLType compatibleType(GLSLType left, GLSLType right) throws TypeException {
        if (left == right || left.isAssignableBy(right)) {
            return left;
        }

        if (left instanceof PredefinedType && right instanceof PredefinedType) {
            final PredefinedType[] leftConversions = ((PredefinedType) left).getTypeConversion();
            for (PredefinedType conversion : leftConversions) {
                if (conversion == right) {
                    return right;
                }
            }

            final PredefinedType[] rightConversions = ((PredefinedType) right).getTypeConversion();
            for (PredefinedType conversion : rightConversions) {
                if (conversion == left) {
                    return left;
                }
            }
        }

        throw new TypeException(Errors.Type.INCOMPATIBLE_OP_TYPES(left, right));
    }

    public static GLSLType arithmeticResult(GLSLType leftType, GLSLType rightType) throws TypeException {
        if (leftType instanceof PredefinedType && rightType instanceof PredefinedType) {
            PredefinedType left = (PredefinedType) leftType;
            PredefinedType right = (PredefinedType) rightType;

            switch (left.category()) {
                case Scalar:
                    switch (right.category()) {
                        case Scalar:
                            return compatibleType(left, right);
                        case Vector:
                            return right;
                        case Matrix:
                            return right;
                    }
                    break;
                case Vector:
                    switch (right.category()) {
                        case Scalar:
                            return left;
                        case Vector:
                            if (left.components() != right.components()) {
                                throw new TypeException(Errors.Type.INCOMPATIBLE_VECTOR_TYPES(left, right));
                            }
                            return compatibleType(left, right);
                        case Matrix:
                            // vector * matrix
                            if (left.components() != right.columns()) {
                                throw new TypeException(Errors.Type.INCOMPATIBLE_MATRIX_TYPES(left, right));
                            }
                            return left;
                    }
                    break;
                case Matrix:
                    switch (right.category()) {
                        case Scalar:
                            return left;
                        case Vector:
                            // matrix * vector
                            if (left.columns() != right.components()) {
                                throw new TypeException(Errors.Type.INCOMPATIBLE_MATRIX_TYPES(left, right));
                            }
                            return right;
                        case Matrix:
                            // matrix * matrix
                            if (left.columns() == right.columns() && left.rows() == right.rows()) {
                                return compatibleType(left, right);
                            }
                            if (left.columns() != right.rows()) {
                                throw new TypeException(Errors.Type.INCOMPATIBLE_MATRIX_TYPES(left, right));
                            }

                            // find a matrix type with dimensions left.columns() x right.rows()
                            GLSLType baseType = compatibleType(left.baseType(), right.baseType());
                            for (PredefinedType type : PredefinedType.values()) {
                                if (type.category() != TypeCategory.Matrix) {
                                    continue;
                                }
                                if (type.baseType() == baseType && type.columns() == left.columns() && type.rows() == right.rows()) {
                                    return type;
                                }
                            }
                            throw new TypeException(Errors.Type.INCOMPATIBLE_MATRIX_TYPES(left, right));

                    }
                    break;
            }
        }
        throw new TypeException(Errors.Type.INCOMPATIBLE_OP_TYPES(leftType, rightType));
    }

    public static boolean ofCategory(TypeCategory typeCategory, GLSLType... types) {
        for (GLSLType type : types) {
            if (type instanceof PredefinedType && ((PredefinedType) type).category() == typeCategory) {
                continue;
            }
            return false;
        }
        return true;
    }

    public static boolean ofAnyCategory(GLSLType target, TypeCategory... typeCategories) {
        if (target instanceof PredefinedType) {
            TypeCategory targetCategory = ((PredefinedType) target).category();
            for (TypeCategory category : typeCategories) {
                if (targetCategory == category) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean allOf(GLSLType targetType, GLSLType... types) {
        for (GLSLType type : types) {
            if (targetType == type) {
                continue;
            }
            return false;
        }
        return true;
    }

    public static boolean anyOf(GLSLType type, GLSLType... targetTypes) {
        for (GLSLType targetType : targetTypes) {
            if (targetType == type) {
                return true;
            }
        }
        return false;
    }

    public static boolean sameSize(GLSLType a, GLSLType b) {
        if (a instanceof PredefinedType && b instanceof PredefinedType) {
            return ((PredefinedType) a).components() == ((PredefinedType) b).components();
        }
        return false;
    }
}
