package com.tazadum.glsl.parser.functions;

import com.tazadum.glsl.language.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.language.function.FunctionPrototype;
import com.tazadum.glsl.language.function.FunctionRegistry;
import com.tazadum.glsl.language.type.FullySpecifiedType;
import com.tazadum.glsl.language.type.GenTypeIterator;
import com.tazadum.glsl.language.type.PredefinedType;
import com.tazadum.glsl.util.SourcePosition;

import java.util.Arrays;

/**
 * Created by erikb on 2018-10-22.
 */
public abstract class FunctionSet {
    private final FunctionRegistry functionRegistry;

    public FunctionSet(FunctionRegistry functionRegistry) {
        this.functionRegistry = functionRegistry;
    }

    abstract public void generate();

    protected void fixedFunction(String identifier, PredefinedType returnType, PredefinedType... parameters) {
        final FunctionPrototypeNode node = new FunctionPrototypeNode(SourcePosition.TOP, identifier, new FullySpecifiedType(returnType));
        node.setPrototype(new FunctionPrototype(true, returnType, parameters));
        functionRegistry.declareFunction(node);
    }

    protected void function(String identifier, Object... parameterTypes) {
        final GenTypeIterator iterator = new GenTypeIterator(parameterTypes);
        while (iterator.hasNext()) {
            final PredefinedType[] parameters = iterator.next();
            final PredefinedType returnType = parameters[0];
            final PredefinedType[] arguments = Arrays.copyOfRange(parameters, 1, parameters.length);

            final FunctionPrototypeNode node = new FunctionPrototypeNode(SourcePosition.TOP, identifier, new FullySpecifiedType(returnType));
            node.setPrototype(new FunctionPrototype(true, returnType, arguments));

            // System.out.println("Declare: " + node.getIdentifier().original() + " = " + node.getPrototype());
            functionRegistry.declareFunction(node);
        }
    }
}
