package com.tazadum.glsl.util;

public class StringUtils {
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0 || s.trim().length() == 0;
    }

    public static boolean isDigit(char ch) {
        return '0' <= ch && ch <= '9';
    }
}
