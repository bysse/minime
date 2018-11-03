package com.tazadum.glsl.language.ast;

public class Identifier implements Comparable<Identifier> {
    private String original;
    private String replacement;

    public static Identifier get(String original) {
        return new Identifier(original);
    }

    /**
     * Returns an instance of Identifier is the provided value is not null.
     */
    public static Identifier orNull(String identifier) {
        if (identifier == null) {
            return null;
        }
        return get(identifier);
    }

    public Identifier(Identifier identifier) {
        this.original = identifier.original;
        this.replacement = identifier.replacement;
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

    public void changeOriginal(String original) {
        this.original = original;
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
