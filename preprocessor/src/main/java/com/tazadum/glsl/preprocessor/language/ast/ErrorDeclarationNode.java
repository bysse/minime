package com.tazadum.glsl.preprocessor.language.ast;

import com.tazadum.glsl.preprocessor.language.Declaration;
import com.tazadum.glsl.preprocessor.language.DeclarationType;
import com.tazadum.glsl.util.SourcePositionId;

/**
 * Created by erikb on 2018-10-02.
 */
public class ErrorDeclarationNode extends BaseNode implements Declaration {
    private String message;

    public ErrorDeclarationNode(SourcePositionId sourcePosition, String message) {
        super(sourcePosition);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public DeclarationType getDeclarationType() {
        return DeclarationType.ERROR;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return "#error " + message;
    }
}
