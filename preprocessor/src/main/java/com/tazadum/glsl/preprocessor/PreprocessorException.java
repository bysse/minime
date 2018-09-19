package com.tazadum.glsl.preprocessor;

import com.tazadum.glsl.util.SourcePosition;

/**
 * An exception to rule them all.
 */
public class PreprocessorException extends RuntimeException {
    private SourcePosition position;

    public PreprocessorException(SourcePosition position, String message) {
        super(message);
        this.position = position;
    }

    public PreprocessorException(SourcePosition position, String message, Throwable t) {
        super(message, t);
        this.position = position;
    }

    public SourcePosition getPosition() {
        return position;
    }
}
