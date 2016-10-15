package com.tazadum.glsl.parser;

import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.BuiltInType;
import com.tazadum.glsl.parser.function.FunctionRegistry;
import com.tazadum.glsl.parser.type.FullySpecifiedType;
import com.tazadum.glsl.parser.type.TypeRegistry;
import com.tazadum.glsl.parser.variable.VariableRegistry;

public class ParserContextImpl extends ContextAwareImpl implements ParserContext {
    private final TypeRegistry typeRegistry;
    private final VariableRegistry variableRegistry;
    private final FunctionRegistry functionRegistry;

    public ParserContextImpl(TypeRegistry typeRegistry, VariableRegistry variableRegistry, FunctionRegistry functionRegistry) {
        this.typeRegistry = typeRegistry;
        this.variableRegistry = variableRegistry;
        this.functionRegistry = functionRegistry;

        setupVariables();
    }

    private void setupVariables() {
        GLSLContext context = currentContext();

        variableRegistry.declare(context, variable(BuiltInType.VEC4, "gl_FragColor"));
        variableRegistry.declare(context, variable(BuiltInType.VEC4, "gl_FragCoord"));
        variableRegistry.declare(context, variable(BuiltInType.BOOL, "gl_FrontFacing"));
        variableRegistry.declare(context, variable(BuiltInType.VEC2, "gl_PointCoord"));
        variableRegistry.declare(context, variable(BuiltInType.FLOAT, "gl_FragDepth"));

        //functionRegistry.declare(context, function(BuiltInType.FLOAT, "dot"));
    }

    private VariableDeclarationNode variable(BuiltInType type, String identifier) {
        return new VariableDeclarationNode(new FullySpecifiedType(type), identifier, null, null);
    }

    private FunctionPrototypeNode function(BuiltInType type, String identifier, BuiltInType... param) {
        return new FunctionPrototypeNode(identifier, new FullySpecifiedType(type));
    }

    @Override
    public TypeRegistry getTypeRegistry() {
        return typeRegistry;
    }

    @Override
    public VariableRegistry getVariableRegistry() {
        return variableRegistry;
    }

    @Override
    public FunctionRegistry getFunctionRegistry() {
        return functionRegistry;
    }
}
