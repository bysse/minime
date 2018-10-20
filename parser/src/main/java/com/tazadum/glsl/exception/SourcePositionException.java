package com.tazadum.glsl.exception;

import com.tazadum.glsl.language.ast.traits.HasSourcePosition;
import com.tazadum.glsl.util.SourcePosition;

public class SourcePositionException extends RuntimeException implements HasSourcePosition {
    private SourcePosition sourcePosition;

    public static SourcePositionException wrap(HasSourcePosition hasSourcePosition, TypeException e) {
        return new SourcePositionException(hasSourcePosition, e.getMessage(), e);
    }

    public SourcePositionException(HasSourcePosition hasSourcePosition, String message) {
        this(hasSourcePosition.getSourcePosition(), message);
    }

    public SourcePositionException(HasSourcePosition hasSourcePosition, String message, Throwable cause) {
        this(hasSourcePosition.getSourcePosition(), message, cause);
    }

    public SourcePositionException(SourcePosition sourcePosition, String message) {
        super(message);
        this.sourcePosition = sourcePosition;
    }

    public SourcePositionException(SourcePosition sourcePosition, String message, Throwable cause) {
        super(message, cause);
        this.sourcePosition = sourcePosition;
    }

    @Override
    public SourcePosition getSourcePosition() {
        return sourcePosition;
    }
}
