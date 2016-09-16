package com.tazadum.glsl.ast;

public class Identifier {
    private String original;
    private String replacement;

    public static Identifier get(String original) {
        return new Identifier(original);
    }

    public Identifier(String original) {
        this.original = original;
    }

    public String original() {
        return original;
    }

    public void replacement(String replacement) {
        this.replacement = replacement;
    }

    public String token() {
        return replacement == null ? original : replacement;
    }

    public String toString() {
        return original;
    }

    public boolean isEmpty() {
        return original == null || original.length() == 0;
    }
}
