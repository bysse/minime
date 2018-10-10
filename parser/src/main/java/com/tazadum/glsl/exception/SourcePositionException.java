package com.tazadum.glsl.exception;

import com.tazadum.glsl.util.SourcePosition;

public class SourcePositionException extends RuntimeException {
    private SourcePosition sourcePosition;

    public SourcePositionException(SourcePosition sourcePosition, String message) {
        super(message);
        this.sourcePosition = sourcePosition;
    }

    public SourcePositionException(SourcePosition sourcePosition, String message, Throwable cause) {
        super(message, cause);
        this.sourcePosition = sourcePosition;
    }

    public SourcePosition getSourcePosition() {
        return sourcePosition;
    }
}
