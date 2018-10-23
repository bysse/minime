package com.tazadum.glsl.language.function;

import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.language.type.FullySpecifiedType;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.GenTypeIterator;
import com.tazadum.glsl.language.type.PredefinedType;
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
        this.functionMap = new ConcurrentHashMap<>(1000);
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

        return null;
    }

    public void function(FunctionPrototypeNode node) {
        functionMap.computeIfAbsent(node.getIdentifier().original(), (identifier) -> new ArrayList<>()).add(node);
    }

    @Override
    public void function(String identifier, Object... parameterTypes) {
        final GenTypeIterator iterator = new GenTypeIterator(parameterTypes);
        while (iterator.hasNext()) {
            final PredefinedType[] parameters = iterator.next();
            final PredefinedType returnType = parameters[0];
            final PredefinedType[] arguments = Arrays.copyOfRange(parameters, 1, parameters.length);

            final FunctionPrototypeNode node = new FunctionPrototypeNode(SourcePosition.TOP, identifier, new FullySpecifiedType(returnType));
            node.setPrototype(new FunctionPrototype(true, returnType, arguments));

            function(node);
        }
    }
}
