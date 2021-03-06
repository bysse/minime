package com.tazadum.glsl.language.type;

import com.tazadum.glsl.language.model.ArraySpecifiers;

import java.util.Objects;

public class FullySpecifiedType {
    private TypeQualifierList qualifiers;
    private GLSLType type;

    public FullySpecifiedType(GLSLType type) {
        this(null, type);
    }

    public FullySpecifiedType(TypeQualifierList qualifiers, GLSLType type) {
        this.qualifiers = qualifiers == null ? new TypeQualifierList() : qualifiers;
        this.type = type;

        assert type != null : "Provided type is null";

        // TODO: add some lightweight sanity checks, such as bool can't have precision
    }

    public TypeQualifierList getQualifiers() {
        return qualifiers;
    }

    public GLSLType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FullySpecifiedType that = (FullySpecifiedType) o;

        if (!Objects.equals(qualifiers, that.qualifiers)) return false;
        return type.equals(that.type);
    }

    @Override
    public int hashCode() {
        int result = qualifiers != null ? qualifiers.hashCode() : 0;
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return (qualifiers == null ? "" : qualifiers + " ") +
                type.token();
    }

    /**
     * Merging the array specifiers of the given type and the provided specifiers.
     */
    public static FullySpecifiedType mergeArraySpecifier(FullySpecifiedType fullySpecifiedType, ArraySpecifiers specifiers) {
        return new FullySpecifiedType(fullySpecifiedType.getQualifiers(), specifiers.transform(fullySpecifiedType.getType()));
    }
}
