package com.tazadum.glsl.output;

/**
 * Created by Erik on 2016-10-13.
 */
public class OutputConfig {
    private boolean newlines;
    private boolean outputConst;
    private int indentation;
    private IdentifierOutput identifiers;
    private boolean commentWithOriginalIdentifiers;
    private int maxDecimals;
    private boolean implicitConversionToFloat;

    public OutputConfig() {
        setNewlines(true);
        setOutputConst(true);
        setIndentation(4);
        setIdentifiers(IdentifierOutput.Original);
        setCommentWithOriginalIdentifiers(false);
        setMaxDecimals(5);
        setImplicitConversionToFloat(true);
    }

    public OutputConfig(OutputConfig config) {
        setNewlines(config.isNewlines());
        setOutputConst(config.isOutputConst());
        setIndentation(config.getIndentation());
        setIdentifiers(config.getIdentifiers());
        setCommentWithOriginalIdentifiers(config.isCommentWithOriginalIdentifiers());
        setMaxDecimals(config.getMaxDecimals());
        setImplicitConversionToFloat(config.isImplicitConversionToFloat());
    }

    public boolean isNewlines() {
        return newlines;
    }

    public void setNewlines(boolean newlines) {
        this.newlines = newlines;
    }

    public boolean isOutputConst() {
        return outputConst;
    }

    public void setOutputConst(boolean outputConst) {
        this.outputConst = outputConst;
    }

    public int getIndentation() {
        return indentation;
    }

    public void setIndentation(int indentation) {
        this.indentation = indentation;
    }

    public IdentifierOutput getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(IdentifierOutput identifiers) {
        this.identifiers = identifiers;
    }

    public boolean isCommentWithOriginalIdentifiers() {
        return commentWithOriginalIdentifiers;
    }

    public void setCommentWithOriginalIdentifiers(boolean commentWithOriginalIdentifiers) {
        this.commentWithOriginalIdentifiers = commentWithOriginalIdentifiers;
    }

    public boolean isImplicitConversionToFloat() {
        return implicitConversionToFloat;
    }

    public void setImplicitConversionToFloat(boolean implicitConversionToFloat) {
        this.implicitConversionToFloat = implicitConversionToFloat;
    }

    public int getMaxDecimals() {
        return maxDecimals;
    }

    public void setMaxDecimals(int maxDecimals) {
        this.maxDecimals = maxDecimals;
    }
}
