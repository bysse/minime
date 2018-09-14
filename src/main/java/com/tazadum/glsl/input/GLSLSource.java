package com.tazadum.glsl.input;

public class GLSLSource {
    private String filename;
    private String content;
    private FileMapper mapper;

    public GLSLSource(String filename) {
        this(filename, null, null);
    }

    public GLSLSource(String filename, String content) {
        this(filename, content, null);
    }

    public GLSLSource(String filename, String content, FileMapper mapper) {
        this.filename = filename;
        this.content = content;
        this.mapper = mapper;
    }

    public String getFilename() {
        return filename;
    }

    public String getContent() {
        return content;
    }

    public FileMapper getFileMapper() {
        return mapper;
    }

    public boolean hasContent() {
        return content != null;
    }
}
