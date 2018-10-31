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
import com.tazadum.glsl.parser.variables.*;
import com.tazadum.glsl.preprocessor.language.GLSLProfile;
import com.tazadum.glsl.util.SourcePosition;

import java.util.Arrays;
import java.util.Set;

public class ParserContextImpl implements ParserContext {
    private final TypeRegistry typeRegistry;
    private final VariableRegistry variableRegistry;
    private final FunctionRegistry functionRegistry;
    private final ContextAware contextAware;

    private final ContextVisitor contextVisitor;
    private final DereferencingVisitor dereferencingVisitor;
    private final ReferencingVisitor referencingVisitor;
    private final TypeVisitor typeVisitor;

    public ParserContextImpl(TypeRegistry typeRegistry, VariableRegistry variableRegistry, FunctionRegistry functionRegistry) {
        this(typeRegistry, variableRegistry, functionRegistry, new ContextAwareImpl());
    }

    public ParserContextImpl(TypeRegistry typeRegistry, VariableRegistry variableRegistry, FunctionRegistry functionRegistry, ContextAware contextAware) {
        this.typeRegistry = typeRegistry;
        this.variableRegistry = variableRegistry;
        this.functionRegistry = functionRegistry;
        this.contextAware = contextAware;

        this.contextVisitor = new ContextVisitor();
        this.dereferencingVisitor = new DereferencingVisitor(this);
        this.referencingVisitor = new ReferencingVisitor(this);
        this.typeVisitor = new TypeVisitor(this);
    }

    public TypeVisitor getTypeVisitor() {
        return typeVisitor;
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
            final GLSLType[] parameters = iterator.next();
            final GLSLType returnType = parameters[0];
            final GLSLType[] arguments = Arrays.copyOfRange(parameters, 1, parameters.length);

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

    @Override
    public void dereferenceTree(Node node) {
        node.accept(dereferencingVisitor);
    }

    @Override
    public void referenceTree(Node node) {
        // make sure the context nodes are connected
        node.accept(contextVisitor);

        // add type information to the tree and register function-calls
        node.accept(typeVisitor);

        // register variable usages
        node.accept(referencingVisitor);
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
        return new ParserContextImpl(typeRegistryRemap, variableRegistryCopy, functionRegistryCopy, contextAware);
    }

    @Override
    public void initializeVariables(ShaderType shaderType, GLSLProfile profile) {
        switch (shaderType) {
            case COMPUTE:
                variableRegistry.apply(globalContext(), new ComputeShaderVariableSet(profile));
                break;
            case VERTEX:
                variableRegistry.apply(globalContext(), new VertexShaderVariableSet(profile));
                break;
            case FRAGMENT:
                variableRegistry.apply(globalContext(), new FragmentShaderVariableSet(profile));
                break;
            case SHADER_TOY:
                variableRegistry.apply(globalContext(), new ShaderToyVariableSet(profile));
                break;
            case GEOMETRY:
                variableRegistry.apply(globalContext(), new GeometryShaderVariableSet(profile));
                break;
            case TESSELLATION_EVALUATION:
                variableRegistry.apply(globalContext(), new TessellationShaderVariableSet(profile));
                break;
            case TESSELLATION_CONTROL:
                variableRegistry.apply(globalContext(), new TessellationControlShaderVariableSet(profile));
                break;
            default:
                throw new IllegalArgumentException("Unknown shader type " + shaderType);
        }
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
    public boolean removeContext(GLSLContext context) {
        if (contextAware.removeContext(context)) {
            variableRegistry.dereference(context);
            return true;
        }
        return false;
    }

    @Override
    public void addContext(GLSLContext context) {
        contextAware.addContext(context);
    }

    @Override
    public Set<GLSLContext> contexts() {
        return contextAware.contexts();
    }
}
