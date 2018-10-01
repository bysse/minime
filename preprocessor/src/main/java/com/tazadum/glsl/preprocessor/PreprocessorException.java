package com.tazadum.glsl.preprocessor;

import com.tazadum.glsl.util.SourcePosition;
import com.tazadum.glsl.util.SourcePositionId;

/**
 * An exception to rule them all.
 */
public class PreprocessorException extends RuntimeException {
    private SourcePositionId position;

    public PreprocessorException(SourcePosition position, String message) {
        super(message);
        this.position = SourcePositionId.create(position.getLine(), position.getColumn());
    }

    public PreprocessorException(SourcePositionId position, String message) {
        super(message);
        this.position = position;
    }

    public PreprocessorException(SourcePositionId position, String message, Throwable t) {
        super(message, t);
        this.position = position;
    }

    public SourcePositionId getSourcePosition() {
        return position;
    }
}
