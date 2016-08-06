package com.tazadum.glsl.parser;

import com.tazadum.glsl.parser.type.TypeRegistry;
import com.tazadum.glsl.parser.variable.VariableRegistry;

public class ParserContextImpl extends ContextAwareImpl implements ParserContext {
    private final TypeRegistry typeRegistry;
    private final VariableRegistry variableRegistry;

    public ParserContextImpl(TypeRegistry typeRegistry, VariableRegistry variableRegistry) {
        this.typeRegistry = typeRegistry;
        this.variableRegistry = variableRegistry;
    }

    @Override
    public TypeRegistry getTypeRegistry() {
        return typeRegistry;
    }

    @Override
    public VariableRegistry getVariableRegistry() {
        return variableRegistry;
    }
}
