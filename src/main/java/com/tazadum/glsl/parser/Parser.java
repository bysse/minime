package com.tazadum.glsl.parser;

import com.tazadum.glsl.ast.Context;

/**
 * @author erikb
 * @since 2016-07-31
 */
public interface Parser {
    Context parse(String source);
}
