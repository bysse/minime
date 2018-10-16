package com.tazadum.glsl.exception;

import com.tazadum.glsl.util.SourcePosition;

/**
 * Created by erikb on 2018-10-16.
 */
public class NotContExpressionException extends SourcePositionException {
    public NotContExpressionException(SourcePosition sourcePosition, String message) {
        super(sourcePosition, message);
    }

    public NotContExpressionException(SourcePosition sourcePosition, String message, Throwable cause) {
        super(sourcePosition, message, cause);
    }
}
