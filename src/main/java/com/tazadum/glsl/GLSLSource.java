package com.tazadum.glsl;

public class GLSLSource {
    private String filename;
    private String content;

    public GLSLSource(String filename, String content) {
        this.filename = filename;
        this.content = content;
    }

    public String getFilename() {
        return filename;
    }

    public String getContent() {
        return content;
    }

    public boolean hasContent() {
        return content != null;
    }
}
