package com.tazadum.glsl.preprocessor.language.ast;

import com.tazadum.glsl.preprocessor.language.Declaration;
import com.tazadum.glsl.preprocessor.model.DeclarationType;

/**
 * Created by erikb on 2018-09-17.
 */
public class LineDeclarationNode implements Declaration {
    private int lineNumber;
    private int sourceLineNumber;

    public LineDeclarationNode(int lineNumber, int sourceLineNumber) {
        this.lineNumber = lineNumber;
        this.sourceLineNumber = sourceLineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getSourceLineNumber() {
        return sourceLineNumber;
    }

    @Override
    public DeclarationType getDeclarationType() {
        return DeclarationType.LINE;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
