package com.tazadum.glsl.preprocesor.language.ast;

import com.tazadum.glsl.preprocesor.language.Declaration;
import com.tazadum.glsl.preprocesor.model.DeclarationType;

/**
 * Created by erikb on 2018-09-17.
 */
public class PragmaDeclarationNode implements Declaration {
    private String declaration;

    /**
     * Constructs a PragmaDeclarationNode.
     *
     * @param declaration The declaration after the word '#pragma'.
     */
    public PragmaDeclarationNode(String declaration) {
        this.declaration = declaration;
    }

    /**
     * Returns the pragma declaration.
     */
    public String getDeclaration() {
        return declaration;
    }

    @Override
    public DeclarationType getDeclarationType() {
        return DeclarationType.PRAGMA;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return "#pragma " + declaration;
    }
}
