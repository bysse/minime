package com.tazadum.glsl.preprocessor.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains all macros
 */
public class MacroRegistry {
    private final Map<String, MacroDefinition> map;
    private String[] macroNames = null;

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
        define(name, null, template);
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
        MacroDefinition previous = map.put(name, new MacroDefinition(parameters, template));

        if (previous == null) {
            // only invalidate the cache if the macro name is new ie don't allocate on re-defines.
            macroNames = null;
        }
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
        macroNames = null;
        map.remove(name);
    }

    /**
     * Returns a cached array of all defined macro names.
     */
    public String[] getMacroNames() {
        if (macroNames == null) {
            macroNames = map.keySet().toArray(new String[map.size()]);
        }
        return macroNames;
    }

    /**
     * Returns the definition of a macro.
     *
     * @param name The name of the macro.
     */
    public MacroDefinition getDefinition(String name) {
        return map.get(name);
    }
}
