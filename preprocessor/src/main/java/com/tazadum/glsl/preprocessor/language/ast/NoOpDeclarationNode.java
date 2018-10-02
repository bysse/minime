package com.tazadum.glsl.preprocessor.language.ast;

import com.tazadum.glsl.preprocessor.language.Declaration;
import com.tazadum.glsl.preprocessor.language.DeclarationType;
import com.tazadum.glsl.util.SourcePositionId;

/**
 * An empty directive line aka no op.
 */
public class NoOpDeclarationNode extends BaseNode implements Declaration {

    public NoOpDeclarationNode(SourcePositionId sourcePosition) {
        super(sourcePosition);
    }

    @Override
    public DeclarationType getDeclarationType() {
        return DeclarationType.NO_OP;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
