package com.tazadum.glsl.preprocessor.language;

import com.tazadum.glsl.util.SourcePositionId;

/**
 * Created by erikb on 2018-09-17.
 */
public interface Node {
    SourcePositionId getSourcePosition();
}
