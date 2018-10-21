package com.tazadum.glsl.parser;


import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.context.ContextAware;
import com.tazadum.glsl.language.context.ContextAwareImpl;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.language.function.FunctionPrototype;
import com.tazadum.glsl.language.function.FunctionRegistry;
import com.tazadum.glsl.language.type.*;
import com.tazadum.glsl.language.variable.VariableRegistry;
import com.tazadum.glsl.util.SourcePosition;

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

        variableRegistry.declareVariable(context, variable(PredefinedType.VEC4, "gl_FragColor"));
        variableRegistry.declareVariable(context, variable(PredefinedType.VEC4, "gl_FragCoord"));
        variableRegistry.declareVariable(context, variable(PredefinedType.BOOL, "gl_FrontFacing"));
        variableRegistry.declareVariable(context, variable(PredefinedType.VEC2, "gl_PointCoord"));
        variableRegistry.declareVariable(context, variable(PredefinedType.FLOAT, "gl_FragDepth"));
    }

    private void setupFunctions() {
        // scalar type construction
        fixedFunction("float", PredefinedType.FLOAT, PredefinedType.FLOAT);
        fixedFunction("float", PredefinedType.FLOAT, PredefinedType.INT);
        fixedFunction("int", PredefinedType.INT, PredefinedType.FLOAT);
        fixedFunction("int", PredefinedType.INT, PredefinedType.INT);

        // vector construction
        fixedFunction("vec2", PredefinedType.VEC2, PredefinedType.VEC2);
        function("vec2", PredefinedType.VEC2, GenTypes.GenScalarType);
        function("vec2", PredefinedType.VEC2, GenTypes.GenScalarType, GenTypes.GenScalarType);

        // vec3
        fixedFunction("vec3", PredefinedType.VEC3, PredefinedType.VEC3);
        function("vec3", PredefinedType.VEC3, GenTypes.GenScalarType);
        function("vec3", PredefinedType.VEC3, GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType);
        function("vec3", PredefinedType.VEC3, PredefinedType.VEC2, GenTypes.GenScalarType);
        function("vec3", PredefinedType.VEC3, GenTypes.GenScalarType, PredefinedType.VEC2);
        // vec4
        fixedFunction("vec4", PredefinedType.VEC4, PredefinedType.VEC4);
        fixedFunction("vec4", PredefinedType.VEC4, PredefinedType.VEC2, PredefinedType.VEC2);
        function("vec4", PredefinedType.VEC4, GenTypes.GenScalarType);
        function("vec4", PredefinedType.VEC4, GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType);
        function("vec4", PredefinedType.VEC4, PredefinedType.VEC3, GenTypes.GenScalarType);
        function("vec4", PredefinedType.VEC4, GenTypes.GenScalarType, PredefinedType.VEC3);
        function("vec4", PredefinedType.VEC4, PredefinedType.VEC2, GenTypes.GenScalarType, GenTypes.GenScalarType);
        function("vec4", PredefinedType.VEC4, GenTypes.GenScalarType, PredefinedType.VEC2, GenTypes.GenScalarType);
        function("vec4", PredefinedType.VEC4, GenTypes.GenScalarType, GenTypes.GenScalarType, PredefinedType.VEC2);

        // naive matrix construction
        function("mat2", PredefinedType.MAT2, GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType);
        function("mat2", PredefinedType.MAT2, PredefinedType.VEC2, GenTypes.GenScalarType, GenTypes.GenScalarType);
        function("mat2", PredefinedType.MAT2, GenTypes.GenScalarType, PredefinedType.VEC2, GenTypes.GenScalarType);
        function("mat2", PredefinedType.MAT2, GenTypes.GenScalarType, GenTypes.GenScalarType, PredefinedType.VEC2);

        function("mat3", PredefinedType.MAT3, GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType,
            GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType,
            GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType);

        function("mat4", PredefinedType.MAT4, GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType,
            GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType,
            GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType,
            GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType, GenTypes.GenScalarType);
        fixedFunction("mat4", PredefinedType.MAT4, PredefinedType.VEC4, PredefinedType.VEC4, PredefinedType.VEC4, PredefinedType.VEC4);


        // Angle and Trigonometry Functions
        function("radians", GenTypes.GenType, GenTypes.GenType);
        function("degrees", GenTypes.GenType, GenTypes.GenType);
        function("sin", GenTypes.GenType, GenTypes.GenType);
        function("cos", GenTypes.GenType, GenTypes.GenType);
        function("tan", GenTypes.GenType, GenTypes.GenType);
        function("asin", GenTypes.GenType, GenTypes.GenType);
        function("acos", GenTypes.GenType, GenTypes.GenType);
        function("atan", GenTypes.GenType, GenTypes.GenType);
        function("atan", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType);
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
        function("mod", GenTypes.GenType, GenTypes.GenType, PredefinedType.FLOAT);
        function("mod", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType);
        function("modf", GenTypes.GenType, GenTypes.GenType, PredefinedType.FLOAT);
        function("modf", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType);

        function("min", GenTypes.GenType, GenTypes.GenType, PredefinedType.INT);
        function("min", GenTypes.GenType, GenTypes.GenType, PredefinedType.FLOAT);
        function("min", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType);
        function("max", GenTypes.GenType, GenTypes.GenType, PredefinedType.INT);
        function("max", GenTypes.GenType, GenTypes.GenType, PredefinedType.FLOAT);
        function("max", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType);

        function("clamp", GenTypes.GenType, GenTypes.GenType, PredefinedType.FLOAT, PredefinedType.FLOAT);
        function("clamp", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType, GenTypes.GenType);
        function("mix", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType, PredefinedType.FLOAT);
        function("mix", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType, GenTypes.GenType);

        function("step", GenTypes.GenType, PredefinedType.FLOAT, GenTypes.GenType);
        function("step", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType);
        function("smoothstep", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType, PredefinedType.FLOAT);
        function("smoothstep", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType, GenTypes.GenType);
        function("smoothstep", GenTypes.GenType, PredefinedType.FLOAT, PredefinedType.FLOAT, GenTypes.GenType);

        // isnan
        // isinf
        function("fma", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType, GenTypes.GenType);

        // Geometric Functions
        function("length", PredefinedType.FLOAT, GenTypes.GenType);
        function("distance", PredefinedType.FLOAT, GenTypes.GenType);
        function("dot", PredefinedType.FLOAT, GenTypes.GenType, GenTypes.GenType);
        fixedFunction("cross", PredefinedType.VEC3, PredefinedType.VEC3, PredefinedType.VEC3);
        function("normalize", GenTypes.GenType, GenTypes.GenType);
        function("faceforward", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType, GenTypes.GenType);
        function("reflect", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType);
        function("refract", GenTypes.GenType, GenTypes.GenType, GenTypes.GenType, PredefinedType.FLOAT);

        // Matrix Functions
        function("inverse", GenTypes.GenMatrixType, GenTypes.GenMatrixType);

        // Vector Relational Functions

        // Integer Functions

        // Texture Functions
        fixedFunction("texture2D", PredefinedType.VEC4, PredefinedType.SAMPLER2D, PredefinedType.VEC2);
        fixedFunction("texture", PredefinedType.VEC4, PredefinedType.SAMPLER2D, PredefinedType.VEC2);
        fixedFunction("texture", PredefinedType.VEC4, PredefinedType.SAMPLER3D, PredefinedType.VEC3);

        // Derivative Functions
        function("dFdx", GenTypes.GenType, GenTypes.GenType);
        function("dFdy", GenTypes.GenType, GenTypes.GenType);
        function("fwidth", GenTypes.GenType, GenTypes.GenType);

        // Interpolation Functions
    }

    private VariableDeclarationNode variable(PredefinedType type, String identifier) {
        return new VariableDeclarationNode(SourcePosition.TOP, true, new FullySpecifiedType(type), identifier, null, null, null);
    }

    private void fixedFunction(String identifier, PredefinedType returnType, PredefinedType... parameters) {
        final FunctionPrototypeNode node = new FunctionPrototypeNode(SourcePosition.TOP, identifier, new FullySpecifiedType(returnType));
        node.setPrototype(new FunctionPrototype(true, returnType, parameters));
        functionRegistry.declareFunction(node);
    }

    private void function(String identifier, Object... parameterTypes) {
        final GenTypeIterator iterator = new GenTypeIterator(parameterTypes);
        while (iterator.hasNext()) {
            final PredefinedType[] parameters = iterator.next();
            final PredefinedType returnType = parameters[0];
            final PredefinedType[] arguments = Arrays.copyOfRange(parameters, 1, parameters.length);

            final FunctionPrototypeNode node = new FunctionPrototypeNode(SourcePosition.TOP, identifier, new FullySpecifiedType(returnType));
            node.setPrototype(new FunctionPrototype(true, returnType, arguments));
            functionRegistry.declareFunction(node);
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

    //@Override
    //public BranchRegistry getBranchRegistry() {
    //    return branchRegistry;
    //}

    @Override
    public void dereferenceTree(Node node) {
        node.accept(dereferenceVisitor);
    }

    @Override
    public void referenceTree(Node node) {
        assert false : "Not implemented";

        // add type information to the tree
        //node.accept(new TypeVisitor(this));

        // register all function-calls and variable usages
        //node.accept(new VariableReferenceVisitor(this));
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
        //final BranchRegistry branchRegistryCopy = branchRegistry.remap();
        //return new ParserContextImpl(typeRegistryRemap, variableRegistryCopy, functionRegistryCopy, branchRegistryCopy, contextAwareRemap);
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
