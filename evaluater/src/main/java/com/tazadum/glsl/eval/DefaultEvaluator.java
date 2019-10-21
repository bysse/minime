package com.tazadum.glsl.eval;

import com.tazadum.glsl.cli.builder.CompilerExecutor;
import com.tazadum.glsl.eval.type.TypedVariable;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.util.SourcePositionMapper;

import java.util.Map;

public class DefaultEvaluator implements Evaluator {
    private final Map<String, VariableDeclarationNode> variableMap;
    private final SourcePositionMapper sourcePositionMapper;
    private final Node node;

    public DefaultEvaluator(CompilerExecutor.Result result, Map<String, VariableDeclarationNode> variableMap) {
        this.sourcePositionMapper = result.getMapper();
        this.node = result.getNode();
        this.variableMap = variableMap;
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


        // TODO: store value;

        return this;
    }

    @Override
    public TypedVariable run() {
        return null;
    }
}
