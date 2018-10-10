package com.tazadum.glsl.language.type;

public class FullySpecifiedType {
    private TypeQualifierList qualifiers;
    private GLSLType type;

    public FullySpecifiedType(GLSLType type) {
        this(null, type);
    }

    public FullySpecifiedType(TypeQualifierList qualifiers, GLSLType type) {
        this.qualifiers = qualifiers;
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

        if (qualifiers != that.qualifiers) return false;
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
}
