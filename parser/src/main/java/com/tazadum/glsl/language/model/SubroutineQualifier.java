package com.tazadum.glsl.language.model;

import com.tazadum.glsl.language.type.TypeQualifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubroutineQualifier implements TypeQualifier {
    private List<String> typeNames;

    public SubroutineQualifier() {
        typeNames = new ArrayList<>();
    }

    public List<String> getTypeNames() {
        return typeNames;
    }

    public SubroutineQualifier(List<String> typeNames) {
        this.typeNames = typeNames == null ? Collections.emptyList() : typeNames;

    }

    public String toString() {
        if (typeNames.isEmpty()) {
            return "subroutine";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("subroutine(");
        for (String id : typeNames) {
            if (builder.length() > 0) {
                builder.append(',');
            }
            builder.append(id);
        }
        builder.append(')');
        return builder.toString();
    }
}
