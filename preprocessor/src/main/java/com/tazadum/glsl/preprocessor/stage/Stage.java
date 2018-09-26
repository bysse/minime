package com.tazadum.glsl.preprocessor.stage;

import com.tazadum.glsl.preprocessor.LogKeeper;
import com.tazadum.glsl.util.SourcePositionMapper;

public interface Stage {
    StageResult process(SourcePositionMapper mapper, LogKeeper logKeeper, String source);
}
