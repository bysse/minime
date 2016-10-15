package com.tazadum.glsl.output;

/**
 * Created by Erik on 2016-10-13.
 */
public class OutputConfig {
    private boolean newlines;
    private int indentation;
    private IdentifierOutput identifiers;

    public OutputConfig() {
        setNewlines(true);
        setIndentation(4);
        setIdentifiers(IdentifierOutput.Original);
    }

    public boolean isNewlines() {
        return newlines;
    }

    public int getIndentation() {
        return indentation;
    }

    public IdentifierOutput getIdentifiers() {
        return identifiers;
    }

    public void setNewlines(boolean newlines) {
        this.newlines = newlines;
    }

    public void setIndentation(int indentation) {
        this.indentation = indentation;
    }

    public void setIdentifiers(IdentifierOutput identifiers) {
        this.identifiers = identifiers;
    }
}
