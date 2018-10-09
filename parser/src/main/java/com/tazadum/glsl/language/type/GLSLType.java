package com.tazadum.glsl.language.type;

import com.tazadum.glsl.exception.NoSuchFieldException;
import com.tazadum.glsl.language.HasToken;

public interface GLSLType extends HasToken {
    /**
     * Returns the type of a field in the type.
     * This is mostly useed by structs and vectors.
     *
     * @param fieldName The name of the field.
     * @return The type of the field.
     * @throws NoSuchFieldException If the field doesn't exist.
     */
    GLSLType fieldType(String fieldName) throws NoSuchFieldException;

    /**
     * Returns true if the type can be assigned by another type.
     *
     * @param type The type that is assigned to a field of the current type.
     */
    boolean isAssignableBy(GLSLType type);

    /**
     * @return True if the type is an array.
     */
    boolean isArray();

    /**
     * The unqualified type meaning uint will return int.
     */
    GLSLType baseType();
}
