package com.tazadum.glsl.preprocesor.language.ast;

import com.tazadum.glsl.preprocesor.language.Declaration;
import com.tazadum.glsl.preprocesor.model.DeclarationType;

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
