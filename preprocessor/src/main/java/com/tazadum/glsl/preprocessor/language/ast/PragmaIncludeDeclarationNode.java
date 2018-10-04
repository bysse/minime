package com.tazadum.glsl.preprocessor.language.ast;

import com.tazadum.glsl.preprocessor.language.Declaration;
import com.tazadum.glsl.preprocessor.language.DeclarationType;
import com.tazadum.glsl.util.SourcePositionId;

/**
 * This is an implementation specific extension to easily recognize
 * imports of other source files.
 */
public class PragmaIncludeDeclarationNode extends BaseNode implements Declaration {
    private final String filePath;

    /**
     * Constructs a pragma include.
     *
     * @param sourcePosition The source position of the node.
     * @param filePath       The path to a source file to include.
     */
    public PragmaIncludeDeclarationNode(SourcePositionId sourcePosition, String filePath) {
        super(sourcePosition);
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
        return DeclarationType.PRAGMA_INCLUDE;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return "#pragma " + filePath;
    }
}
