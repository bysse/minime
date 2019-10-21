package com.tazadum.glsl.eval;

import com.tazadum.glsl.cli.builder.CompilerExecutor;
import com.tazadum.glsl.eval.type.TypedVariable;
import com.tazadum.glsl.eval.visitor.ExpressionEvaluationVisitor;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;

import java.util.HashMap;
import java.util.Map;

public class DefaultEvaluator implements Evaluator {
    private final Map<String, VariableDeclarationNode> variableMap;
    private final Map<VariableDeclarationNode, TypedVariable> valueMap;
    private final Node node;

    public DefaultEvaluator(CompilerExecutor.Result result, Map<String, VariableDeclarationNode> variableMap) {
        this.node = result.getNode();
        this.variableMap = variableMap;
        this.valueMap = new HashMap<>();
    }

    @Override
    public Evaluator setVariable(String identifier, TypedVariable typedVariable) {
        VariableDeclarationNode declarationNode = variableMap.get(identifier);
        if (declarationNode == null) {
            throw new IllegalArgumentException("The variable '" + identifier + "' was not registered as an Evaluator variable.");
        }

        if (!declarationNode.getType().isAssignableBy(typedVariable.getType())) {
            throw new IllegalArgumentException("The variable type " + declarationNode.getType() + " is not assignable by " + typedVariable.getType());
        }

        valueMap.put(declarationNode, typedVariable);
        return this;
    }

    @Override
    public TypedVariable run() {
        ExpressionEvaluationVisitor visitor = new ExpressionEvaluationVisitor(valueMap);
        node.accept(visitor);
        return visitor.getResult();
    }
}
