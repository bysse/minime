package com.tazadum.glsl.util;

/**
 * Created by Erik on 2016-10-29.
 */
public class IdGenerator {
    private String alphabet = "aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ";
    private String frequent;
    private int index;

    public IdGenerator(char[] frequentlyUsed) {
        this.frequent = new String(frequentlyUsed);

        for (char ch : frequentlyUsed) {
            if (Character.isUpperCase(ch)) {
                frequent += Character.toLowerCase(ch);
            } else {
                frequent += Character.toUpperCase(ch);
            }
        }

        // add extra single letter characters
        for (int i = 0; i < alphabet.length(); i++) {
            final char ch = alphabet.charAt(i);
            if (frequent.indexOf(ch) < 0) {
                frequent += ch;
            }
        }

        this.index = 0;
    }

    public String next() {
        if (index < frequent.length()) {
            return String.valueOf(frequent.charAt(index++));
        }

        String id = "";
        int value = index++;
        while (value >= frequent.length()) {
            int chIndex = value % frequent.length();
            value /= frequent.length();
            id = String.valueOf(frequent.charAt(chIndex)) + id;
        }
        id = String.valueOf(frequent.charAt(value - 1)) + id;
        return id;
    }
}
