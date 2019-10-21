package com.tazadum.glsl.eval.visitor;

import com.tazadum.glsl.eval.functions.GLSLFunctions;
import com.tazadum.glsl.eval.type.TypedFloat;
import com.tazadum.glsl.eval.type.TypedVariable;
import com.tazadum.glsl.exception.SourcePositionException;
import com.tazadum.glsl.language.ast.DefaultASTVisitor;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.arithmetic.NumericLeafNode;
import com.tazadum.glsl.language.ast.arithmetic.NumericOperationNode;
import com.tazadum.glsl.language.ast.function.FunctionCallNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.ast.variable.VariableNode;

import java.util.Map;

public class ExpressionEvaluationVisitor extends DefaultASTVisitor<TypedVariable> {
    private TypedVariable result;
    private Map<VariableDeclarationNode, TypedVariable> valueMap;

    public ExpressionEvaluationVisitor(Map<VariableDeclarationNode, TypedVariable> valueMap) {
        this.valueMap = valueMap;
    }

    public TypedVariable getResult() {
        return result;
    }

    private TypedVariable store(Node node, TypedVariable value) {
        if (value == null) {
            throw new SourcePositionException(node.getSourcePosition(), "Result value is null");
        }

        result = value;
        return value;
    }

    private TypedVariable[] arguments(ParentNode node) {
        final int count = node.getChildCount();
        final TypedVariable[] arguments = new TypedVariable[count];
        for (int i = 0; i < count; i++) {
            arguments[i] = node.getChild(i).accept(this);
        }
        return arguments;
    }

    @Override
    public TypedVariable visitVariable(VariableNode node) {
        TypedVariable value = valueMap.get(node.getDeclarationNode());

        if (value == null) {
            final String message = String.format("Variable '%s' has no value", node.getDeclarationNode().getIdentifier().original());
            throw new SourcePositionException(node.getSourcePosition(), message);
        }

        return store(node, value);
    }

    @Override
    public TypedVariable visitFunctionCall(FunctionCallNode node) {
        final TypedVariable[] arguments = arguments(node);
        final String functionName = node.getIdentifier().original();
        try {
            switch (functionName) {
                case "sin":
                    return store(node, GLSLFunctions.sin(arguments));
                case "cos":
                    return store(node, GLSLFunctions.cos(arguments));
            }
        } catch (IllegalArgumentException e) {
            throw new SourcePositionException(node.getSourcePosition(), e.getMessage());
        }

        throw new SourcePositionException(node.getSourcePosition(), "Unsupported function '" + functionName + "'");
    }

    @Override
    public TypedVariable visitNumeric(NumericLeafNode node) {
        return new TypedFloat((float)node.getValue().doubleValue());
    }

    @Override
    public TypedVariable visitNumericOperation(NumericOperationNode node) {
        final TypedVariable[] arguments = arguments(node);

        switch (node.getOperator()) {
            case ADD:
                return store(node, GLSLFunctions.add(arguments));
            case SUB:
                return store(node, GLSLFunctions.sub(arguments));
        }

        throw new SourcePositionException(node.getSourcePosition(), "Unsupported operator '" + node.getOperator() + "'");
    }
}
