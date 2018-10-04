package com.tazadum.glsl.preprocessor.language.ast;

import com.tazadum.glsl.preprocessor.language.Node;
import com.tazadum.glsl.util.SourcePositionId;

public class BaseNode implements Node {
    private final SourcePositionId sourcePosition;

    public BaseNode(SourcePositionId sourcePosition) {
        this.sourcePosition = sourcePosition;
    }

    @Override
    public SourcePositionId getSourcePosition() {
        return sourcePosition;
    }
}
