package com.tazadum.glsl.output;

/**
 * Created by Erik on 2016-10-13.
 */
public class OutputConfig {
    private boolean newlines = true;
    private boolean indentation = true;
    private IdentifierOutput identifiers = IdentifierOutput.Original;

    public boolean isNewlines() {
        return newlines;
    }

    public boolean isIndentation() {
        return indentation;
    }

    public IdentifierOutput getIdentifiers() {
        return identifiers;
    }
}
