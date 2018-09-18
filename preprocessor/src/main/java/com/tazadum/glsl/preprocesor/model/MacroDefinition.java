package com.tazadum.glsl.preprocesor.model;

/**
 * Created by erikb on 2018-09-17.
 */
public class MacroDefinition {
    private String[] parameters;
    private String template;

    public MacroDefinition(String[] parameters, String template) {
        this.parameters = parameters;
        this.template = template;
    }


    public String[] getParameters() {
        return parameters;
    }

    public String getTemplate() {
        return template;
    }
}
