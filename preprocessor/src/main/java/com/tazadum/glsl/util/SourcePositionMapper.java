package com.tazadum.glsl.util;

import java.util.*;

public class SourcePositionMapper {
    private SourcePositionMapper sourceMapper;
    private TreeMap<SourcePosition, SourcePositionId> tree;
    private TreeMap<SourcePosition, SourcePositionId> aggregate;

    public SourcePositionMapper() {
        this(null);
    }

    public SourcePositionMapper(SourcePositionMapper sourceMapper) {
        this.sourceMapper = sourceMapper;
        this.tree = new TreeMap<>();
        this.aggregate = new TreeMap<>();
    }

    public SourcePositionId map(SourcePosition position) {
        SourcePositionId sourcePosition = internalMap(position);
        if (sourceMapper != null) {
            return sourceMapper.map(sourcePosition.getPosition());
        }
        return sourcePosition;
    }

    public SourcePositionId internalMap(SourcePosition position) {
        SourcePosition key = aggregate.floorKey(position);
        if (key == null) {
            if (aggregate.isEmpty()) {
                throw new IllegalStateException("SourcePositionMapper is empty");
            }

            String id = tree.firstEntry().getValue().getId();
            return SourcePositionId.create(id, position);
        }

        final SourcePositionId delta = aggregate.get(key);

        int column = position.getColumn();
        if (position.getLine() == key.getLine()) {
            column = position.getColumn() - key.getColumn();
        }

        return SourcePositionId.create(delta.getId(), position.getLine() + delta.getPosition().getLine(), column);
    }

    /**
     * Adds a mapping between a source line and a target line.
     *
     * @param sourceId       The id of the current source.
     * @param targetPosition The position in the target / current file.
     * @param sourcePosition The position in the source file.
     */
    public void remap(String sourceId, SourcePosition targetPosition, SourcePosition sourcePosition) {
        tree.put(targetPosition, SourcePositionId.create(sourceId, sourcePosition));

        final SourcePosition fromTarget = aggregate.floorKey(targetPosition);
        if (fromTarget == null) {
            rebuildAggregate();
            return;
        }

        final SourcePosition toTarget = aggregate.floorKey(sourcePosition);
        if (!toTarget.equals(fromTarget)) {
            throw new IllegalArgumentException("Nested mappings are not supported");
        }

        rebuildAggregate();
    }

    private void rebuildAggregate() {
        aggregate.clear();

        int previousSourceLine = -1;
        int previousTargetLine = -1;

        SourcePosition delta = null;
        for (Map.Entry<SourcePosition, SourcePositionId> entry : tree.entrySet()) {
            final SourcePosition key = entry.getKey();
            final SourcePositionId point = entry.getValue();
            final SourcePosition value = point.getPosition();

            int lineDiff = value.getLine() - key.getLine();
            int colDiff = value.getColumn() - key.getColumn();

            if (previousSourceLine == key.getLine()) {
                // if we're adding a re-mapping on the same line
                // remove the previous delta from the diff
                lineDiff -= previousTargetLine - previousSourceLine;
            }

            previousSourceLine = key.getLine();
            previousTargetLine = value.getLine();

            if (delta == null) {
                // no previous delta to add to this remap
                delta = SourcePosition.create(lineDiff, colDiff);
                aggregate.put(key, SourcePositionId.create(point.getId(), delta));

                continue;
            }

            // update the delta
            if (lineDiff > 0) {
                // don't aggregate column delta over multiple lines
                delta = SourcePosition.create(delta.getLine() + lineDiff, colDiff);
            } else {
                delta = SourcePosition.add(delta, lineDiff, colDiff);
            }

            // record the delta point
            aggregate.put(key, SourcePositionId.create(point.getId(), delta));
        }
    }
}
