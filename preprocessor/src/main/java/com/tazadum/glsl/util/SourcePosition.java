package com.tazadum.glsl.util;

import org.antlr.v4.runtime.Token;

/**
 * Created by erikb on 2018-09-18.
 */
public class SourcePosition implements Comparable<SourcePosition> {
    private int line;
    private int column;

    /**
     * Create a SourcePosition from a parser Token.
     *
     * @param token Token that to generate a position from.
     * @return Returns a SourcePosition.
     */
    public static SourcePosition from(Token token) {
        return new SourcePosition(token.getLine(), token.getCharPositionInLine());
    }

    public static SourcePosition add(SourcePosition offset, SourcePosition pos) {
        return new SourcePosition(offset.getLine() + pos.getLine(), offset.getColumn() + pos.getColumn());
    }

    public static SourcePosition from(int line, int column) {
        return new SourcePosition(line, column);
    }

    private SourcePosition(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public int compareTo(SourcePosition o) {
        if (line == o.line) {
            return Integer.compare(column, o.column);
        }
        return line < o.line ? -1 : 1;
    }

    public String toString() {
        return line + "(" + column + ")";
    }
}
