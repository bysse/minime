package com.tazadum.glsl.parser.visitor;

import com.tazadum.glsl.ast.BooleanLeafNode;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ParenthesisNode;
import com.tazadum.glsl.ast.StatementListNode;
import com.tazadum.glsl.ast.arithmetic.FloatLeafNode;
import com.tazadum.glsl.ast.arithmetic.IntLeafNode;
import com.tazadum.glsl.ast.arithmetic.Numeric;
import com.tazadum.glsl.ast.expression.AssignmentNode;
import com.tazadum.glsl.ast.function.FunctionCallNode;
import com.tazadum.glsl.ast.function.FunctionDefinitionNode;
import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.ast.variable.VariableNode;
import com.tazadum.glsl.language.AssignmentOperator;
import com.tazadum.glsl.language.GLSLBaseVisitor;
import com.tazadum.glsl.language.GLSLParser;
import com.tazadum.glsl.language.HasToken;
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

        // parse the function declaration
        FunctionDeclarationVisitor declarationVisitor = new FunctionDeclarationVisitor(parserContext);
        FunctionPrototypeNode functionPrototype = (FunctionPrototypeNode) ctx.function_prototype().accept(declarationVisitor);

        StatementListNode statementList = null;

        GLSLParser.Compound_statement_no_new_scopeContext statements = ctx.compound_statement_no_new_scope();
        if (statements != null) {
            statementList = (StatementListNode) statements.accept(this);
        }

        final FunctionDefinitionNode functionDefinition = new FunctionDefinitionNode(functionPrototype, statementList);

        parserContext.exitContext();

        return functionDefinition;
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
    public Node visitCompound_statement_no_new_scope(GLSLParser.Compound_statement_no_new_scopeContext ctx) {
        StatementListNode statementList = new StatementListNode();

        if (ctx.statement_list() != null) {
            // go through all of the statements
            for (GLSLParser.Statement_no_new_scopeContext statementScope : ctx.statement_list().statement_no_new_scope()) {
                final Node node = statementScope.accept(this);
                statementList.addChild(node);
            }
        }

        return statementList;
    }

    @Override
    public Node visitAssignment_expression(GLSLParser.Assignment_expressionContext ctx) {
        if (ctx.conditional_expression() != null) {
            return ctx.conditional_expression().accept(this);
        }

        final Node lparam = ctx.unary_expression().accept(this);
        final AssignmentOperator operator = HasToken.match(ctx.assignment_operator(), AssignmentOperator.values());
        final Node rparam = ctx.assignment_expression().accept(this);

        return new AssignmentNode(lparam, operator, rparam);
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
