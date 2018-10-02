package com.tazadum.glsl.util;

import org.antlr.v4.runtime.Token;

import java.util.Objects;

/**
 * Created by erikb on 2018-09-18.
 */
public class SourcePosition implements Comparable<SourcePosition> {
    public static final SourcePosition TOP = SourcePosition.create(0, 0);

    private int line;
    private int column;

    /**
     * Create a SourcePosition from a parser Token.
     *
     * @param token Token that to generate a position from.
     * @return Returns a SourcePosition.
     */
    public static SourcePosition create(Token token) {
        return new SourcePosition(token.getLine(), token.getCharPositionInLine());
    }

    public static SourcePosition add(SourcePosition base, int lines, int columns) {
        return new SourcePosition(base.getLine() + lines, base.getColumn() + columns);
    }

    public static SourcePosition add(SourcePosition base, SourcePosition offset) {
        return new SourcePosition(base.getLine() + offset.getLine(), base.getColumn() + offset.getColumn());
    }

    public static SourcePosition sub(SourcePosition a, SourcePosition b) {
        return new SourcePosition(a.getLine() - b.getLine(), a.getColumn() - b.getColumn());
    }

    public static SourcePosition create(int line, int column) {
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

    public boolean isAfterOrEqual(SourcePosition sourcePosition) {
        return compareTo(sourcePosition) >= 0;
    }

    @Override
    public int compareTo(SourcePosition o) {
        if (line == o.line) {
            return Integer.compare(column, o.column);
        }
        return line < o.line ? -1 : 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SourcePosition that = (SourcePosition) o;
        return line == that.line &&
                column == that.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, column);
    }

    public String format() {
        return line + "(" + column + ")";
    }

    public String toString() {
        return format();
    }
}
