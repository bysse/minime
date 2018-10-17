package com.tazadum.glsl.language.model;

import com.tazadum.glsl.language.ast.type.ArraySpecifier;
import com.tazadum.glsl.language.type.ArrayType;
import com.tazadum.glsl.language.type.GLSLType;

import java.util.ArrayList;
import java.util.List;

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

        for (ArraySpecifier specifier : specifiers) {
            if (specifier.hasDimension()) {
                baseType = new ArrayType(baseType, specifier.getDimension());
            } else {
                baseType = new ArrayType(baseType);
            }
        }

        return baseType;
    }

}
