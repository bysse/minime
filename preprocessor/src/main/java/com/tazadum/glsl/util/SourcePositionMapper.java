package com.tazadum.glsl.util;

import java.util.Map;
import java.util.TreeMap;

public class SourcePositionMapper {
    private final SourcePositionMapper sourceMapper;
    private final TreeMap<SourcePosition, SourcePositionId> tree;

    public SourcePositionMapper() {
        this(null);
    }

    public SourcePositionMapper(SourcePositionMapper sourceMapper) {
        this.sourceMapper = sourceMapper;
        this.tree = new TreeMap<>();
    }

    /**
     * Map a SourcePosition to the position of the original file.
     *
     * @param position The position to map.
     */
    public SourcePositionId map(SourcePosition position) {
        SourcePositionId sourcePosition = internalMap(position);
        if (sourceMapper != null) {
            return sourceMapper.map(sourcePosition.getPosition());
        }
        return sourcePosition;
    }

    private SourcePositionId internalMap(SourcePosition position) {
        Map.Entry<SourcePosition, SourcePositionId> entry = tree.floorEntry(position);
        if (entry == null) {
            if (tree.isEmpty()) {
                throw new IllegalStateException("SourcePositionMapper is empty");
            }

            String id = tree.firstEntry().getValue().getId();
            return SourcePositionId.create(id, position);
        }

        final SourcePosition key = entry.getKey();
        final SourcePositionId target = entry.getValue();

        int rowDiff = position.getLine() - key.getLine();
        int colDiff = position.getColumn() - key.getColumn();

        if (rowDiff == 0) {
            return SourcePositionId.add(target, 0, colDiff);
        }

        return SourcePositionId.add(target, rowDiff, position.getColumn());
    }

    /**
     * Adds a mapping between a source line and a target line.
     *
     * @param targetPosition The position in the target / current file.
     * @param sourcePosition The position in the source file.
     */
    public void remap(SourcePosition targetPosition, SourcePositionId sourcePosition) {
        tree.put(targetPosition, sourcePosition);
    }
}
