package com.tazadum.glsl.preprocessor.model;

/**
 * Created by erikb on 2018-09-17.
 */
public class MacroDefinition {
    private String[] parameters;
    private String template;

    public static MacroDefinition objectLike(String template) {
        return new MacroDefinition(null, template);
    }

    public static MacroDefinition functionLike(String[] parameters, String template) {
        return new MacroDefinition(parameters, template);
    }

    MacroDefinition(String[] parameters, String template) {
        this.parameters = parameters;
        this.template = template;
    }

    public String[] getParameters() {
        return parameters;
    }

    public String getTemplate() {
        return template;
    }

    public boolean isFunctionLike() {
        return parameters != null;
    }
}
