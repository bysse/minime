package com.tazadum.glsl.preprocessor.language.ast;

import com.tazadum.glsl.preprocessor.language.Declaration;
import com.tazadum.glsl.preprocessor.language.DeclarationType;
import com.tazadum.glsl.util.SourcePositionId;

/**
 * Created by erikb on 2018-09-17.
 */
public class PragmaDeclarationNode extends BaseNode implements Declaration {
    private final String declaration;

    /**
     * Constructs a PragmaDeclarationNode.
     *
     * @param sourcePosition The source position of the node.
     * @param declaration    The declaration after the word '#pragma'.
     */
    public PragmaDeclarationNode(SourcePositionId sourcePosition, String declaration) {
        super(sourcePosition);
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
