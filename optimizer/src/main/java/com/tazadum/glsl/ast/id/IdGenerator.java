package com.tazadum.glsl.ast.id;

import java.util.Arrays;

/**
 * Created by Erik on 2016-10-29.
 */
public class IdGenerator {
    private String alphabet = "aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ";
    private String frequent;
    private int index;

    public static IdGenerator create(String content) {
        final int[][] frequency = new int[256][2];
        for (int i = 0; i < content.length(); i++) {
            final char ch = content.charAt(i);
            if ('a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z') {
                frequency[ch][0]++;
                frequency[ch][1] = ch;
            }
        }
        Arrays.sort(frequency, (a, b) -> b[0] - a[0]);

        int nonZero = 0;
        for (int i = 0; i < 256; i++) {
            if (frequency[i][0] <= 0) {
                break;
            }
            nonZero++;
        }
        final char[] chars = new char[nonZero];
        for (int i = 0; i < nonZero; i++) {
            chars[i] = (char) frequency[i][1];
        }
        return new IdGenerator(chars);
    }

    IdGenerator(String frequent) {
        this.frequent = frequent;
        this.index = 0;
    }

    public IdGenerator(char[] frequentlyUsed) {
        this.frequent = new String(frequentlyUsed);
        this.index = 0;

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
    }

    public IdGenerator clone() {
        return new IdGenerator(frequent);
    }

    public void exclude(char ch) {
        int index = frequent.indexOf(ch);
        if (index < 0) {
            return;
        }
        frequent = frequent.substring(0, index) + frequent.substring(index + 1);
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
