package com.tazadum.glsl.parser;

import com.tazadum.glsl.exception.TypeException;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.PredefinedType;
import com.tazadum.glsl.language.type.TypeCategory;

import static com.tazadum.glsl.exception.Errors.Coarse.INCOMPATIBLE_TYPES;
import static com.tazadum.glsl.exception.Errors.Extras.*;

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

    public static GLSLType compatibleTypeNoException(GLSLType left, GLSLType right) {
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
        return null;
    }

    public static GLSLType compatibleType(GLSLType left, GLSLType right) throws TypeException {
        GLSLType glslType = compatibleTypeNoException(left, right);
        if (glslType == null) {
            throw new TypeException(INCOMPATIBLE_TYPES(left, right, NO_CONVERSION));
        }
        return glslType;
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
                                throw new TypeException(INCOMPATIBLE_TYPES(left, right, VECTOR_DIM_DIFFERENT));
                            }
                            return compatibleType(left, right);
                        case Matrix:
                            // vector * matrix
                            if (left.components() != right.columns()) {
                                throw new TypeException(INCOMPATIBLE_TYPES(left, right, MATRIX_VECTOR_DIM_DIFFERENT));
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
                                throw new TypeException(INCOMPATIBLE_TYPES(left, right, MATRIX_VECTOR_DIM_DIFFERENT));
                            }
                            return right;
                        case Matrix:
                            // matrix * matrix
                            if (left.columns() == right.columns() && left.rows() == right.rows()) {
                                return compatibleType(left, right);
                            }
                            if (left.columns() != right.rows()) {
                                throw new TypeException(INCOMPATIBLE_TYPES(left, right, MATRIX_DIM_DIFFERENT));
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
                            throw new TypeException(INCOMPATIBLE_TYPES(left, right, MATRIX_DIM_DIFFERENT));

                    }
                    break;
            }
        }
        throw new TypeException(INCOMPATIBLE_TYPES(leftType, rightType, NO_CONVERSION));
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
