package com.tazadum.glsl.preprocessor.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains all macros
 */
public class MacroRegistry {
    private Map<String, MacroDefinition> map;

    public MacroRegistry() {
        this.map = new HashMap<>();
    }

    /**
     * Defines a macro without any parameters.
     *
     * @param name     Name of the macro.
     * @param template The value / template of the macro.
     */
    public void define(String name, String template) {
        define(name, new String[0], template);
    }

    /**
     * Defines a macro with parameters.
     *
     * @param name       Name of the macro.
     * @param parameters An array with the parameter names.
     * @param template   The value / template of the macro.
     */
    public void define(String name, String[] parameters, String template) {
        if (template != null && template.isEmpty()) {
            template = null;
        }
        map.put(name, new MacroDefinition(parameters, template));
    }

    /**
     * Returns true if the specified macro is defined.
     *
     * @param name Name of the macro.
     * @return Returns true if the macro is defined, otherwise false.
     */
    public boolean isDefined(String name) {
        return map.containsKey(name);
    }

    /**
     * Removes any previous definition of a macro.
     *
     * @param name The name of the macro to remove.
     */
    public void undefine(String name) {
        map.remove(name);
    }
}
