package com.tazadum.glsl.preprocessor.language.ast;

import com.tazadum.glsl.preprocessor.language.Declaration;
import com.tazadum.glsl.preprocessor.language.DeclarationType;
import com.tazadum.glsl.preprocessor.language.ExtensionBehavior;
import com.tazadum.glsl.util.SourcePositionId;

/**
 * Created by erikb on 2018-09-17.
 */
public class ExtensionDeclarationNode extends BaseNode implements Declaration {
    private String extension;
    private ExtensionBehavior behavior;

    public ExtensionDeclarationNode(SourcePositionId sourcePosition, String extension, ExtensionBehavior behavior) {
        super(sourcePosition);
        this.extension = extension;
        this.behavior = behavior;
    }

    @Override
    public DeclarationType getDeclarationType() {
        return DeclarationType.EXTENSION;
    }

    public String getExtension() {
        return extension;
    }

    public ExtensionBehavior getBehavior() {
        return behavior;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return String.format("#extension %s : %s", extension, behavior.token());
    }
}
