package com.tazadum.glsl.parser;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.parser.function.FunctionRegistry;
import com.tazadum.glsl.parser.type.TypeRegistry;
import com.tazadum.glsl.parser.variable.VariableRegistry;

/**
 * @author erikb
 * @since 2016-07-31
 */
public interface ParserContext extends ContextAware {
    TypeRegistry getTypeRegistry();

    VariableRegistry getVariableRegistry();

    FunctionRegistry getFunctionRegistry();

    void dereferenceTree(Node node);
}
