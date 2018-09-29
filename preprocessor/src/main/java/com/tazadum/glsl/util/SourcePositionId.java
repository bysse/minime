package com.tazadum.glsl.util;

/**
 * Created by erikb on 2018-09-18.
 */
public class SourcePositionId {
    private final String id;
    private final SourcePosition position;

    public static SourcePositionId create(String sourceId, SourcePosition position) {
        return new SourcePositionId(sourceId, position);
    }

    public static SourcePositionId create(String id, int line, int column) {
        return new SourcePositionId(id, SourcePosition.create(line, column));
    }

    private SourcePositionId(String id, SourcePosition position) {
        this.id = id;
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public SourcePosition getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SourcePositionId that = (SourcePositionId) o;

        if (!id.equals(that.id)) return false;
        return position.equals(that.position);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + position.hashCode();
        return result;
    }

    public String toString() {
        return id + ":" + position;
    }
}
