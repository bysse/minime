package com.tazadum.glsl.preprocessor;

/**
 * Created by erikb on 2018-09-17.
 */
public class PreprocessorException extends RuntimeException {
    public PreprocessorException(String message) {
        super(message);
    }

    public PreprocessorException(String message, Throwable cause) {
        super(message, cause);
    }
}
