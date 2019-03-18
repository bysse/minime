package com.tazadum.glsl.language.function;

import com.tazadum.glsl.exception.TypeException;
import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.language.model.StorageQualifier;
import com.tazadum.glsl.language.type.*;
import com.tazadum.glsl.parser.TypeCombination;
import com.tazadum.glsl.preprocessor.model.HasToken;
import com.tazadum.glsl.util.SourcePosition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by erikb on 2018-10-23.
 */
public class BuiltInFunctionRegistryImpl implements BuiltInFunctionRegistry, BuiltInFunctionRegistry.FunctionDeclarator {
    private ConcurrentMap<String, List<FunctionPrototypeNode>> functionMap;

    public BuiltInFunctionRegistryImpl() {
        this.functionMap = new ConcurrentHashMap<>(2500);
    }

    @Override
    public FunctionDeclarator getFunctionDeclarator() {
        return this;
    }

    @Override
    public FunctionPrototypeNode resolve(Identifier identifier, GLSLType... parameters) {
        return resolve(identifier, new FunctionPrototypeMatcher(FunctionPrototypeMatcher.ANY, parameters));
    }

    @Override
    public FunctionPrototypeNode resolve(Identifier identifier, FunctionPrototypeMatcher prototypeMatcher) {
        final List<FunctionPrototypeNode> prototypeNodes = functionMap.get(identifier.original());

        if (prototypeNodes != null && !prototypeNodes.isEmpty()) {
            for (FunctionPrototypeNode prototypeNode : prototypeNodes) {
                if (prototypeMatcher.matches(prototypeNode.getPrototype())) {
                    return prototypeNode;
                }
            }
        }

        final PredefinedType predefinedType = HasToken.fromString(identifier.original(), PredefinedType.values());
        if (predefinedType != null && !TypeCombination.ofCategory(TypeCategory.Opaque, predefinedType)) {

            // attempt at resolving matrix constructors
            if (TypeCombination.ofCategory(TypeCategory.Matrix, predefinedType)) {
                final GLSLType[] parameterTypes = prototypeMatcher.getParameterTypes();

                // count the number of components that are present in the specified constructor

                int parameters = 0;
                for (GLSLType parameterType : parameterTypes) {
                    if (!TypeCombination.ofAnyCategory(parameterType, TypeCategory.Scalar, TypeCategory.Vector)) {

                        // we only scalars and matrices in the constructor

                        parameters = 0;
                        break;
                    }

                    final PredefinedType type = (PredefinedType)parameterType;
                    parameters += type.components();

                    // verify that the types are compatible with the matrix base type
                    if (null == TypeCombination.compatibleTypeNoException(type.baseType(), type.baseType())) {

                        // the components can't be assigned to each other

                        parameters = 0;
                        break;
                    }
                }

                if (parameters == predefinedType.columns() * predefinedType.rows()) {
                    // this is a match, build a function prototype for it
                    final FunctionPrototypeNode prototypeNode = new FunctionPrototypeNode(
                            SourcePosition.TOP,
                            identifier.original(),
                            new FullySpecifiedType(predefinedType)
                    );
                    prototypeNode.setPrototype(new FunctionPrototype(true, predefinedType, parameterTypes));

                    // register the function
                    function(prototypeNode);
                    return prototypeNode;
                }
            }

            // create array constructor nodes on the fly
            if (!prototypeMatcher.hasWildcards()) {
                final GLSLType[] parameterTypes = prototypeMatcher.getParameterTypes();
                final int parameterCount = prototypeMatcher.getParameterCount();
                final GLSLType type = new ArrayType(predefinedType, parameterCount);

                final FunctionPrototypeNode prototypeNode = new FunctionPrototypeNode(
                    SourcePosition.TOP,
                    identifier.original(),
                    new FullySpecifiedType(type)
                );
                prototypeNode.setPrototype(new FunctionPrototype(true, type, parameterTypes));

                // register the function
                function(prototypeNode);
                return prototypeNode;
            }
        }

        return null;
    }

    public void function(FunctionPrototypeNode node) {
        functionMap.computeIfAbsent(node.getIdentifier().original(), (identifier) -> new ArrayList<>()).add(node);
    }

    @Override
    public void function(String identifier, Object... parameterTypes) {
        final GenTypeIterator iterator = new GenTypeIterator(parameterTypes);

        while (iterator.hasNext()) {
            final GLSLType[] parameters = iterator.next();
            final GLSLType returnType = parameters[0];
            final GLSLType[] params = Arrays.copyOfRange(parameters, 1, parameters.length);

            final FunctionPrototypeNode node = new FunctionPrototypeNode(
                SourcePosition.TOP,
                identifier,
                new FullySpecifiedType(returnType)
            );

            StorageQualifier[] qualifiers = null;
            boolean foundQualifier = false;
            for (int i = 0; i < params.length; i++) {
                if (params[i] instanceof GenTypeIterator.OutWrapper) {
                    params[i] = params[i].baseType();
                    foundQualifier = true;
                }
            }

            if (foundQualifier) {
                qualifiers = new StorageQualifier[params.length];
                for (int i = 0; i < params.length; i++) {
                    if (params[i] instanceof GenTypeIterator.OutWrapper) {
                        qualifiers[i] = StorageQualifier.OUT;
                    } else {
                        qualifiers[i] = StorageQualifier.IN;
                    }
                }
            }

            node.setPrototype(new FunctionPrototype(true, returnType, params, qualifiers));

            function(node);
        }
    }
}
