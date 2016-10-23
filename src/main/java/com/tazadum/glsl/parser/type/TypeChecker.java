package com.tazadum.glsl.parser.type;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.parser.ParserContext;

/**
 * Created by Erik on 2016-10-17.
 */
public class TypeChecker {
    public void check(ParserContext parserContext, Node node) {
        final TypeVisitor visitor = new TypeVisitor(parserContext);
        node.accept(visitor);
    }
}
