package com.tazadum.glsl.parser;


import com.tazadum.glsl.language.ast.StatementListNode;

/**
 * @author erikb
 * @since 2016-07-31
 */
public interface ParserListener extends GLSLListener {
    StatementListNode getStatements();
}
