package com.tazadum.glsl.preprocesor.language.ast;

import com.tazadum.glsl.preprocesor.language.Declaration;
import com.tazadum.glsl.preprocesor.model.DeclarationType;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Created by erikb on 2018-09-17.
 */
public class MacroDeclarationNode implements Declaration {
    private String identifier;
    private List<String> parameters;
    private String value;

    public MacroDeclarationNode(String identifier, List<String> parameters, String value) {
        this.identifier = identifier;
        this.parameters = parameters;
        this.value = value;
    }

    @Override
    public DeclarationType getDeclarationType() {
        return DeclarationType.DEFINE;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public String getValue() {
        return value;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        String params = "";
        if (!parameters.isEmpty()) {
            params = parameters.stream().collect(
                    Collectors.joining("(", ", ", ")")
            );
        }

        return "#define " + identifier + params + " " + value;
    }
}
