package com.tazadum.glsl.parser;

import com.tazadum.glsl.ast.Shader;

/**
 * @author erikb
 * @since 2016-07-31
 */
public interface Parser {
    Shader parse(String source);
}
