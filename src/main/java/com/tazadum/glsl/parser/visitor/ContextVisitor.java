package com.tazadum.glsl.parser.visitor;

import com.tazadum.glsl.ast.BooleanLeafNode;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ParenthesisNode;
import com.tazadum.glsl.ast.StatementListNode;
import com.tazadum.glsl.ast.arithmetic.*;
import com.tazadum.glsl.ast.conditional.TernaryConditionNode;
import com.tazadum.glsl.ast.expression.AssignmentNode;
import com.tazadum.glsl.ast.function.FunctionCallNode;
import com.tazadum.glsl.ast.function.FunctionDefinitionNode;
import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.ast.variable.ArrayIndexNode;
import com.tazadum.glsl.ast.variable.FieldSelectionNode;
import com.tazadum.glsl.ast.variable.ParameterDeclarationNode;
import com.tazadum.glsl.ast.variable.VariableNode;
import com.tazadum.glsl.language.*;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.listener.VariableDeclarationListener;
import com.tazadum.glsl.parser.type.FullySpecifiedType;
import com.tazadum.glsl.parser.type.TypeHelper;
import com.tazadum.glsl.parser.variable.ResolutionResult;
import com.tazadum.glsl.parser.variable.VariableRegistry;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

public class ContextVisitor extends GLSLBaseVisitor<Node> {
    private ParserContext parserContext;

    public ContextVisitor(ParserContext parserContext) {
        this.parserContext = parserContext;
    }

    @Override
    public Node visitAdditiveExpression(GLSLParser.AdditiveExpressionContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitArray_specifier(GLSLParser.Array_specifierContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitAssignment_operator(GLSLParser.Assignment_operatorContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitCondition(GLSLParser.ConditionContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitConditional_expression(GLSLParser.Conditional_expressionContext ctx) {
        final Node condition = ctx.logical_expression().accept(this);
        if (ctx.expression() == null || ctx.assignment_expression() == null) {
            // this is not a conditional, so just pass through.
            return condition;
        }

        final Node thenNode = ctx.expression().accept(this);
        final Node elseNode = ctx.assignment_expression().accept(this);

        return new TernaryConditionNode(condition, thenNode, elseNode);
    }

    @Override
    public Node visitConstant_expression(GLSLParser.Constant_expressionContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitConstructor_identifier(GLSLParser.Constructor_identifierContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitDivisionExpression(GLSLParser.DivisionExpressionContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitDoIterationStatement(GLSLParser.DoIterationStatementContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitExternal_declaration(GLSLParser.External_declarationContext ctx) {
        return super.visitExternal_declaration(ctx);
    }

    @Override
    public Node visitField_selection(GLSLParser.Field_selectionContext ctx) {
        // TODO: implement
        throw new IllegalStateException("Should be handled in postfix_expression");
    }

    @Override
    public Node visitFor_init_statement(GLSLParser.For_init_statementContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitFor_rest_statement(GLSLParser.For_rest_statementContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitForIterationStatement(GLSLParser.ForIterationStatementContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitFully_specified_type(GLSLParser.Fully_specified_typeContext ctx) {
        throw new UnsupportedOperationException("Can't visit FullySpecifiedType. Please use TypeHelper::parseFullySpecifiedType");
    }

    @Override
    public Node visitFunction_call_header(GLSLParser.Function_call_headerContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitFunction_declarator(GLSLParser.Function_declaratorContext ctx) {
        final GLSLParser.Function_headerContext functionHeader = ctx.function_header();
        final String functionName = functionHeader.IDENTIFIER().getText();

        // parse the return type
        final FullySpecifiedType returnType = TypeHelper.parseFullySpecifiedType(functionHeader.fully_specified_type());
        final FunctionPrototypeNode functionDeclaration = new FunctionPrototypeNode(functionName, returnType);

        // parse the parameters
        for (GLSLParser.Parameter_declarationContext parameterCtx : ctx.parameter_declaration()) {
            final ParameterDeclarationNode parameter = (ParameterDeclarationNode) parameterCtx.accept(this);

            if (parameter.getIdentifier().isEmpty() && BuiltInType.VOID == parameter.getFullySpecifiedType().getType()) {
                // don't add void as a parameter node
                continue;
            }

            functionDeclaration.addChild(parameter);
        }

        // register the function
        parserContext.getFunctionRegistry().declare(functionDeclaration);

        return functionDeclaration;
    }


    @Override
    public Node visitFunction_header(GLSLParser.Function_headerContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitFunction_identifier(GLSLParser.Function_identifierContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitFunction_prototype(GLSLParser.Function_prototypeContext ctx) {
        // Done
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitFunctionDeclaration(GLSLParser.FunctionDeclarationContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitInit_declarator_list(GLSLParser.Init_declarator_listContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitInitializer(GLSLParser.InitializerContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitInteger_expression(GLSLParser.Integer_expressionContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitJump_statement(GLSLParser.Jump_statementContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitLogicalAnd(GLSLParser.LogicalAndContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitLogicalEquality(GLSLParser.LogicalEqualityContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitLogicalInEquality(GLSLParser.LogicalInEqualityContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitLogicalOr(GLSLParser.LogicalOrContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitLogicalXor(GLSLParser.LogicalXorContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitMultiplicationExpression(GLSLParser.MultiplicationExpressionContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitNumericDelegator(GLSLParser.NumericDelegatorContext ctx) {
        return super.visitNumericDelegator(ctx);
    }

    @Override
    public ParameterDeclarationNode visitParameter_declaration(GLSLParser.Parameter_declarationContext ctx) {
        // the parameter might not have an identifier
        final String parameterName = ctx.IDENTIFIER() == null ? null : ctx.IDENTIFIER().getText();

        // parse the type
        final FullySpecifiedType type = TypeHelper.parseFullySpecifiedType(ctx);

        // parse the array specifier
        Node arraySpecifier = null;
        if (ctx.array_specifier() != null) {
            ContextVisitor visitor = new ContextVisitor(parserContext);
            arraySpecifier = ctx.array_specifier().accept(visitor);
        }

        return new ParameterDeclarationNode(type, parameterName, arraySpecifier);
    }

    @Override
    public Node visitParameter_qualifier(GLSLParser.Parameter_qualifierContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitPostfix_expression(GLSLParser.Postfix_expressionContext ctx) {
        if (ctx.primary_expression_or_function_call() != null) {
            return ctx.primary_expression_or_function_call().accept(this);
        }

        // resolve the postfix_expression
        final Node expression = ctx.postfix_expression().accept(this);

        // handle array index
        if (ctx.integer_expression() != null) {
            final Node arrayIndex = ctx.integer_expression().accept(this);
            return new ArrayIndexNode(expression, arrayIndex);
        }

        // handle field selection
        if (ctx.field_selection() != null) {
            final String selection = ctx.field_selection().IDENTIFIER().getText();
            final FieldSelectionNode fieldSelectionNode = new FieldSelectionNode(selection);
            fieldSelectionNode.setExpression(expression);
            return fieldSelectionNode;
        }

        // handle the postfix operators
        if (ctx.INC_OP() != null) {
            final PostfixOperationNode operationNode = new PostfixOperationNode(UnaryOperator.INCREASE);
            operationNode.setExpression(expression);
            return operationNode;
        }

        if (ctx.DEC_OP() != null) {
            final PostfixOperationNode operationNode = new PostfixOperationNode(UnaryOperator.DECREASE);
            operationNode.setExpression(expression);
            return operationNode;
        }

        throw new IllegalStateException("Unknown/invalid postfix_expression");
    }

    @Override
    public Node visitPrecision_qualifier(GLSLParser.Precision_qualifierContext ctx) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitPrecisionDeclaration(GLSLParser.PrecisionDeclarationContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitPrimary_expression_or_function_call(GLSLParser.Primary_expression_or_function_callContext ctx) {
        return super.visitPrimary_expression_or_function_call(ctx);
    }

    @Override
    public Node visitRelational(GLSLParser.RelationalContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitSingle_declaration(GLSLParser.Single_declarationContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitStatement_list(GLSLParser.Statement_listContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitStatement_with_scope(GLSLParser.Statement_with_scopeContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitStruct_declaration(GLSLParser.Struct_declarationContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitStruct_declaration_list(GLSLParser.Struct_declaration_listContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitStruct_declarator(GLSLParser.Struct_declaratorContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitStruct_declarator_list(GLSLParser.Struct_declarator_listContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitStruct_specifier(GLSLParser.Struct_specifierContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitSubtractionExpression(GLSLParser.SubtractionExpressionContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitTranslation_unit(GLSLParser.Translation_unitContext ctx) {
        final StatementListNode listNode = new StatementListNode();
        for (GLSLParser.External_declarationContext externalContext : ctx.external_declaration()) {
            final Node node = externalContext.accept(this);
            listNode.addChild(node);
        }
        return listNode;
    }

    @Override
    public Node visitType_qualifier(GLSLParser.Type_qualifierContext ctx) {
        throw new UnsupportedOperationException("Please use HasToken::match or TypeHelper to parse this.");
    }

    @Override
    public Node visitType_specifier(GLSLParser.Type_specifierContext ctx) {
        throw new UnsupportedOperationException("Please use HasToken::match or TypeHelper to parse this.");
    }

    @Override
    public Node visitType_specifier_no_prec(GLSLParser.Type_specifier_no_precContext ctx) {
        throw new UnsupportedOperationException("Please use HasToken::match or TypeHelper to parse this.");
    }

    @Override
    public Node visitUnary_expression(GLSLParser.Unary_expressionContext ctx) {
        if (ctx.postfix_expression() != null) {
            return ctx.postfix_expression().accept(this);
        }

        final Node expression = ctx.unary_expression().accept(this);

        if (ctx.INC_OP() != null) {
            final PrefixOperationNode prefixOperationNode = new PrefixOperationNode(UnaryOperator.INCREASE);
            prefixOperationNode.setExpression(expression);
            return prefixOperationNode;
        }

        if (ctx.DEC_OP() != null) {
            final PrefixOperationNode prefixOperationNode = new PrefixOperationNode(UnaryOperator.DECREASE);
            prefixOperationNode.setExpression(expression);
            return prefixOperationNode;
        }

        final UnaryOperator operator = HasToken.match(ctx.unary_operator(), UnaryOperator.values());
        final UnaryOperationNode unaryOperationNode = new UnaryOperationNode(operator);
        unaryOperationNode.setExpression(expression);
        return unaryOperationNode;
    }

    @Override
    public Node visitUnary_operator(GLSLParser.Unary_operatorContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitUnaryExpression(GLSLParser.UnaryExpressionContext ctx) {
        return super.visitUnaryExpression(ctx);
    }

    @Override
    public Node visitVariable_identifier(GLSLParser.Variable_identifierContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitWhileIterationStatement(GLSLParser.WhileIterationStatementContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
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
                if (node instanceof StatementListNode) {
                    for (Node child : ((StatementListNode) node).getChildren()) {
                        statementList.addChild(child);
                    }
                } else {
                    statementList.addChild(node);
                }
            }
        }

        return statementList;
    }

    @Override
    public Node visitStatement_no_new_scope(GLSLParser.Statement_no_new_scopeContext ctx) {
        return super.visitStatement_no_new_scope(ctx);
    }

    @Override
    public Node visitCompound_statement_with_scope(GLSLParser.Compound_statement_with_scopeContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitSimple_statement(GLSLParser.Simple_statementContext ctx) {
        return super.visitSimple_statement(ctx);
    }

    @Override
    public Node visitDeclaration_statement(GLSLParser.Declaration_statementContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitExpression_statement(GLSLParser.Expression_statementContext ctx) {
        if (ctx.expression() == null) {
            // if we only have a semicolon this is true
            return null;
        }
        return ctx.expression().accept(this);
    }

    @Override
    public Node visitExpression(GLSLParser.ExpressionContext ctx) {
        List<GLSLParser.Assignment_expressionContext> expressions = ctx.assignment_expression();
        if (expressions.size() == 1) {
            return expressions.get(0).accept(this);
        }

        StatementListNode listNode = new StatementListNode();
        for (GLSLParser.Assignment_expressionContext expression : expressions) {
            listNode.addChild(expression.accept(this));
        }
        return listNode;
    }

    @Override
    public Node visitSelection_statement(GLSLParser.Selection_statementContext ctx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
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

    @Override
    public Node visitChildren(RuleNode node) {
        Node result = super.visitChildren(node);
        if (result == null) {
            System.err.format("ERROR: null result from node %s : %s\n", node.getRuleContext().getClass().getSimpleName(), node.getText());
        }
        return result;
    }

    @Override
    public Node visitTerminal(TerminalNode node) {
        Node result = super.visitTerminal(node);
        if (result == null) {
            System.err.format("ERROR: null result from terminal node %s\n", node.getText());
        }
        return result;
    }
}
