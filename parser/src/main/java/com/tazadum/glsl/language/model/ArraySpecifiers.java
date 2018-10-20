package com.tazadum.glsl.language.model;

import com.tazadum.glsl.language.ast.type.ArraySpecifier;
import com.tazadum.glsl.language.type.ArrayType;
import com.tazadum.glsl.language.type.GLSLType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by erikb on 2018-10-17.
 */
public class ArraySpecifiers {
    private List<ArraySpecifier> specifiers = new ArrayList<>(3);

    public ArraySpecifiers() {
    }

    public void addSpecifier(ArraySpecifier specifier) {
        specifiers.add(specifier);
    }

    public List<ArraySpecifier> getSpecifiers() {
        return specifiers;
    }

    /**
     * Transform a type into an array type;
     */
    public GLSLType transform(GLSLType baseType) {
        if (specifiers.isEmpty()) {
            return baseType;
        }

        if (baseType instanceof ArrayType) {
            // unwrap array types
            ArrayType arrayType = (ArrayType) baseType;
            GLSLType type = transform(arrayType.baseType());
            return new ArrayType(type, arrayType.getDimension());
        }

        for (ArraySpecifier specifier : specifiers) {
            baseType = specifier.transform(baseType);
        }

        return baseType;
    }

    public String toString() {
        return specifiers.stream().map(ArraySpecifier::toString).collect(Collectors.joining());
    }
}
