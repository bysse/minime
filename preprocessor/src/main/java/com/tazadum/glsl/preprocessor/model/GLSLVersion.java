package com.tazadum.glsl.preprocessor.model;

import java.util.Objects;

public enum GLSLVersion implements HasToken {
    OpenGL20("OpenGL 2.0", 110),
    OpenGL21("OpenGL 2.1", 120),
    OpenGL30("OpenGL 3.0", 130),
    OpenGL31("OpenGL 3.1", 140),
    OpenGL32("OpenGL 3.2", 150),
    OpenGLES30("OpenGL ES 3.0", 300),
    OpenGLES31("OpenGL ES 3.1", 310),
    OpenGL33("OpenGL 3.3", 330),
    OpenGL40("OpenGL 4.0", 400),
    OpenGL41("OpenGL 4.1", 410),
    OpenGL42("OpenGL 4.2", 420),
    OpenGL43("OpenGL 4.3", 430),
    OpenGL44("OpenGL 4.4", 440),
    OpenGL45("OpenGL 4.5", 450),
    OpenGL46("OpenGL 4.6", 460),;

    private final String versionName;
    private final int versionCode;

    GLSLVersion(String versionName, int versionCode) {
        this.versionName = versionName;
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String token() {
        return Objects.toString(versionCode);
    }
}
