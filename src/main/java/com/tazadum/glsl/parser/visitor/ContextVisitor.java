package com.tazadum.glsl.parser.visitor;

import com.tazadum.glsl.ast.BooleanLeafNode;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ParenthesisNode;
import com.tazadum.glsl.ast.arithmetic.FloatLeafNode;
import com.tazadum.glsl.ast.arithmetic.IntLeafNode;
import com.tazadum.glsl.ast.arithmetic.Numeric;
import com.tazadum.glsl.ast.function.FunctionCallNode;
import com.tazadum.glsl.ast.variable.VariableNode;
import com.tazadum.glsl.language.GLSLBaseVisitor;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.listener.VariableDeclarationListener;
import com.tazadum.glsl.parser.variable.ResolutionResult;
import com.tazadum.glsl.parser.variable.VariableRegistry;

import java.util.ArrayList;
import java.util.List;

public class ContextVisitor extends GLSLBaseVisitor<Node> {
    private ParserContext parserContext;

    public ContextVisitor(ParserContext parserContext) {
        this.parserContext = parserContext;
    }

    @Override
    public Node visitFunction_definition(GLSLParser.Function_definitionContext ctx) {
        parserContext.enterContext();


        ctx.function_prototype();

        // TODO: parse the definition

        ContextVisitor contextVisitor = new ContextVisitor(parserContext);

        GLSLParser.Compound_statement_no_new_scopeContext statements = ctx.compound_statement_no_new_scope();
        if (statements != null) {
            Node functionBody = statements.accept(contextVisitor);

            // TODO: add functionBody to the definition
        }

        parserContext.exitContext();

        // TODO: return the function definition

        return null;
    }

    @Override
    public Node visitFunction_call(GLSLParser.Function_callContext ctx) {
        final String functionName = ctx.function_call_header().function_identifier().getText();
        final FunctionCallNode functionCallNode = new FunctionCallNode(functionName);

        // check if the function call has arguments
        if (ctx.VOID() == null) {
            final List<Node> arguments = new ArrayList<>();
            for (GLSLParser.Assignment_expressionContext argCtx : ctx.assignment_expression()) {
                // parse each argument and add them to the function
                final Node argument = argCtx.accept(this);
                functionCallNode.addChild(argument);
            }
        }

        return functionCallNode;
    }

    @Override
    public Node visitVariableDeclaration(GLSLParser.VariableDeclarationContext ctx) {
        VariableDeclarationListener listener = new VariableDeclarationListener(parserContext);
        listener.walk(ctx.init_declarator_list());
        return listener.getResult();
    }


    @Override
    public Node visitPrimary_expression(GLSLParser.Primary_expressionContext ctx) {
        if (ctx.INTCONSTANT() != null) {
            Numeric numeric = Numeric.create(ctx.INTCONSTANT().getText());
            return new IntLeafNode(numeric);
        }
        if (ctx.FLOATCONSTANT() != null) {
            Numeric numeric = Numeric.create(ctx.FLOATCONSTANT().getText());
            return new FloatLeafNode(numeric);
        }
        if (ctx.BOOLCONSTANT() != null) {
            return new BooleanLeafNode(Boolean.valueOf(ctx.BOOLCONSTANT().getText()));
        }
        if (ctx.LEFT_PAREN() != null) {
            final Node expression = ctx.expression().accept(this);
            return new ParenthesisNode(expression);
        }

        // resolve variable
        final VariableRegistry variableRegistry = parserContext.getVariableRegistry();
        final GLSLContext currentContext = parserContext.currentContext();

        final String variableName = ctx.variable_identifier().IDENTIFIER().getText();

        final ResolutionResult result = variableRegistry.resolve(currentContext, variableName);
        final VariableNode node = new VariableNode(result.getDeclaration());

        variableRegistry.usage(currentContext, variableName, node);

        return node;
    }
}
