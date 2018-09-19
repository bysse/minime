package com.tazadum.glsl.preprocessor.model;

/**
 * Wrapper for the special boolean int logic operations.
 */
public class BoolIntLogic {
    public static final int TRUE = 1;
    public static final int FALSE = 0;

    public static boolean isTrue(int value) {
        return value != FALSE;
    }

    public static int logicNot(int value) {
        return toInt(value == FALSE);
    }

    public static int toInt(boolean value) {
        return value ? TRUE : FALSE;
    }
}
