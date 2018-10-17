package com.tazadum.glsl.language.ast.type;

import com.tazadum.glsl.language.ast.traits.HasSourcePosition;
import com.tazadum.glsl.util.SourcePosition;

public class ArraySpecifier implements HasSourcePosition {
    private static final int IMPLICIT_DIMENSION = -1;

    private final SourcePosition sourcePosition;
    private final int dimension;

    public ArraySpecifier(SourcePosition position) {
        this(position, IMPLICIT_DIMENSION);
    }

    public ArraySpecifier(SourcePosition sourcePosition, int dimension) {
        this.sourcePosition = sourcePosition;
        this.dimension = dimension;
    }

    @Override
    public SourcePosition getSourcePosition() {
        return sourcePosition;
    }

    public int getDimension() {
        return dimension;
    }

    public boolean hasDimension() {
        return dimension != IMPLICIT_DIMENSION;
    }
}
