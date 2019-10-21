package com.tazadum.glsl.eval;

import com.tazadum.glsl.cli.builder.CompilerExecutor;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.type.FullySpecifiedType;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.parser.GLSLParser;
import com.tazadum.glsl.util.SourcePosition;

import java.util.HashMap;
import java.util.Map;

public class EvaluatorBuilder {
    private CompilerExecutor compilerExecutor;
    private Map<String, VariableDeclarationNode> variableMap;

    public static EvaluatorBuilder expression(String source) {
        return new EvaluatorBuilder(
                CompilerExecutor.create()
                        .source(source)
                        .parserRuleProvider(GLSLParser::expression)
        );
    }

    public EvaluatorBuilder(CompilerExecutor compilerExecutor) {
        this.compilerExecutor = compilerExecutor;
        this.variableMap = new HashMap<>();
    }

    public EvaluatorBuilder variable(String identifier, GLSLType type) {
        final VariableDeclarationNode variableDeclaration = new VariableDeclarationNode(
                SourcePosition.TOP,
                true,
                new FullySpecifiedType(type),
                identifier,
                null,
                null,
                null
        );

        variableMap.put(identifier, variableDeclaration);

        compilerExecutor.withVariableRegistry((context, registry) -> registry.declareVariable(
                context.globalContext(),
                variableDeclaration
        ));

        return this;
    }

    public Evaluator build() {
        CompilerExecutor.Result result = compilerExecutor.process();
        return new DefaultEvaluator(result, variableMap);
    }
}
