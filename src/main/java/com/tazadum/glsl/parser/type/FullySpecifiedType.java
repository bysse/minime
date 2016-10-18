package com.tazadum.glsl.parser.type;

import com.tazadum.glsl.exception.TypeException;
import com.tazadum.glsl.language.GLSLType;
import com.tazadum.glsl.language.PrecisionQualifier;
import com.tazadum.glsl.language.TypeQualifier;

public class FullySpecifiedType {
    private TypeQualifier qualifier;
    private PrecisionQualifier precision;
    private GLSLType type;

    public FullySpecifiedType(GLSLType type) {
        this(null, null, type);
    }

    public FullySpecifiedType(PrecisionQualifier precision, GLSLType type) {
        this(null, precision, type);
    }

    public FullySpecifiedType(TypeQualifier qualifier, PrecisionQualifier precision, GLSLType type) {
        this.qualifier = qualifier;
        this.precision = precision;
        this.type = type;

        if (type == null) {
            throw TypeException.missingType();
        }

        // TODO: add some lightweight sanity checks, such as bool can't have precision
    }

    public TypeQualifier getQualifier() {
        return qualifier;
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

        if (qualifier != that.qualifier) return false;
        if (precision != that.precision) return false;
        return type.equals(that.type);

    }

    @Override
    public int hashCode() {
        int result = qualifier != null ? qualifier.hashCode() : 0;
        result = 31 * result + (precision != null ? precision.hashCode() : 0);
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return (qualifier == null ? "" : qualifier.token() + " ") +
                (precision == null ? "" : precision.token() + " ") +
                type.token();
    }
}
