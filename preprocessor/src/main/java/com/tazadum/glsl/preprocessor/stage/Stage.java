package com.tazadum.glsl.preprocessor.stage;

import com.tazadum.glsl.util.SourcePositionMapper;
import com.tazadum.glsl.util.io.Source;

public interface Stage extends Source {
    SourcePositionMapper getSourcePositionMapper();
}
