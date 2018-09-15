package com.tazadum.glsl.input;

import java.nio.file.Path;

/**
 * Created by erikb on 2018-09-14.
 */
public class FilePosition {
    private Path path;
    private int line;
    private boolean inRootShader;

    public FilePosition(Path path, int line, boolean inRootShader) {
        this.path = path;
        this.line = line;
        this.inRootShader = inRootShader;
    }

    public Path getPath() {
        return path;
    }

    public int getLine() {
        return line;
    }

    /**
     * Returns true if the line number is in the root file of the tree.
     */
    public boolean isInRoot() {
        return inRootShader;
    }
}
