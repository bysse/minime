package com.tazadum.glsl.preprocesor.language.ast;

import com.tazadum.glsl.preprocesor.language.Declaration;
import com.tazadum.glsl.preprocesor.model.DeclarationType;

/**
 * This is an implementation specific extension to easily recognize
 * imports of other source files.
 */
public class PragmaIncludeDeclarationNode implements Declaration {
    private String filePath;

    /**
     * Constructs a pragma include.
     *
     * @param filePath The path to a source file to include.
     */
    public PragmaIncludeDeclarationNode(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Returns the path to the included file.
     */
    public String getFilePath() {
        return filePath;
    }

    @Override
    public DeclarationType getDeclarationType() {
        return DeclarationType.PRAGMA;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return "#pragma " + filePath;
    }
}
