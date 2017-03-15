package com.tazadum.glsl;// (C) King.com Ltd 2017

import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.optimizer.IdentifierShortener;
import com.tazadum.glsl.parser.type.TypeChecker;

public class GLSLOptimizerContext {
    private TypeChecker typeChecker;
    private ParserContext parserContext;
    private IdentifierShortener identifierShortener;

    public GLSLOptimizerContext(TypeChecker typeChecker, ParserContext parserContext, IdentifierShortener identifierShortener) {
        this.typeChecker = typeChecker;
        this.parserContext = parserContext;
        this.identifierShortener = identifierShortener;
    }

    public TypeChecker getTypeChecker() {
        return typeChecker;
    }

    public ParserContext getParserContext() {
        return parserContext;
    }

    public IdentifierShortener getIdentifierShortener() {
        return identifierShortener;
    }
}
