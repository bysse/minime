package com.tazadum.glsl.preprocessor.stage;

import com.tazadum.glsl.util.SourcePositionMapper;

public class StageResult {
    private final String source;
    private final SourcePositionMapper mapper;

    public StageResult(String source, SourcePositionMapper mapper) {
        this.source = source;
        this.mapper = mapper;
    }

    public String getSource() {
        return source;
    }

    public SourcePositionMapper getMapper() {
        return mapper;
    }
}
