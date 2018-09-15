package com.tazadum.glsl.parser;

import com.tazadum.glsl.ast.StatementListNode;

/**
 * @author erikb
 * @since 2016-07-31
 */
public interface Parser {
    StatementListNode parse(String source);
}
