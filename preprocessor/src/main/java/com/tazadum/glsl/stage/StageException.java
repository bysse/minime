package com.tazadum.glsl.stage;

/**
 * Created by erikb on 2018-10-25.
 */
public class StageException extends RuntimeException {
    public StageException(String message) {
        super(message);
    }

    public StageException(String message, Throwable cause) {
        super(message, cause);
    }
}
