package com.tazadum.glsl.language.output;

import com.tazadum.glsl.language.HasToken;
import com.tazadum.glsl.util.Provider;

/**
 * Created by erikb on 2018-10-16.
 */
public class SourceBuffer implements Provider<String> {
    private static final char SPACE = ' ';
    private static final char SEMICOLON = ';';
    private static final char NEWLINE = '\n';

    private StringBuilder builder = new StringBuilder();

    public SourceBuffer append(String string) {
        builder.append(string);
        return this;
    }

    public SourceBuffer append(int number) {
        builder.append(number);
        return this;
    }

    public SourceBuffer append(char ch) {
        builder.append(ch);
        return this;
    }

    public SourceBuffer append(HasToken token) {
        builder.append(token.token());
        return this;
    }

    public SourceBuffer appendSpace() {
        if (builder.length() == 0) {
            return this;
        }
        switch (lastCharacter()) {
            case '}':
            case '(':
            case SEMICOLON:
            case NEWLINE:
            case SPACE:
                return this;
            default:
                builder.append(SPACE);
                return this;
        }
    }

    public SourceBuffer appendSemicolon() {
        switch (lastCharacter()) {
            case SEMICOLON:
            case NEWLINE:
                return this;
            default:
                builder.append(SEMICOLON);
                return this;
        }
    }

    /**
     * Adds a comma to the buffer unless ',', '(', '[' is the last character in the buffer.
     */
    public SourceBuffer appendComma() {
        switch (lastCharacter()) {
            case '(':
            case '[':
            case ',':
                return this;
            default:
                builder.append(',');
                return this;
        }
    }

    public AutoCloseable block(char start, char end) {
        builder.append(start);
        return () -> builder.append(end);
    }

    /**
     * Appends a character to the buffer unless the last character
     * is the same one. Useful for avoiding duplicates.
     */
    public SourceBuffer appendIfNotSame(char ch) {
        if (lastCharacter() != ch) {
            builder.append(ch);
        }
        return this;
    }

    public SourceBuffer ifThen(char condition, char then) {
        if (lastCharacter() == condition) {
            builder.append(then);
        }
        return this;
    }

    public char lastCharacter() {
        final int length = builder.length();
        if (length <= 0) {
            return 0;
        }
        return builder.charAt(length - 1);
    }

    @Override
    public String get() {
        final String source = builder.toString();
        builder = new StringBuilder();
        return source;
    }
}
