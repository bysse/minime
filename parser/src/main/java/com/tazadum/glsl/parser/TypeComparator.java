package com.tazadum.glsl.parser;

import com.tazadum.glsl.exception.BadImplementationException;
import com.tazadum.glsl.exception.NoSuchFieldException;
import com.tazadum.glsl.exception.TypeException;
import com.tazadum.glsl.language.ast.type.ArraySpecifier;
import com.tazadum.glsl.language.model.ArraySpecifiers;
import com.tazadum.glsl.language.type.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.tazadum.glsl.exception.Errors.Coarse.INCOMPATIBLE_TYPES;
import static com.tazadum.glsl.exception.Errors.Extras.*;
import static com.tazadum.glsl.language.type.TypeCategory.Matrix;
import static com.tazadum.glsl.language.type.TypeCategory.Vector;
import static com.tazadum.glsl.parser.TypeCombination.ofCategory;

/**
 * Created by erikb on 2018-10-20.
 */
public class TypeComparator {
    public static GLSLType checkAndTransfer(GLSLType leftType, ArraySpecifiers leftArraySpecifier, GLSLType rightType, ArraySpecifiers rightSpecifiers) throws TypeException {
        if (leftArraySpecifier == null || leftArraySpecifier.getSpecifiers().isEmpty()) {
            return checkAndTransfer(leftType, Collections.emptyList(), rightType, rightSpecifiers);
        }

        final List<ArraySpecifier> specifierList = new ArrayList<>(leftArraySpecifier.getSpecifiers());
        return checkAndTransfer(leftType, specifierList, rightType, rightSpecifiers);
    }

    private static GLSLType checkAndTransfer(GLSLType leftType, List<ArraySpecifier> leftSpecifiers, GLSLType rightType, ArraySpecifiers rightSpecifiers) throws TypeException {
        ArraySpecifier activeSpecifier = null;
        if (!(leftType instanceof ArrayType) && !leftSpecifiers.isEmpty()) {
            activeSpecifier = leftSpecifiers.remove(0);
            leftType = activeSpecifier.transform(leftType);
        }

        if (leftType instanceof ArrayType) {
            if (!(rightType instanceof ArrayType)) {
                throw new TypeException(INCOMPATIBLE_TYPES(leftType, rightType, NO_CONVERSION));
            }

            ArrayType rightArray = (ArrayType) rightType;
            ArrayType leftArray = (ArrayType) leftType;

            if (!rightArray.hasDimension()) {
                throw new BadImplementationException("Initializers should always have a dimension");
            }

            final int dimension = rightArray.getDimension();
            if (leftArray.hasDimension()) {
                // verify the array sizes
                if (leftArray.getDimension() < rightArray.getDimension()) {
                    throw new TypeException(INCOMPATIBLE_TYPES(leftArray, rightArray, INITIALIZER_TOO_BIG));
                }
                if (leftArray.getDimension() > rightArray.getDimension()) {
                    throw new TypeException(INCOMPATIBLE_TYPES(leftArray, rightArray, INITIALIZER_TOO_SMALL));
                }
            }

            GLSLType type = checkAndTransfer(leftArray.baseType(), leftSpecifiers, rightArray.baseType(), rightSpecifiers);
            if (activeSpecifier == null) {
                return new ArrayType(type, rightArray.getDimension());
            }

            rightSpecifiers.addSpecifier(new ArraySpecifier(activeSpecifier.getSourcePosition(), dimension));
            return type;
        }

        if (activeSpecifier == null ) {
            if (ofCategory(Vector, leftType) && rightType instanceof ArrayType && rightSpecifiers.isEmpty()) {
                // handle array initialization of vector declarations
                return checkArrayInitializerSizeMatch((PredefinedType)leftType, (ArrayType)rightType);
            }

            if (ofCategory(Matrix, leftType) && rightType instanceof ArrayType && rightSpecifiers.isEmpty()) {
                // handle array initialization of matrix declarations
                final PredefinedType matrixType = (PredefinedType)leftType;
                final ArrayType rightArray = (ArrayType) rightType;

                // check that the number of columns in the matrix matches the array size
                if (matrixType.columns() < rightArray.getDimension()) {
                    throw new TypeException(INCOMPATIBLE_TYPES(matrixType, rightArray, INITIALIZER_MATRIX_TOO_BIG));
                }
                if (matrixType.columns() > rightArray.getDimension()) {
                    throw new TypeException(INCOMPATIBLE_TYPES(matrixType, rightArray, INITIALIZER_MATRIX_TOO_SMALL));
                }

                // at this point the number of columns matches the number of elements in the initializer array
                // check that the column type can be initialized by the base type of the array
                checkAndTransfer(matrixType.columnType(), Collections.emptyList(), rightArray.baseType(), rightSpecifiers);

                return leftType;
            }
        }

        // struct comparison are supported in this clause
        if (!leftType.isAssignableBy(rightType)) {
            throw new TypeException(INCOMPATIBLE_TYPES(leftType, rightType, NO_CONVERSION));
        }

        // all good
        return leftType;
    }

    private static GLSLType checkArrayInitializerSizeMatch(PredefinedType vectorType, ArrayType rightType) throws TypeException {
        // verify the array initializer size
        if (!rightType.hasDimension()) {
            throw new TypeException(INCOMPATIBLE_TYPES(vectorType, rightType, INITIALIZER_HAS_UNKNOWN_SIZE));
        }

        if (vectorType.components() < rightType.getDimension()) {
            throw new TypeException(INCOMPATIBLE_TYPES(vectorType, rightType, INITIALIZER_TOO_BIG));
        }

        if (vectorType.components() > rightType.getDimension()) {
            throw new TypeException(INCOMPATIBLE_TYPES(vectorType, rightType, INITIALIZER_TOO_SMALL));
        }

        if (!PredefinedType.isAssignableBy(vectorType.baseType(), rightType.baseType())) {
            throw new TypeException(INCOMPATIBLE_TYPES(vectorType, rightType, NO_CONVERSION));
        }

        return vectorType;
    }

    public static boolean isAssignable(StructType structType, GLSLType type) {
        if (structType == type) {
            return true;
        }

        if (type instanceof ArrayType) {
            ArrayType other = (ArrayType) type;

            if (!other.hasDimension() || other.getDimension() != structType.components()) {
                // the sizes are different or non-specified
                return false;
            }
            // all fields in the structure has compatible types and is compatible with the array
            return PredefinedType.isAssignableBy(structType.baseType(), other.baseType());
        }

        if (type instanceof StructType) {
            StructType other = (StructType) type;

            if (structType.components() != other.components()) {
                return false;
            }

            try {
                for (int i = 0; i < structType.components(); i++) {
                    GLSLType structField = structType.fieldType(i);
                    GLSLType initField = other.fieldType(i);
                    if (!structField.isAssignableBy(initField)) {
                        return false;
                    }
                }
                return true;
            } catch (NoSuchFieldException e) {
                return false;
            }
        }

        // some other type
        return false;
    }
}
