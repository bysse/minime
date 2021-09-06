package com.tazadum.glsl.language.model;

import com.tazadum.glsl.language.ast.traits.HasSourcePosition;
import com.tazadum.glsl.util.SourcePosition;

import java.util.Objects;

/**
 * Node for holding a single TypeQualifier
 */
public class LayoutQualifierId implements HasSourcePosition {
    private SourcePosition position;
    private String identifier;
    private Integer value;

    public LayoutQualifierId(SourcePosition position, String identifier) {
        this(position, identifier, null);
    }

    public LayoutQualifierId(SourcePosition position, String identifier, Integer value) {
        this.position = position;
        this.value = value;
        this.identifier = identifier;
    }

    @Override
    public SourcePosition getSourcePosition() {
        return position;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LayoutQualifierId that = (LayoutQualifierId) o;

        if (!position.equals(that.position)) return false;
        if (!identifier.equals(that.identifier)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        int result = position.hashCode();
        result = 31 * result + identifier.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
