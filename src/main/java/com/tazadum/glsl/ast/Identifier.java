package com.tazadum.glsl.ast;

public class Identifier {
    private String original;
    private String replacement;

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
}
