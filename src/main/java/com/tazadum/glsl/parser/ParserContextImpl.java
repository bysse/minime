package com.tazadum.glsl.parser;

import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.BuiltInType;
import com.tazadum.glsl.language.GenTypeIterator;
import com.tazadum.glsl.language.GenTypes;
import com.tazadum.glsl.parser.function.FunctionPrototype;
import com.tazadum.glsl.parser.function.FunctionRegistry;
import com.tazadum.glsl.parser.type.FullySpecifiedType;
import com.tazadum.glsl.parser.type.TypeRegistry;
import com.tazadum.glsl.parser.variable.VariableRegistry;

import java.util.Arrays;

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

        // Angle and Trigonometry Functions

        // Exponential Functions

        // Common Functions

        // Geometric Functions
        function("length", BuiltInType.FLOAT, GenTypes.GenType);
        function("distance", BuiltInType.FLOAT, GenTypes.GenType);
        function("dot" , BuiltInType.FLOAT, GenTypes.GenType, GenTypes.GenType);
        fixedFunction("cross", BuiltInType.VEC3, BuiltInType.VEC3, BuiltInType.VEC3);
        function("normalize" , GenTypes.GenType, GenTypes.GenType, GenTypes.GenType);
        function("faceforward" , GenTypes.GenType, GenTypes.GenType, GenTypes.GenType, GenTypes.GenType);
        function("reflect" , GenTypes.GenType, GenTypes.GenType, GenTypes.GenType);
        function("refract" , GenTypes.GenType, GenTypes.GenType, GenTypes.GenType);

        // Matrix Functions

        // Vector Relational Functions

        // Integer Functions

        // Texture Functions

    }

    private VariableDeclarationNode variable(BuiltInType type, String identifier) {
        return new VariableDeclarationNode(new FullySpecifiedType(type), identifier, null, null);
    }

    private FunctionPrototypeNode fixedFunction(String identifier, BuiltInType returnType, BuiltInType... parameters) {
        FunctionPrototypeNode node = new FunctionPrototypeNode(identifier, new FullySpecifiedType(returnType));
        node.setPrototype(new FunctionPrototype(returnType, parameters));
        return node;
    }

    private void function(String identifier, Object... parameterTypes) {
        final GenTypeIterator iterator = new GenTypeIterator(parameterTypes);
        while (iterator.hasNext()) {
            final BuiltInType[] parameters = iterator.next();
            final BuiltInType returnType = parameters[0];
            final BuiltInType[] arguments = Arrays.copyOfRange(parameters, 1, parameters.length);

            final FunctionPrototypeNode node = new FunctionPrototypeNode(identifier, new FullySpecifiedType(returnType));
            node.setPrototype(new FunctionPrototype(returnType, arguments));
            functionRegistry.declare(node);
        }
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
