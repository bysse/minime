package com.tazadum.glsl.parser.visitor;

import com.tazadum.glsl.ast.*;
import com.tazadum.glsl.ast.arithmetic.*;
import com.tazadum.glsl.ast.conditional.*;
import com.tazadum.glsl.ast.expression.AssignmentNode;
import com.tazadum.glsl.ast.expression.ConstantExpressionNode;
import com.tazadum.glsl.ast.function.FunctionCallNode;
import com.tazadum.glsl.ast.function.FunctionDefinitionNode;
import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.ast.iteration.DoWhileIterationNode;
import com.tazadum.glsl.ast.iteration.ForIterationNode;
import com.tazadum.glsl.ast.iteration.WhileIterationNode;
import com.tazadum.glsl.ast.logical.BooleanLeafNode;
import com.tazadum.glsl.ast.logical.LogicalOperationNode;
import com.tazadum.glsl.ast.logical.RelationalOperationNode;
import com.tazadum.glsl.ast.variable.*;
import com.tazadum.glsl.exception.ParserException;
import com.tazadum.glsl.exception.TypeException;
import com.tazadum.glsl.language.*;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.function.FunctionPrototype;
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
        final NumericOperationNode node = new NumericOperationNode(NumericOperator.ADD);
        node.setLeft(ctx.numeric_expression(0).accept(this));
        node.setRight(ctx.numeric_expression(1).accept(this));
        return node;
    }

    @Override
    public Node visitArray_specifier(GLSLParser.Array_specifierContext ctx) {
        return ctx.constant_expression().accept(this);
    }

    @Override
    public Node visitAssignment_operator(GLSLParser.Assignment_operatorContext ctx) {
        throw ParserException.notSupported("AssignmentOperator are handled by HasToken::match");
    }

    @Override
    public Node visitCondition(GLSLParser.ConditionContext ctx) {
        if (ctx.expression() != null) {
            return ctx.expression().accept(this);
        }

        final FullySpecifiedType fullySpecifiedType = TypeHelper.parseFullySpecifiedType(ctx.fully_specified_type());
        final String identifier = ctx.IDENTIFIER().getText();
        final Node initializer = ctx.initializer().accept(this);
        return new VariableDeclarationNode(false, fullySpecifiedType, identifier, null, initializer);
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
        final Node expression = ctx.conditional_expression().accept(this);
        return new ConstantExpressionNode(expression);
    }

    @Override
    public Node visitConstructor_identifier(GLSLParser.Constructor_identifierContext ctx) {
        throw ParserException.notSupported("constructor_identifier is handled in function_call");
    }

    @Override
    public Node visitDivisionExpression(GLSLParser.DivisionExpressionContext ctx) {
        final NumericOperationNode node = new NumericOperationNode(NumericOperator.DIV);
        node.setLeft(ctx.numeric_expression(0).accept(this));
        node.setRight(ctx.numeric_expression(1).accept(this));
        return node;
    }

    @Override
    public Node visitDoIterationStatement(GLSLParser.DoIterationStatementContext ctx) {
        final DoWhileIterationNode node = new DoWhileIterationNode();
        node.setStatement(ctx.statement_with_scope().accept(this));
        node.setCondition(ctx.expression().accept(this));
        return node;
    }

    @Override
    public Node visitExternal_declaration(GLSLParser.External_declarationContext ctx) {
        return super.visitExternal_declaration(ctx);
    }

    @Override
    public Node visitField_selection(GLSLParser.Field_selectionContext ctx) {
        throw ParserException.notSupported("field_selection is handled in postfix_expression");
    }

    @Override
    public Node visitFor_init_statement(GLSLParser.For_init_statementContext ctx) {
        if (ctx.expression_statement() != null) {
            return ctx.expression_statement().accept(this);
        }
        return ctx.declaration_statement().accept(this);
    }

    @Override
    public Node visitFor_rest_statement(GLSLParser.For_rest_statementContext ctx) {
        throw ParserException.notSupported("for_rest_statement is handled in ForIterationStatement");
    }

    @Override
    public Node visitForIterationStatement(GLSLParser.ForIterationStatementContext ctx) {
        final ForIterationNode node = new ForIterationNode();
        node.setInitialization(ctx.for_init_statement().accept(this));

        GLSLParser.For_rest_statementContext forRestStatement = ctx.for_rest_statement();
        if (forRestStatement != null) {
            node.setCondition(forRestStatement.condition().accept(this));
            node.setExpression(forRestStatement.expression().accept(this));
        }

        if (ctx.statement_no_new_scope() != null) {
            parserContext.enterContext(node);
            node.setStatement(ctx.statement_no_new_scope().accept(this));
            parserContext.exitContext();
        }

        return node;
    }

    @Override
    public Node visitFully_specified_type(GLSLParser.Fully_specified_typeContext ctx) {
        throw ParserException.notSupported("Can't visit FullySpecifiedType. Please use TypeHelper::parseFullySpecifiedType");
    }

    @Override
    public Node visitFunction_call_header(GLSLParser.Function_call_headerContext ctx) {
        throw ParserException.notSupported("This should be handled by function_call");
    }

    @Override
    public Node visitFunction_declarator(GLSLParser.Function_declaratorContext ctx) {
        final GLSLParser.Function_headerContext functionHeader = ctx.function_header();
        final String functionName = functionHeader.IDENTIFIER().getText();

        // parse the return type
        final FullySpecifiedType returnType = TypeHelper.parseFullySpecifiedType(functionHeader.fully_specified_type());
        final FunctionPrototypeNode functionPrototype = new FunctionPrototypeNode(functionName, returnType);

        // function declarations are in the global context
        functionPrototype.setContext(null); // TODO: should we change to global context here?

        // parse the parameters
        final List<GLSLType> parameterTypes = new ArrayList<>();
        for (GLSLParser.Parameter_declarationContext parameterCtx : ctx.parameter_declaration()) {
            final ParameterDeclarationNode parameter = (ParameterDeclarationNode) parameterCtx.accept(this);

            if (parameter.getIdentifier().isEmpty() && BuiltInType.VOID == parameter.getFullySpecifiedType().getType()) {
                // don't add void as a parameter node
                continue;
            }

            parameterTypes.add(parameter.getFullySpecifiedType().getType());
            functionPrototype.addChild(parameter);
        }

        final FunctionPrototype prototype = new FunctionPrototype(false, returnType.getType(), parameterTypes);
        functionPrototype.setPrototype(prototype);

        // register the function
        parserContext.getFunctionRegistry().declare(functionPrototype);

        return functionPrototype;
    }

    @Override
    public Node visitFunction_header(GLSLParser.Function_headerContext ctx) {
        throw ParserException.notSupported("Should be handled in function_declarator");
    }

    @Override
    public Node visitFunction_identifier(GLSLParser.Function_identifierContext ctx) {
        throw ParserException.notSupported("This should be handled by function_call");
    }

    @Override
    public Node visitFunction_prototype(GLSLParser.Function_prototypeContext ctx) {
        return ctx.function_declarator().accept(this);
    }

    @Override
    public Node visitFunctionDeclaration(GLSLParser.FunctionDeclarationContext ctx) {
        return ctx.function_prototype().accept(this);
    }

    @Override
    public Node visitInit_declarator_list(GLSLParser.Init_declarator_listContext ctx) {
        if (ctx.single_declaration() != null) {
            final VariableDeclarationNode variableNode = (VariableDeclarationNode) ctx.single_declaration().accept(this);

            // always return a list node
            final VariableDeclarationListNode listNode = new VariableDeclarationListNode(variableNode.getFullySpecifiedType());
            listNode.addChild(variableNode);
            return listNode;
        }

        final VariableDeclarationListNode listNode = (VariableDeclarationListNode) ctx.init_declarator_list().accept(this);

        FullySpecifiedType fullySpecifiedType = listNode.getFullySpecifiedType();
        final String identifier = ctx.IDENTIFIER().getText();

        Node arraySpecifier = null;
        Node initializer = null;

        if (ctx.array_specifier() != null) {
            arraySpecifier = ctx.array_specifier().accept(this);

            int arrayLength = ArrayType.UNKNOWN_LENGTH;
            if (arraySpecifier instanceof FloatLeafNode) {
                throw new TypeException("Float values can't be used as array length.");
            }

            if (arraySpecifier instanceof IntLeafNode) {
                arrayLength = (int) ((HasNumeric) arraySpecifier).getValue().getValue();
            }

            // TODO: Evaluate the length of the array

            final GLSLType type = new ArrayType(fullySpecifiedType.getType(), arrayLength);
            fullySpecifiedType = new FullySpecifiedType(fullySpecifiedType.getQualifier(), fullySpecifiedType.getPrecision(), type);
        }

        if (ctx.initializer() != null) {
            initializer = ctx.initializer().accept(this);
        }

        final VariableDeclarationNode node = new VariableDeclarationNode(false, fullySpecifiedType, identifier, arraySpecifier, initializer);
        listNode.addChild(node);

        // register the declaration and usage of the type to enable easy look up during optimization
        parserContext.getVariableRegistry().declare(parserContext.currentContext(), node);
        parserContext.getTypeRegistry().usage(parserContext.currentContext(), fullySpecifiedType.getType(), node);

        return listNode;
    }

    @Override
    public Node visitInitializer(GLSLParser.InitializerContext ctx) {
        return ctx.assignment_expression().accept(this);
    }

    @Override
    public Node visitInteger_expression(GLSLParser.Integer_expressionContext ctx) {
        return ctx.expression().accept(this);
    }

    @Override
    public Node visitJump_statement(GLSLParser.Jump_statementContext ctx) {
        if (ctx.CONTINUE() != null) {
            return new ContinueLeafNode();
        }
        if (ctx.BREAK() != null) {
            return new BreakLeafNode();
        }
        if (ctx.DISCARD() != null) {
            return new DiscardLeafNode();
        }

        final ReturnNode returnNode = new ReturnNode();
        if (ctx.expression() != null) {
            returnNode.setExpression(ctx.expression().accept(this));
        }
        return returnNode;
    }

    @Override
    public Node visitLogicalAnd(GLSLParser.LogicalAndContext ctx) {
        final LogicalOperationNode node = new LogicalOperationNode(LogicalOperator.AND);
        node.setLeft(ctx.logical_expression(0).accept(this));
        node.setRight(ctx.logical_expression(1).accept(this));
        return node;
    }

    @Override
    public Node visitLogicalEquality(GLSLParser.LogicalEqualityContext ctx) {
        final RelationalOperationNode node = new RelationalOperationNode(RelationalOperator.Equal);
        node.setLeft(ctx.logical_expression(0).accept(this));
        node.setRight(ctx.logical_expression(1).accept(this));
        return node;
    }

    @Override
    public Node visitLogicalInEquality(GLSLParser.LogicalInEqualityContext ctx) {
        final RelationalOperationNode node = new RelationalOperationNode(RelationalOperator.NotEqual);
        node.setLeft(ctx.logical_expression(0).accept(this));
        node.setRight(ctx.logical_expression(1).accept(this));
        return node;
    }

    @Override
    public Node visitLogicalOr(GLSLParser.LogicalOrContext ctx) {
        final LogicalOperationNode node = new LogicalOperationNode(LogicalOperator.OR);
        node.setLeft(ctx.logical_expression(0).accept(this));
        node.setRight(ctx.logical_expression(1).accept(this));
        return node;

    }

    @Override
    public Node visitLogicalXor(GLSLParser.LogicalXorContext ctx) {
        final LogicalOperationNode node = new LogicalOperationNode(LogicalOperator.XOR);
        node.setLeft(ctx.logical_expression(0).accept(this));
        node.setRight(ctx.logical_expression(1).accept(this));
        return node;
    }

    @Override
    public Node visitMultiplicationExpression(GLSLParser.MultiplicationExpressionContext ctx) {
        final NumericOperationNode node = new NumericOperationNode(NumericOperator.MUL);
        node.setLeft(ctx.numeric_expression(0).accept(this));
        node.setRight(ctx.numeric_expression(1).accept(this));
        return node;
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

        final ParameterDeclarationNode parameterDeclaration = new ParameterDeclarationNode(type, parameterName, arraySpecifier);

        final GLSLContext context = parserContext.currentContext();
        parserContext.getVariableRegistry().declare(context, parameterDeclaration);

        return parameterDeclaration;
    }

    @Override
    public Node visitParameter_qualifier(GLSLParser.Parameter_qualifierContext ctx) {
        throw ParserException.notSupported("Not supported");
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
        throw ParserException.notSupported("Not implemented");
    }

    @Override
    public Node visitPrecisionDeclaration(GLSLParser.PrecisionDeclarationContext ctx) {
        PrecisionQualifier qualifier = HasToken.match(ctx.precision_qualifier(), PrecisionQualifier.values());
        BuiltInType builtInType = HasToken.match(ctx.type_specifier_no_prec(), BuiltInType.values());

        if (builtInType == null) {
            throw ParserException.notSupported("Precision for custom types are not supported");
        }

        return new PrecisionDeclarationNode(qualifier, builtInType);
    }

    @Override
    public Node visitPrimary_expression_or_function_call(GLSLParser.Primary_expression_or_function_callContext ctx) {
        return super.visitPrimary_expression_or_function_call(ctx);
    }

    @Override
    public Node visitRelational(GLSLParser.RelationalContext ctx) {
        RelationalOperator operator = null;
        if (ctx.LEFT_ANGLE() != null) {
            operator = RelationalOperator.LessThan;
        }
        if (ctx.RIGHT_ANGLE() != null) {
            operator = RelationalOperator.GreaterThan;
        }
        if (ctx.LE_OP() != null) {
            operator = RelationalOperator.LessThanOrEqual;
        }
        if (ctx.GE_OP() != null) {
            operator = RelationalOperator.GreaterThanOrEqual;
        }

        final RelationalOperationNode node = new RelationalOperationNode(operator);
        node.setLeft(ctx.logical_expression(0).accept(this));
        node.setRight(ctx.logical_expression(1).accept(this));
        return node;
    }

    @Override
    public Node visitSingle_declaration(GLSLParser.Single_declarationContext ctx) {
        if (ctx.INVARIANT() != null) {
            throw ParserException.notSupported("invariant");
        }

        if (ctx.IDENTIFIER() == null) {
            // early type declaration
            throw ParserException.notSupported("early type declaration");
        }

        final String identifier = ctx.IDENTIFIER().getText();
        final FullySpecifiedType fullySpecifiedType = TypeHelper.parseFullySpecifiedType(ctx.fully_specified_type());

        Node arraySpecifier = null;
        Node initializer = null;

        if (ctx.array_specifier() != null) {
            arraySpecifier = ctx.array_specifier().accept(this);
        }
        if (ctx.initializer() != null) {
            initializer = ctx.initializer().accept(this);
        }

        final VariableDeclarationNode node = new VariableDeclarationNode(false, fullySpecifiedType, identifier, arraySpecifier, initializer);
        // register the declaration and usage of the type to enable easy look up during optimization
        parserContext.getVariableRegistry().declare(parserContext.currentContext(), node);
        parserContext.getTypeRegistry().usage(parserContext.currentContext(), fullySpecifiedType.getType(), node);
        return node;
    }

    @Override
    public Node visitStatement_list(GLSLParser.Statement_listContext ctx) {
        final StatementListNode listNode = new StatementListNode();
        for (GLSLParser.Statement_no_new_scopeContext statementCtx : ctx.statement_no_new_scope()) {
            listNode.addChild(statementCtx.accept(this));
        }
        return listNode;
    }

    @Override
    public Node visitStatement_with_scope(GLSLParser.Statement_with_scopeContext ctx) {
        if (ctx.simple_statement() != null) {
            return ctx.simple_statement().accept(this);
        }

        return ctx.compound_statement_no_new_scope().accept(this);
    }

    @Override
    public Node visitStruct_declaration(GLSLParser.Struct_declarationContext ctx) {
        throw ParserException.notSupported("Structs are not supported");
    }

    @Override
    public Node visitStruct_declaration_list(GLSLParser.Struct_declaration_listContext ctx) {
        throw ParserException.notSupported("Structs are not supported");
    }

    @Override
    public Node visitStruct_declarator(GLSLParser.Struct_declaratorContext ctx) {
        throw ParserException.notSupported("Structs are not supported");
    }

    @Override
    public Node visitStruct_declarator_list(GLSLParser.Struct_declarator_listContext ctx) {
        throw ParserException.notSupported("Structs are not supported");
    }

    @Override
    public Node visitStruct_specifier(GLSLParser.Struct_specifierContext ctx) {
        throw ParserException.notSupported("Structs are not supported");
    }

    @Override
    public Node visitSubtractionExpression(GLSLParser.SubtractionExpressionContext ctx) {
        final NumericOperationNode node = new NumericOperationNode(NumericOperator.SUB);
        node.setLeft(ctx.numeric_expression(0).accept(this));
        node.setRight(ctx.numeric_expression(1).accept(this));
        return node;
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
        throw ParserException.notSupported("UnaryOperator should be parsed with HasToken::match");
    }

    @Override
    public Node visitUnaryExpression(GLSLParser.UnaryExpressionContext ctx) {
        return super.visitUnaryExpression(ctx);
    }

    @Override
    public Node visitVariable_identifier(GLSLParser.Variable_identifierContext ctx) {
        throw ParserException.notSupported("Variable identifiers are handled in visitPrimary_expression");
    }

    @Override
    public Node visitWhileIterationStatement(GLSLParser.WhileIterationStatementContext ctx) {
        final WhileIterationNode node = new WhileIterationNode();
        node.setCondition(ctx.condition().accept(this));
        node.setStatement(ctx.statement_no_new_scope().accept(this));
        return node;
    }

    @Override
    public Node visitFunction_definition(GLSLParser.Function_definitionContext ctx) {
        // create the context
        final FunctionDefinitionNode definitionNode = new FunctionDefinitionNode(null, null);
        parserContext.enterContext(definitionNode);

        final FunctionPrototypeNode functionPrototype = (FunctionPrototypeNode) ctx.function_prototype().accept(this);
        definitionNode.setFunctionPrototype(functionPrototype);
        functionPrototype.setContext(definitionNode);

        GLSLParser.Compound_statement_no_new_scopeContext statements = ctx.compound_statement_no_new_scope();
        if (statements != null) {
            definitionNode.setStatements((StatementListNode) statements.accept(this));
        }
        parserContext.exitContext();

        return definitionNode;
    }

    @Override
    public Node visitFunction_call(GLSLParser.Function_callContext ctx) {
        // TODO: handle constructor_identifier and builtin function separately from user functions?
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
        return ctx.init_declarator_list().accept(this);
    }

    @Override
    public Node visitCompound_statement_no_new_scope(GLSLParser.Compound_statement_no_new_scopeContext ctx) {
        StatementListNode statementList = new StatementListNode();

        if (ctx.statement_list() != null) {
            // go through all of the statements
            for (GLSLParser.Statement_no_new_scopeContext statementScope : ctx.statement_list().statement_no_new_scope()) {
                final Node node = statementScope.accept(this);
                if (node == null) {
                    // this can happen if we have double semicolons
                    continue;
                }
                if (node instanceof StatementListNode) {
                    final StatementListNode statementListNode = (StatementListNode) node;
                    for (int i = 0; i < statementListNode.getChildCount(); i++) {
                        statementList.addChild(statementListNode.getChild(i));
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
        if (ctx.statement_list() == null) {
            return new StatementListNode();
        }
        return ctx.statement_list().accept(this);
    }

    @Override
    public Node visitSimple_statement(GLSLParser.Simple_statementContext ctx) {
        return super.visitSimple_statement(ctx);
    }

    @Override
    public Node visitDeclaration_statement(GLSLParser.Declaration_statementContext ctx) {
        return ctx.declaration().accept(this);
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
        final List<GLSLParser.Assignment_expressionContext> expressions = ctx.assignment_expression();
        if (expressions.size() == 1) {
            return expressions.get(0).accept(this);
        }

        final StatementListNode listNode = new StatementListNode();
        for (GLSLParser.Assignment_expressionContext expression : expressions) {
            listNode.addChild(expression.accept(this));
        }
        return listNode;
    }

    @Override
    public Node visitSelection_statement(GLSLParser.Selection_statementContext ctx) {
        final Node expression = ctx.expression().accept(this);
        final Node thenNode = ctx.statement_with_scope(0).accept(this);

        Node elseNode = null;
        if (ctx.statement_with_scope(1) != null) {
            elseNode = ctx.statement_with_scope(1).accept(this);
        }

        return new ConditionNode(expression, thenNode, elseNode);
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

        final ResolutionResult result = variableRegistry.resolve(currentContext, variableName, Identifier.Mode.Original);
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
