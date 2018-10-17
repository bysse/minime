package com.tazadum.glsl.language.model;

import com.tazadum.glsl.language.ast.traits.HasSourcePosition;
import com.tazadum.glsl.language.type.TypeQualifier;
import com.tazadum.glsl.util.SourcePosition;

import java.util.Collections;
import java.util.List;

public class SubroutineQualifier implements TypeQualifier, HasSourcePosition {
    private final SourcePosition sourcePosition;
    private final List<String> typeNames;

    public SubroutineQualifier(SourcePosition sourcePosition) {
        this(sourcePosition, Collections.emptyList());
    }

    public SubroutineQualifier(SourcePosition sourcePosition, List<String> typeNames) {
        this.sourcePosition = sourcePosition;
        this.typeNames = typeNames;
    }

    @Override
    public SourcePosition getSourcePosition() {
        return sourcePosition;
    }

    public List<String> getTypeNames() {
        return typeNames;
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
