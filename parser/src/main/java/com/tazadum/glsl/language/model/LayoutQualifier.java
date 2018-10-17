package com.tazadum.glsl.language.model;

import com.tazadum.glsl.language.ast.traits.HasSourcePosition;
import com.tazadum.glsl.language.type.TypeQualifier;
import com.tazadum.glsl.util.SourcePosition;

import java.util.ArrayList;
import java.util.List;

/**
 * Layout qualifiers can appear in several forms of declaration. They can appear as part of an
 * interface block definition or block member, as shown in the grammar in the previous section. They
 * can also appear with just an interface-qualifier to establish layouts of other declarations made with
 * that qualifier
 */
public class LayoutQualifier implements TypeQualifier, HasSourcePosition {
    private final SourcePosition sourcePosition;
    private final List<LayoutQualifierId> qualifiers;

    public LayoutQualifier(SourcePosition sourcePosition) {
        this.sourcePosition = sourcePosition;
        qualifiers = new ArrayList<>();
    }

    public void addQualifierId(LayoutQualifierId id) {
        qualifiers.add(id);
    }

    @Override
    public SourcePosition getSourcePosition() {
        return sourcePosition;
    }

    public List<LayoutQualifierId> getIds() {
        return qualifiers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LayoutQualifier that = (LayoutQualifier) o;

        return qualifiers.equals(that.qualifiers);
    }

    @Override
    public int hashCode() {
        return qualifiers.hashCode();
    }
}
