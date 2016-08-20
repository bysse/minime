package com.tazadum.glsl.util;

public class StringUtils {
    public static final boolean isEmpty(String s) {
        return s == null || s.length() == 0 || s.trim().length() == 0;
    }
}
