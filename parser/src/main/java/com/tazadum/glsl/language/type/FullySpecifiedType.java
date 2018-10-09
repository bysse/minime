package com.tazadum.glsl.language.type;

import com.tazadum.glsl.exception.TypeException;
import com.tazadum.glsl.language.model.PrecisionQualifier;
import com.tazadum.glsl.language.model.TypeQualifierList;

public class FullySpecifiedType {
    private TypeQualifierList qualifiers;
    private PrecisionQualifier precision;
    private GLSLType type;

    public FullySpecifiedType(GLSLType type) {
        this(null, null, type);
    }

    public FullySpecifiedType(PrecisionQualifier precision, GLSLType type) {
        this(null, precision, type);
    }

    public FullySpecifiedType(TypeQualifierList qualifiers, PrecisionQualifier precision, GLSLType type) {
        this.qualifiers = qualifiers;
        this.precision = precision;
        this.type = type;

        if (type == null) {
            throw TypeException.missingType();
        }

        // TODO: add some lightweight sanity checks, such as bool can't have precision
    }

    public TypeQualifierList getQualifiers() {
        return qualifiers;
    }

    public PrecisionQualifier getPrecision() {
        return precision;
    }

    public GLSLType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FullySpecifiedType that = (FullySpecifiedType) o;

        if (qualifiers != that.qualifiers) return false;
        if (precision != that.precision) return false;
        return type.equals(that.type);
    }

    @Override
    public int hashCode() {
        int result = qualifiers != null ? qualifiers.hashCode() : 0;
        result = 31 * result + (precision != null ? precision.hashCode() : 0);
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return (qualifiers == null ? "" : qualifiers + " ") +
            (precision == null ? "" : precision.token() + " ") +
            type.token();
    }
}
