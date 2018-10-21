package com.tazadum.glsl.language.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Created by erikb on 2018-10-09.
 */
public class TypeQualifierList {
    private List<TypeQualifier> qualifiers = new ArrayList<>();

    public TypeQualifierList() {
    }

    public void add(TypeQualifier qualifier) {
        qualifiers.add(qualifier);
    }

    public List<TypeQualifier> getQualifiers() {
        return qualifiers;
    }

    /**
     * Returns all type qualifiers of a specific type.
     *
     * @param type Type of qualifier to return.
     */
    public <T extends TypeQualifier> Stream<T> get(Class<T> type) {
        return qualifiers.stream()
            .filter(t -> type.isAssignableFrom(t.getClass()))
            .map(type::cast);
    }

    /**
     * Returns true if a qualifier of the given type exists in the list.
     */
    public <T extends TypeQualifier> boolean contains(Class<T> type) {
        return get(type).count() > 0;
    }

    /**
     * Returns true if a specific qualifier exists in the list.
     */
    public <T extends TypeQualifier> boolean contains(T type) {
        return get(type.getClass()).anyMatch(t -> t == type);
    }

    public String toString() {
        if (qualifiers.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (TypeQualifier qualifier : qualifiers) {
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(Objects.toString(qualifier));
        }
        return builder.toString();
    }

    public boolean isEmpty() {
        return qualifiers.isEmpty();
    }
}
