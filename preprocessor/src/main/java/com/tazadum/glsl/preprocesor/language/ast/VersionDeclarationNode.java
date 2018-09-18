package com.tazadum.glsl.preprocesor.language.ast;

import com.tazadum.glsl.preprocesor.language.Declaration;
import com.tazadum.glsl.preprocesor.model.DeclarationType;
import com.tazadum.glsl.preprocesor.model.GLSLProfile;
import com.tazadum.glsl.preprocesor.model.GLSLVersion;

/**
 * Created by erikb on 2018-09-17.
 */
public class VersionDeclarationNode implements Declaration {
    private GLSLVersion version;
    private GLSLProfile profile;

    public VersionDeclarationNode(GLSLVersion version, GLSLProfile profile) {
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
