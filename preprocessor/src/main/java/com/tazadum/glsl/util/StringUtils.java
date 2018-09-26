package com.tazadum.glsl.util;

public class StringUtils {
    public static boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean isWhitespace(char ch) {
        return ch == ' ' | ch == '\t';
    }

    public static String rtrim(String s) {
        if (s == null) {
            return null;
        }
        if (s.isEmpty()) {
            return s;
        }

        int index = s.length() - 1;
        while (index >= 0 && isWhitespace(s.charAt(index))) {
            index--;
        }

        return s.substring(0, index + 1);
    }

    /**
     * Cuts away a number of characters from the provided string.
     *
     * @param s      The source string.
     * @param offset The number of characters to cut away.
     *               A negative number means that characters will
     *               be cut from the ends of the string.
     * @return Returns a non-null string.
     */
    public static String cut(String s, int offset) {
        if (s == null) {
            return "";
        }
        if (offset == 0) {
            return s;
        }

        if (offset < 0) {
            int index = s.length() + offset;
            if (index <= 0) {
                return "";
            }
            return s.substring(0, index);
        }

        if (offset >= s.length()) {
            return "";
        }

        return s.substring(offset);
    }
}
