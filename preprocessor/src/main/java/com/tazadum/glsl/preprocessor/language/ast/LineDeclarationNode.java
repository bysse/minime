package com.tazadum.glsl.preprocessor.language.ast;

import com.tazadum.glsl.preprocessor.language.Declaration;
import com.tazadum.glsl.preprocessor.language.DeclarationType;
import com.tazadum.glsl.util.SourcePositionId;

/**
 * Created by erikb on 2018-09-17.
 */
public class LineDeclarationNode extends BaseNode implements Declaration {
    public static final int NO_VALUE = -1;

    private int lineNumber;
    private int sourceLineNumber;

    public LineDeclarationNode(SourcePositionId sourcePosition, int lineNumber, int sourceLineNumber) {
        super(sourcePosition);
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

    public String toString() {
        return "#line " + lineNumber + " " + sourceLineNumber;
    }
}
