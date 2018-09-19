package com.tazadum.glsl.preprocessor.language.ast;

import com.tazadum.glsl.preprocessor.language.Node;
import com.tazadum.glsl.util.SourcePosition;

public class BaseNode implements Node {
    private SourcePosition sourcePosition;

    public BaseNode(SourcePosition sourcePosition) {
        this.sourcePosition = sourcePosition;
    }

    @Override
    public SourcePosition getSourcePosition() {
        return sourcePosition;
    }
}
