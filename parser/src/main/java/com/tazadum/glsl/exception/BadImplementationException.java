package com.tazadum.glsl.exception;

/**
 * This is only thrown as a result of an implementation error.
 * Created by erikb on 2018-10-13.
 */
public class BadImplementationException extends RuntimeException {
    public BadImplementationException() {
        super("Not implemented");
    }

    public BadImplementationException(String message) {
        super(message);
    }
}
