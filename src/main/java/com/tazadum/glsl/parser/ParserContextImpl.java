package com.tazadum.glsl.parser;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ParentNode;
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
import com.tazadum.glsl.parser.visitor.DereferenceVisitor;

import java.util.Arrays;
import java.util.Set;

public class ParserContextImpl implements ParserContext {
    private final TypeRegistry typeRegistry;
    private final VariableRegistry variableRegistry;
    private final FunctionRegistry functionRegistry;
    private final DereferenceVisitor dereferenceVisitor;
    private final ContextAware contextAware;

    public ParserContextImpl(TypeRegistry typeRegistry, VariableRegistry variableRegistry, FunctionRegistry functionRegistry) {
        this(typeRegistry, variableRegistry, functionRegistry, new ContextAwareImpl());
    }

    public ParserContextImpl(TypeRegistry typeRegistry, VariableRegistry variableRegistry, FunctionRegistry functionRegistry, ContextAware contextAware) {
        this.typeRegistry = typeRegistry;
        this.variableRegistry = variableRegistry;
        this.functionRegistry = functionRegistry;
        this.dereferenceVisitor = new DereferenceVisitor(this);
        this.contextAware = contextAware;

        if (variableRegistry.isEmpty()) {
            setupVariables();
        }

        if (functionRegistry.isEmpty()) {
            setupFunctions();
        }
    }

    private void setupVariables() {
        GLSLContext context = currentContext();

        variableRegistry.declare(context, variable(BuiltInType.VEC4, "gl_FragColor"));
        variableRegistry.declare(context, variable(BuiltInType.VEC4, "gl_FragCoord"));
        variableRegistry.declare(context, variable(BuiltInType.BOOL, "gl_FrontFacing"));
        variableRegistry.declare(context, variable(BuiltInType.VEC2, "gl_PointCoord"));
        variableRegistry.declare(context, variable(BuiltInType.FLOAT, "gl_FragDepth"));
    }

    private void setupFunctions() {
        // scalar type construction
        fixedFunction("float", BuiltInType.FLOAT, BuiltInType.FLOAT);
        fixedFunction("float", BuiltInType.FLOAT, BuiltInType.INT);
        fixedFunction("int", BuiltInType.INT, BuiltInType.FLOAT);
        fixedFunction("int", BuiltInType.INT, BuiltInType.INT);

        // vector construction
        fixedFunction("vec2", BuiltInType.VEC2, BuiltInType.VEC2);
        function("vec2", BuiltInType.VEC2, GenTypes.GenScalarType);
        function("vec2", BuiltInType.VEC2, GenTypes.GenScalarType, GenTypes.GenScalarType);

        // vec3
        fixedFunction("vec3", BuiltInType.VEC3, BuiltInType.VEC3);
        function("vec3", BuiltInType.VEC3, GenTypes.GenScalarType);
        function("vec3", BuiltInType.VEC3, GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType);
        function("vec3", BuiltInType.VEC3, BuiltInType.VEC2, GenTypes.GenScalarType);
        function("vec3", BuiltInType.VEC3, GenTypes.GenScalarType, BuiltInType.VEC2);
        // vec4
        fixedFunction("vec4", BuiltInType.VEC4, BuiltInType.VEC4);
        function("vec4", BuiltInType.VEC4, GenTypes.GenScalarType);
        function("vec4", BuiltInType.VEC4, GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType);
        function("vec4", BuiltInType.VEC4, BuiltInType.VEC3, GenTypes.GenScalarType);
        function("vec4", BuiltInType.VEC4, GenTypes.GenScalarType, BuiltInType.VEC3);
        function("vec4", BuiltInType.VEC4, BuiltInType.VEC2, GenTypes.GenScalarType, GenTypes.GenScalarType);
        function("vec4", BuiltInType.VEC4, GenTypes.GenScalarType, BuiltInType.VEC2, GenTypes.GenScalarType);
        function("vec4", BuiltInType.VEC4, GenTypes.GenScalarType, GenTypes.GenScalarType, BuiltInType.VEC2);

        // naive matrix construction
        function("mat2", BuiltInType.MAT2, GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType);
        function("mat2", BuiltInType.MAT2, BuiltInType.VEC2, GenTypes.GenScalarType, GenTypes.GenScalarType);
        function("mat2", BuiltInType.MAT2, GenTypes.GenScalarType, BuiltInType.VEC2, GenTypes.GenScalarType);
        function("mat2", BuiltInType.MAT2, GenTypes.GenScalarType, GenTypes.GenScalarType, BuiltInType.VEC2);

        function("mat3", BuiltInType.MAT3, GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType,
            GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType,
            GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType);

        function("mat4", BuiltInType.MAT4, GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType,
            GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType,
            GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType,
            GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType);

        // Angle and Trigonometry Functions
        function("radians", GenTypes.GenType, GenTypes.GenType);
        function("degrees", GenTypes.GenType, GenTypes.GenType);
        function("sin", GenTypes.GenType, GenTypes.GenType);
        function("cos", GenTypes.GenType, GenTypes.GenType);
        function("tan", GenTypes.GenType, GenTypes.GenType);
        function("asin", GenTypes.GenType, GenTypes.GenType);
        function("acos", GenTypes.GenType, GenTypes.GenType);
        function("atan", GenTypes.GenType, GenTypes.GenType);
        function("sinh", GenTypes.GenType, GenTypes.GenType);
        function("cosh", GenTypes.GenType, GenTypes.GenType);
        function("tanh", GenTypes.GenType, GenTypes.GenType);
        function("asinh", GenTypes.GenType, GenTypes.GenType);
        function("acosh", GenTypes.GenType, GenTypes.GenType);
        function("atanh", GenTypes.GenType, GenTypes.GenType);

        // Exponential Functions
        function("pow", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType);
        function("exp", GenTypes.GenType, GenTypes.GenType);
        function("log", GenTypes.GenType, GenTypes.GenType);
        function("exp2", GenTypes.GenType, GenTypes.GenType);
        function("log2", GenTypes.GenType, GenTypes.GenType);
        function("sqrt", GenTypes.GenType, GenTypes.GenType);
        function("inversesqrt", GenTypes.GenType, GenTypes.GenType);

        // Common Functions
        function("abs", GenTypes.GenType, GenTypes.GenType);
        function("sign", GenTypes.GenType, GenTypes.GenType);
        function("floor", GenTypes.GenType, GenTypes.GenType);
        function("trunc", GenTypes.GenType, GenTypes.GenType);
        function("round", GenTypes.GenType, GenTypes.GenType);
        function("roundEven", GenTypes.GenType, GenTypes.GenType);
        function("ceil", GenTypes.GenType, GenTypes.GenType);
        function("fract", GenTypes.GenType, GenTypes.GenType);
        function("mod", GenTypes.GenType, GenTypes.GenType, BuiltInType.FLOAT);
        function("mod", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType);
        function("modf", GenTypes.GenType, GenTypes.GenType, BuiltInType.FLOAT);
        function("modf", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType);

        function("min", GenTypes.GenType, GenTypes.GenType, BuiltInType.INT);
        function("min", GenTypes.GenType, GenTypes.GenType, BuiltInType.FLOAT);
        function("min", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType);
        function("max", GenTypes.GenType, GenTypes.GenType, BuiltInType.INT);
        function("max", GenTypes.GenType, GenTypes.GenType, BuiltInType.FLOAT);
        function("max", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType);

        function("clamp", GenTypes.GenType, GenTypes.GenType, BuiltInType.FLOAT, BuiltInType.FLOAT);
        function("clamp", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType, GenTypes.GenType);
        function("mix", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType, BuiltInType.FLOAT);
        function("mix", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType, GenTypes.GenType);

        function("step", GenTypes.GenType, BuiltInType.FLOAT, GenTypes.GenType);
        function("step", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType);
        function("smoothstep", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType, BuiltInType.FLOAT);
        function("smoothstep", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType, GenTypes.GenType);
        function("smoothstep", GenTypes.GenType, BuiltInType.FLOAT, BuiltInType.FLOAT, GenTypes.GenType);

        // isnan
        // isinf
        function("fma", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType, GenTypes.GenType);

        // Geometric Functions
        function("length", BuiltInType.FLOAT, GenTypes.GenType);
        function("distance", BuiltInType.FLOAT, GenTypes.GenType);
        function("dot", BuiltInType.FLOAT, GenTypes.GenType, GenTypes.GenType);
        fixedFunction("cross", BuiltInType.VEC3, BuiltInType.VEC3, BuiltInType.VEC3);
        function("normalize", GenTypes.GenType, GenTypes.GenType);
        function("faceforward", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType, GenTypes.GenType);
        function("reflect", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType);
        function("refract", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType, BuiltInType.FLOAT);

        // Matrix Functions
        function("inverse", GenTypes.GenMatrixType, GenTypes.GenMatrixType);

        // Vector Relational Functions

        // Integer Functions

        // Texture Functions
        fixedFunction("texture2D", BuiltInType.VEC4, BuiltInType.SAMPLER2D, BuiltInType.VEC2);
        fixedFunction("texture", BuiltInType.VEC4, BuiltInType.SAMPLER2D, BuiltInType.VEC2);
        fixedFunction("texture", BuiltInType.VEC4, BuiltInType.SAMPLER3D, BuiltInType.VEC3);

        // Derivative Functions
        function("dFdx", GenTypes.GenType, GenTypes.GenType);
        function("dFdy", GenTypes.GenType, GenTypes.GenType);
        function("fwidth", GenTypes.GenType, GenTypes.GenType);

        // Interpolation Functions
    }

    private VariableDeclarationNode variable(BuiltInType type, String identifier) {
        return new VariableDeclarationNode(true, new FullySpecifiedType(type), identifier, null, null);
    }

    private void fixedFunction(String identifier, BuiltInType returnType, BuiltInType... parameters) {
        final FunctionPrototypeNode node = new FunctionPrototypeNode(identifier, new FullySpecifiedType(returnType));
        node.setPrototype(new FunctionPrototype(true, returnType, parameters));
        functionRegistry.declare(node);
    }

    private void function(String identifier, Object... parameterTypes) {
        final GenTypeIterator iterator = new GenTypeIterator(parameterTypes);
        while (iterator.hasNext()) {
            final BuiltInType[] parameters = iterator.next();
            final BuiltInType returnType = parameters[0];
            final BuiltInType[] arguments = Arrays.copyOfRange(parameters, 1, parameters.length);

            final FunctionPrototypeNode node = new FunctionPrototypeNode(identifier, new FullySpecifiedType(returnType));
            node.setPrototype(new FunctionPrototype(true, returnType, arguments));
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

    @Override
    public void dereferenceTree(Node node) {
        node.accept(dereferenceVisitor);
    }

    @Override
    public GLSLContext findContext(Node node) {
        if (node instanceof GLSLContext) {
            return (GLSLContext) node;
        }
        final ParentNode parentNode = node.getParentNode();
        if (parentNode != null) {
            return findContext(parentNode);
        }
        return globalContext();
    }

    @Override
    public ParserContext remap(Node base) {
        final TypeRegistry typeRegistryRemap = typeRegistry.remap(base);
        final ContextAware contextAwareRemap = contextAware.remap(base);
        final VariableRegistry variableRegistryCopy = variableRegistry.remap(base, contextAwareRemap);
        final FunctionRegistry functionRegistryCopy = functionRegistry.remap(base);
        return new ParserContextImpl(typeRegistryRemap, variableRegistryCopy, functionRegistryCopy, contextAwareRemap);
    }

    @Override
    public GLSLContext currentContext() {
        return contextAware.currentContext();
    }

    @Override
    public GLSLContext enterContext(GLSLContext context) {
        return contextAware.enterContext(context);
    }

    @Override
    public GLSLContext exitContext() {
        return contextAware.exitContext();
    }

    @Override
    public GLSLContext globalContext() {
        return contextAware.globalContext();
    }

    @Override
    public Set<GLSLContext> contexts() {
        return contextAware.contexts();
    }
}
