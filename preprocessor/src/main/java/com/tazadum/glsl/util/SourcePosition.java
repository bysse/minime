package com.tazadum.glsl.util;

import org.antlr.v4.runtime.Token;

/**
 * Created by erikb on 2018-09-18.
 */
public class SourcePosition implements Comparable<SourcePosition> {
    private int line;
    private int column;

    public static SourcePosition from(Token token) {
        return new SourcePosition(token.getLine(), token.getCharPositionInLine());
    }

    public SourcePosition(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public String toString() {
        return line + "(" + column + ")";
    }

    @Override
    public int compareTo(SourcePosition o) {
        if (line == o.line) {
            return Integer.compare(column, o.column);
        }
        return line < o.line ? -1 : 1;
    }
}
