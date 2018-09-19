package com.tazadum.glsl.preprocessor.language.ast;

import com.tazadum.glsl.preprocessor.language.Declaration;
import com.tazadum.glsl.preprocessor.language.DeclarationType;
import com.tazadum.glsl.util.SourcePosition;

/**
 * An empty directive line aka no op.
 */
public class NoOpDeclarationNode extends BaseNode implements Declaration {

    public NoOpDeclarationNode(SourcePosition sourcePosition) {
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
