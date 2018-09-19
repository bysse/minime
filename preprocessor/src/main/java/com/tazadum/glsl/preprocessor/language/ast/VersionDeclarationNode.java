package com.tazadum.glsl.preprocessor.language.ast;

import com.tazadum.glsl.preprocessor.language.Declaration;
import com.tazadum.glsl.preprocessor.language.DeclarationType;
import com.tazadum.glsl.preprocessor.language.GLSLProfile;
import com.tazadum.glsl.preprocessor.language.GLSLVersion;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Created by erikb on 2018-09-17.
 */
public class VersionDeclarationNode extends BaseNode implements Declaration {
    private GLSLVersion version;
    private GLSLProfile profile;

    public VersionDeclarationNode(SourcePosition sourcePosition, GLSLVersion version, GLSLProfile profile) {
        super(sourcePosition);
        this.version = version;
        this.profile = profile;
    }

    @Override
    public DeclarationType getDeclarationType() {
        return DeclarationType.VERSION;
    }

    public GLSLVersion getVersion() {
        return version;
    }

    public GLSLProfile getProfile() {
        return profile;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        if (profile == null) {
            return "#version " + version.getVersionCode();
        }
        return String.format("#version %d %s", version.getVersionCode(), profile);
    }
}
