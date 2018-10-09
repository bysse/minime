package com.tazadum.glsl.language.model;

import com.tazadum.glsl.language.ast.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Layout qualifiers can appear in several forms of declaration. They can appear as part of an
 * interface block definition or block member, as shown in the grammar in the previous section. They
 * can also appear with just an interface-qualifier to establish layouts of other declarations made with
 * that qualifier
 */
public class LayoutQualifier implements TypeQualifier {
    private List<QualifierId> qualifiers;

    public LayoutQualifier() {
        qualifiers = new ArrayList<>();
    }

    public LayoutQualifier(List<QualifierId> qualifiers) {
        this.qualifiers = qualifiers == null ? Collections.emptyList() : qualifiers;
    }

    public List<QualifierId> getQualifiers() {
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

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("layout(");
        for (QualifierId id : qualifiers) {
            if (builder.length() > 0) {
                builder.append(',');
            }
            builder.append(Objects.toString(id));
        }
        builder.append(')');
        return builder.toString();
    }

    public static class QualifierId {
        private String name;
        private Node value;

        public QualifierId(String name, Node value) {
            this.name = name;
            this.value = value;

            assert name != null : "Qualifier Id has a null name";
        }

        public String getName() {
            return name;
        }

        public Node getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            QualifierId that = (QualifierId) o;

            if (!name.equals(that.name)) return false;
            return value != null ? value.equals(that.value) : that.value == null;
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + (value != null ? value.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            if (value == null) {
                return name;
            }
            return name + "=" + Objects.toString(value);
        }

    }
}
