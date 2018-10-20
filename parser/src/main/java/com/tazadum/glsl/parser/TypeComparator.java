package com.tazadum.glsl.parser;

import com.tazadum.glsl.exception.BadImplementationException;
import com.tazadum.glsl.exception.TypeException;
import com.tazadum.glsl.language.ast.type.ArraySpecifier;
import com.tazadum.glsl.language.model.ArraySpecifiers;
import com.tazadum.glsl.language.type.ArrayType;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.StructType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.tazadum.glsl.exception.Errors.Coarse.INCOMPATIBLE_TYPES;
import static com.tazadum.glsl.exception.Errors.Extras.*;

/**
 * Created by erikb on 2018-10-20.
 */
public class TypeComparator {
    public static GLSLType checkAndTransfer(GLSLType source, ArraySpecifiers sourceSpecifiers, GLSLType targetType, ArraySpecifiers targetArraySpecifier) throws TypeException {
        if (targetArraySpecifier == null || targetArraySpecifier.getSpecifiers().isEmpty()) {
            return checkAndTransfer(source, sourceSpecifiers, targetType, Collections.emptyList());
        }

        final List<ArraySpecifier> specifierList = new ArrayList<>(targetArraySpecifier.getSpecifiers());
        return checkAndTransfer(source, sourceSpecifiers, targetType, specifierList);
    }

    private static GLSLType checkAndTransfer(GLSLType sourceType, ArraySpecifiers sourceSpecifiers, GLSLType targetType, List<ArraySpecifier> targetSpecifiers) throws TypeException {
        ArraySpecifier activeSpecifier = null;
        if (!(targetType instanceof ArrayType) && !targetSpecifiers.isEmpty()) {
            activeSpecifier = targetSpecifiers.remove(0);
            targetType = activeSpecifier.transform(targetType);
        }

        if (targetType instanceof ArrayType) {
            if (!(sourceType instanceof ArrayType)) {
                throw new TypeException(INCOMPATIBLE_TYPES(targetType, sourceType, NO_CONVERSION));
            }

            ArrayType sourceArray = (ArrayType) sourceType;
            ArrayType targetArray = (ArrayType) targetType;

            if (!sourceArray.hasDimension()) {
                throw new BadImplementationException("Initializers should always have a dimension");
            }

            final int dimension = sourceArray.getDimension();
            if (targetArray.hasDimension()) {
                // verify the array sizes
                if (targetArray.getDimension() < sourceArray.getDimension()) {
                    throw new TypeException(INCOMPATIBLE_TYPES(targetArray, sourceArray, INITIALIZER_TOO_BIG));
                }
                if (targetArray.getDimension() > sourceArray.getDimension()) {
                    throw new TypeException(INCOMPATIBLE_TYPES(targetArray, sourceArray, INITIALIZER_TOO_SMALL));
                }
            }

            GLSLType type = checkAndTransfer(sourceArray.baseType(), sourceSpecifiers, targetArray.baseType(), targetSpecifiers);
            if (activeSpecifier == null) {
                return new ArrayType(type, sourceArray.getDimension());
            }

            sourceSpecifiers.addSpecifier(new ArraySpecifier(activeSpecifier.getSourcePosition(), dimension));
            return type;
        }

        if (targetType instanceof StructType) {
            throw new BadImplementationException();
        }

        if (!targetType.isAssignableBy(sourceType)) {
            throw new TypeException(INCOMPATIBLE_TYPES(targetType, sourceType, NO_CONVERSION));
        }

        // all good
        return targetType;
    }
}
