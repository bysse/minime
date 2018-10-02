package com.tazadum.glsl.preprocessor.language.ast;

import com.tazadum.glsl.preprocessor.language.Declaration;
import com.tazadum.glsl.preprocessor.language.DeclarationType;
import com.tazadum.glsl.util.SourcePositionId;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by erikb on 2018-09-17.
 */
public class MacroDeclarationNode extends BaseNode implements Declaration {
    private String identifier;
    private List<String> parameters;
    private String value;

    public MacroDeclarationNode(SourcePositionId sourcePosition, String identifier, List<String> parameters, String value) {
        super(sourcePosition);
        this.identifier = identifier;
        this.parameters = parameters;

        if (value != null && value.isEmpty()) {
            this.value = null;
        } else {
            this.value = value;
        }
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

    /**
     * Returns the value of the MACRO or null if it has no value.
     */
    public String getValue() {
        return value;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        String params = "";
        if (parameters != null) {
            params = parameters.stream().collect(
                    Collectors.joining("(", ", ", ")")
            );
        }

        return "#define " + identifier + params + " " + value;
    }
}
