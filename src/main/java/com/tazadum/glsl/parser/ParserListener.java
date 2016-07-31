package com.tazadum.glsl.parser;

import com.tazadum.glsl.ast.Shader;
import com.tazadum.glsl.language.GLSLListener;

/**
 * @author erikb
 * @since 2016-07-31
 */
public interface ParserListener extends GLSLListener {
    Shader getShader();
}
