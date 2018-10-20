package com.tazadum.glsl.exception;

import com.tazadum.glsl.language.ast.traits.HasSourcePosition;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Created by erikb on 2018-10-16.
 */
public class NotConstExpressionException extends SourcePositionException {
    public NotConstExpressionException(HasSourcePosition hasSourcePosition, String message) {
        super(hasSourcePosition, message);
    }

    public NotConstExpressionException(HasSourcePosition hasSourcePosition, String message, Throwable cause) {
        super(hasSourcePosition, message, cause);
    }

    public NotConstExpressionException(SourcePosition sourcePosition, String message) {
        super(sourcePosition, message);
    }

    public NotConstExpressionException(SourcePosition sourcePosition, String message, Throwable cause) {
        super(sourcePosition, message, cause);
    }
}
