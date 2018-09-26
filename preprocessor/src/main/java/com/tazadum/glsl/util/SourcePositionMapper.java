package com.tazadum.glsl.util;

import java.util.Map;
import java.util.TreeMap;

public class SourcePositionMapper {
    private TreeMap<SourcePosition, SourcePosition> tree;
    private TreeMap<SourcePosition, SourcePosition> aggregate;

    public SourcePositionMapper() {
        this.tree = new TreeMap<>();
        this.aggregate = new TreeMap<>();
    }

    public SourcePosition map(SourcePosition position) {
        Map.Entry<SourcePosition, SourcePosition> entry = aggregate.floorEntry(position);
        if (entry == null) {
            return position;
        }

        final SourcePosition key = entry.getKey();
        final SourcePosition value = entry.getValue();

        if (position.getLine() == key.getLine()) {
            final int sourceDiff = position.getColumn() - key.getColumn();
            return SourcePosition.create(value.getLine(), sourceDiff + value.getColumn());
        }

        // different lines
        final int lineDiff = value.getLine() - key.getLine();
        return SourcePosition.add(position, lineDiff, value.getColumn());
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
            aggregate.put(from, target);
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
        SourcePosition delta = null;
        for (Map.Entry<SourcePosition, SourcePosition> entry : tree.entrySet()) {
            final SourcePosition key = entry.getKey();
            final SourcePosition value = entry.getValue();

            int lineDiff = value.getLine() - key.getLine();
            int colDiff = Math.max(0, value.getColumn() - key.getColumn());

            if (previousSourceLine == key.getLine()) {
                lineDiff = 0;
            }
            previousSourceLine = key.getLine();

            if (delta == null) {
                delta = SourcePosition.create(lineDiff, colDiff);
                aggregate.put(key, value);
                continue;
            }

            if (lineDiff == 0) {
                delta = SourcePosition.add(delta, 0, colDiff);
            } else {
                delta = SourcePosition.create(delta.getLine() + lineDiff, colDiff);
            }

            aggregate.put(key, SourcePosition.add(value, delta));
        }
    }
}
