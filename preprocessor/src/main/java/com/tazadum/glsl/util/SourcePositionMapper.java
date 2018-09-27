package com.tazadum.glsl.util;

import java.util.Map;
import java.util.TreeMap;

public class SourcePositionMapper {
    private SourcePositionMapper sourceMapper;
    private TreeMap<SourcePosition, SourcePosition> tree;
    private TreeMap<SourcePosition, SourcePosition> aggregate;

    public SourcePositionMapper() {
        this(null);
    }

    public SourcePositionMapper(SourcePositionMapper sourceMapper) {
        this.sourceMapper = sourceMapper;
        this.tree = new TreeMap<>();
        this.aggregate = new TreeMap<>();
    }

    public SourcePosition map(SourcePosition position) {
        SourcePosition sourcePosition = internalMap(position);
        if (sourceMapper != null) {
            return sourceMapper.map(sourcePosition);
        }
        return sourcePosition;
    }

    public SourcePosition internalMap(SourcePosition position) {
        SourcePosition key = aggregate.floorKey(position);
        if (key == null) {
            return position;
        }

        final SourcePosition delta = aggregate.get(key);

        int column = position.getColumn();
        if (position.getLine() == key.getLine()) {
            column = position.getColumn() - key.getColumn();
        }

        return SourcePosition.create(position.getLine() + delta.getLine(), column);
    }

    /**
     * Adds a mapping between a source line and a target line.
     * This can be highly confusing and error prone since the
     * target parameter is relative position is relative ie
     * it's subjected to the mapping of the previous lines.
     *
     * @param from   The absolute source position.
     * @param target The target source position.
     */
    public void remap(SourcePosition from, SourcePosition target) {
        if (!target.isAfter(from)) {
            throw new IllegalArgumentException("The 'to' position must be after 'from'");
        }

        tree.put(from, target);

        final SourcePosition fromTarget = aggregate.floorKey(from);
        if (fromTarget == null) {
            rebuildAggregate();
            return;
        }

        final SourcePosition toTarget = aggregate.floorKey(target);
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
        for (Map.Entry<SourcePosition, SourcePosition> entry : tree.entrySet()) {
            final SourcePosition key = entry.getKey();
            final SourcePosition value = entry.getValue();

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
                aggregate.put(key, delta);

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
            aggregate.put(key, delta);
        }
    }
}
