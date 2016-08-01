package com.tazadum.glsl.parser.listener;

public interface HasResult<T> {
    /**
     * Resets all internal state of the listener.
     */
    default void resetState() {
    }

    /**
     * Returns the computed result from the listener
     */
    T getResult();
}
