package com.tazadum.glsl.util;

import java.util.Iterator;

public class LineTokenizer implements Iterable<String>, Iterator<String> {
    private int offset;
    private final String source;

    public LineTokenizer(String source) {
        this.offset = 0;
        this.source = source;
    }

    public String getLine(String eol) {
        String line = getLine();
        return line == null ? eol : line;
    }

    public String getLine() {
        if (source == null) {
            return null;
        }
        final int length = source.length();

        if (offset >= length) {
            if (offset == 0 && length == 0) {
                offset++;
                return "";
            }
            return null;
        }

        final StringBuilder builder = new StringBuilder();
        while (offset < length) {
            final char ch = source.charAt(offset);
            offset++;

            if (ch == '\r') {
                if (peek() == '\n') {
                    offset++;
                }
                break;
            } else if (ch == '\n') {
                break;
            }

            builder.append(ch);
        }

        return builder.toString();
    }

    public int getLineNumber() {
        return offset;
    }

    private char peek() {
        if (offset >= source.length()) {
            return 0;
        }
        return source.charAt(offset);
    }

    @Override
    public Iterator<String> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return offset < source.length();
    }

    @Override
    public String next() {
        return getLine();
    }
}
