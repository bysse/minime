package com.tazadum.glsl.ast;

public class Identifier implements Comparable<Identifier> {
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

    public String token(Mode mode) {
        return mode == Mode.Original ? original : replacement;
    }

    public String toString() {
        return original;
    }

    public boolean isEmpty() {
        return original == null || original.length() == 0;
    }

    @Override
    public int compareTo(Identifier o) {
        return original.compareTo(o.original);
    }

    public enum Mode {
        Original,
        Replacement
    }
}
