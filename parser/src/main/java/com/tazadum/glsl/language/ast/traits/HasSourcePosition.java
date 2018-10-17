package com.tazadum.glsl.language.ast.traits;

import com.tazadum.glsl.util.SourcePosition;

/**
 * Created by erikb on 2018-10-17.
 */
public interface HasSourcePosition {
    SourcePosition getSourcePosition();
}
