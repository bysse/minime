package com.tazadum.glsl.preprocessor.language.ast;

import com.tazadum.glsl.preprocessor.language.Declaration;
import com.tazadum.glsl.preprocessor.model.DeclarationType;

/**
 * Created by erikb on 2018-09-17.
 */
public class NoOpDeclarationNode implements Declaration {

    public NoOpDeclarationNode() {
    }

    @Override
    public DeclarationType getDeclarationType() {
        return DeclarationType.NO_OP;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
