package com.tazadum.glsl.stage;

import com.tazadum.glsl.util.SourcePositionMapper;

import java.nio.file.Path;

/**
 * Created by erikb on 2018-10-25.
 */
public class PathStageData implements StageData<Path> {
    private final Path path;

    public PathStageData(Path path) {
        this.path = path;
    }

    @Override
    public Path getData() {
        return path;
    }

    @Override
    public SourcePositionMapper getMapper() {
        return null;
    }
}
