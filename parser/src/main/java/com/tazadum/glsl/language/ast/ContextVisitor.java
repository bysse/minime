package com.tazadum.glsl.language.ast;

import com.tazadum.glsl.exception.Errors;
import com.tazadum.glsl.exception.SourcePositionException;
import com.tazadum.glsl.exception.VariableException;
import com.tazadum.glsl.language.HasToken;
import com.tazadum.glsl.language.ast.arithmetic.*;
import com.tazadum.glsl.language.ast.conditional.*;
import com.tazadum.glsl.language.ast.expression.AssignmentNode;
import com.tazadum.glsl.language.ast.expression.ConstantExpressionNode;
import com.tazadum.glsl.language.ast.function.FunctionCallNode;
import com.tazadum.glsl.language.ast.function.FunctionDefinitionNode;
import com.tazadum.glsl.language.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.language.ast.iteration.DoWhileIterationNode;
import com.tazadum.glsl.language.ast.iteration.ForIterationNode;
import com.tazadum.glsl.language.ast.iteration.WhileIterationNode;
import com.tazadum.glsl.language.ast.logical.BooleanLeafNode;
import com.tazadum.glsl.language.ast.logical.LogicalOperationNode;
import com.tazadum.glsl.language.ast.logical.RelationalOperationNode;
import com.tazadum.glsl.language.ast.variable.*;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.language.function.FunctionPrototype;
import com.tazadum.glsl.language.model.*;
import com.tazadum.glsl.language.type.*;
import com.tazadum.glsl.language.variable.ResolutionResult;
import com.tazadum.glsl.language.variable.VariableRegistry;
import com.tazadum.glsl.parser.GLSLBaseVisitor;
import com.tazadum.glsl.parser.GLSLParser;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.util.SourcePosition;
import com.tazadum.glsl.util.SourcePositionId;
import com.tazadum.glsl.util.SourcePositionMapper;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

public class ContextVisitor extends GLSLBaseVisitor<Node> {
    private ParserContext parserContext;
    private SourcePositionMapper mapper;

    public ContextVisitor(ParserContext parserContext, SourcePositionMapper mapper) {
        this.parserContext = parserContext;
        this.mapper = mapper;
    }

    @Override
    public Node visitAdditive_expression(GLSLParser.Additive_expressionContext ctx) {
        final NumericOperator op = ctx.PLUS() != null ? NumericOperator.ADD : NumericOperator.SUB;
        final NumericOperationNode node = new NumericOperationNode(SourcePosition.create(ctx.start), op);
        node.setLeft(ctx.numeric_expression(0).accept(this));
        node.setRight(ctx.numeric_expression(1).accept(this));
        return node;
    }

    @Override
    public Node visitArray_specifier(GLSLParser.Array_specifierContext ctx) {
        final Node arraySpecifier = ctx.constant_expression().accept(this);
        if (arraySpecifier instanceof FloatLeafNode) {
            throw new SourcePositionException(SourcePosition.create(ctx.start), Errors.Type.NON_INTEGER_ARRAY_LENGTH());
        }
        return arraySpecifier;
    }

    @Override
    public Node visitAssignment_operator(GLSLParser.Assignment_operatorContext ctx) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Node visitCondition(GLSLParser.ConditionContext ctx) {
        if (ctx.expression() != null) {
            return ctx.expression().accept(this);
        }

        final FullySpecifiedType fullySpecifiedType = TypeParserHelper.parseFullySpecifiedType(this, ctx.fully_specified_type());
        final String identifier = ctx.IDENTIFIER().getText();
        final Node initializer = ctx.initializer().accept(this);
        return new VariableDeclarationNode(SourcePosition.create(ctx.start), false, fullySpecifiedType, identifier, initializer);
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
        return new TernaryConditionNode(SourcePosition.create(ctx.start), condition, thenNode, elseNode);
    }

    @Override
    public Node visitConstant_expression(GLSLParser.Constant_expressionContext ctx) {
        final Node expression = ctx.conditional_expression().accept(this);
        return new ConstantExpressionNode(SourcePosition.create(ctx.start), expression);
    }

    @Override
    public Node visitMultiplicative_expression(GLSLParser.Multiplicative_expressionContext ctx) {
        NumericOperator op = null;
        if (ctx.STAR() != null) {
            op = NumericOperator.MUL;
        } else if (ctx.SLASH() != null) {
            op = NumericOperator.DIV;
        } else if (ctx.PERCENT() != null) {
            op = NumericOperator.MOD;
        }

        assert op != null : "Multiplicative_expression has a bad implementation";

        final NumericOperationNode node = new NumericOperationNode(SourcePosition.create(ctx.start), op);
        node.setLeft(ctx.numeric_expression(0).accept(this));
        node.setRight(ctx.numeric_expression(1).accept(this));
        return node;
    }

    @Override
    public Node visitIteration_do_while_statement(GLSLParser.Iteration_do_while_statementContext ctx) {
        final DoWhileIterationNode node = new DoWhileIterationNode(SourcePosition.create(ctx.start));
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
        throw new UnsupportedOperationException("field_selection is handled in postfix_expression");

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
        throw new UnsupportedOperationException("for_rest_statement is handled in ForIterationStatement");
    }

    @Override
    public Node visitIteration_for_statement(GLSLParser.Iteration_for_statementContext ctx) {
        final ForIterationNode node = new ForIterationNode(SourcePosition.create(ctx.start));
        if (ctx.statement_no_new_scope() != null) {
            parserContext.enterContext(node);
        }

        node.setInitialization(ctx.for_init_statement().accept(this));

        GLSLParser.For_rest_statementContext forRestStatement = ctx.for_rest_statement();
        if (forRestStatement != null) {
            node.setCondition(forRestStatement.condition().accept(this));
            node.setExpression(forRestStatement.expression().accept(this));
        }

        if (ctx.statement_no_new_scope() != null) {
            node.setStatement(ctx.statement_no_new_scope().accept(this));
            parserContext.exitContext();
        }

        return node;
    }

    @Override
    public Node visitFully_specified_type(GLSLParser.Fully_specified_typeContext ctx) {
        throw new UnsupportedOperationException("Can't visit FullySpecifiedType. Please use TypeHelper::parseFullySpecifiedType");
    }

    @Override
    public Node visitFunction_call_header(GLSLParser.Function_call_headerContext ctx) {
        throw new UnsupportedOperationException("This should be handled by function_call");
    }

    @Override
    public Node visitFunction_declarator(GLSLParser.Function_declaratorContext ctx) {
        final GLSLParser.Function_headerContext functionHeader = ctx.function_header();
        final String functionName = functionHeader.IDENTIFIER().getText();
        final SourcePosition sourcePosition = SourcePosition.create(ctx.start);

        // parse the return type
        final FullySpecifiedType returnType = TypeParserHelper.parseFullySpecifiedType(this, functionHeader.fully_specified_type());
        final FunctionPrototypeNode functionPrototype = new FunctionPrototypeNode(sourcePosition, functionName, returnType);

        // function declarations are in the global context
        functionPrototype.setContext(null); // TODO: should we change to global context here?

        // parse the parameters
        final List<GLSLType> parameterTypes = new ArrayList<>();
        for (GLSLParser.Parameter_declarationContext parameterCtx : ctx.parameter_declaration()) {
            final ParameterDeclarationNode parameter = (ParameterDeclarationNode) parameterCtx.accept(this);

            if (parameter.getIdentifier().isEmpty() && PredefinedType.VOID == parameter.getFullySpecifiedType().getType()) {
                // don't add void as a parameter node
                continue;
            }

            parameterTypes.add(parameter.getFullySpecifiedType().getType());
            functionPrototype.addChild(parameter);
        }

        final FunctionPrototype prototype = new FunctionPrototype(false, returnType.getType(), parameterTypes);
        functionPrototype.setPrototype(prototype);

        if (isIncluded(sourcePosition)) {
            // mark the function as being included from a shared file.
            functionPrototype.setShared(true);
        }

        // register the function
        parserContext.getFunctionRegistry().declareFunction(functionPrototype);

        return functionPrototype;
    }

    @Override
    public Node visitFunction_header(GLSLParser.Function_headerContext ctx) {
        throw new UnsupportedOperationException("Should be handled in function_declarator");
    }

    @Override
    public Node visitFunction_identifier(GLSLParser.Function_identifierContext ctx) {
        throw new UnsupportedOperationException("This should be handled by function_call");
    }

    @Override
    public Node visitFunction_prototype(GLSLParser.Function_prototypeContext ctx) {
        return ctx.function_declarator().accept(this);
    }

    @Override
    public Node visitInit_declarator_list(GLSLParser.Init_declarator_listContext ctx) {
        final SourcePosition sourcePosition = SourcePosition.create(ctx.start);
        final boolean isShared = isIncluded(sourcePosition);

        if (ctx.single_declaration() != null) {
            final VariableDeclarationNode variableNode = (VariableDeclarationNode) ctx.single_declaration().accept(this);
            variableNode.setShared(isShared);

            // always return a list node
            final VariableDeclarationListNode listNode = new VariableDeclarationListNode(sourcePosition, variableNode.getFullySpecifiedType());
            listNode.setShared(isShared);
            listNode.addChild(variableNode);
            return listNode;
        }

        final VariableDeclarationListNode listNode = (VariableDeclarationListNode) ctx.init_declarator_list().accept(this);
        listNode.setShared(isShared);

        FullySpecifiedType fullySpecifiedType = listNode.getFullySpecifiedType();
        final String identifier = ctx.IDENTIFIER().getText();

        if (ctx.array_specifier() != null) {
            // the declaration has an array specifier, modify the type to reflect that
            final Node node = ctx.array_specifier().accept(this);
            final GLSLType type = new ArrayType(fullySpecifiedType.getType(), node);
            fullySpecifiedType = new FullySpecifiedType(fullySpecifiedType.getQualifiers(), type);
        }

        Node initializer = null;
        if (ctx.initializer() != null) {
            initializer = ctx.initializer().accept(this);
        }

        final VariableDeclarationNode node = new VariableDeclarationNode(sourcePosition, false, fullySpecifiedType, identifier, initializer);
        node.setShared(isShared);
        listNode.addChild(node);

        // register the declaration and usage of the type to enable easy look up during optimization
        parserContext.getVariableRegistry().declareVariable(parserContext.currentContext(), node);
        parserContext.getTypeRegistry().usage(parserContext.currentContext(), fullySpecifiedType.getType(), node);

        return listNode;
    }

    @Override
    public ParameterDeclarationNode visitParameter_declaration(GLSLParser.Parameter_declarationContext ctx) {
        // the parameter might not have an identifier
        final String parameterName = ctx.IDENTIFIER() == null ? null : ctx.IDENTIFIER().getText();

        // parse the type
        FullySpecifiedType fullySpecifiedType = TypeParserHelper.parseFullySpecifiedType(this, ctx);

        // parse the array specifier
        if (ctx.array_specifier() != null) {
            Node node = ctx.array_specifier().accept(this);
            final GLSLType type = new ArrayType(fullySpecifiedType.getType(), node);
            fullySpecifiedType = new FullySpecifiedType(fullySpecifiedType.getQualifiers(), type);
        }

        final ParameterDeclarationNode parameterDeclaration = new ParameterDeclarationNode(SourcePosition.create(ctx.start), fullySpecifiedType, parameterName);

        final GLSLContext context = parserContext.currentContext();
        parserContext.getVariableRegistry().declareVariable(context, parameterDeclaration);

        return parameterDeclaration;
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
            return new ContinueLeafNode(SourcePosition.create(ctx.start));
        }
        if (ctx.BREAK() != null) {
            return new BreakLeafNode(SourcePosition.create(ctx.start));
        }
        if (ctx.DISCARD() != null) {
            return new DiscardLeafNode(SourcePosition.create(ctx.start));
        }

        final ReturnNode returnNode = new ReturnNode(SourcePosition.create(ctx.start));
        if (ctx.expression() != null) {
            returnNode.setExpression(ctx.expression().accept(this));
        }
        return returnNode;
    }

    @Override
    public Node visitLogical_and_expression(GLSLParser.Logical_and_expressionContext ctx) {
        final LogicalOperationNode node = new LogicalOperationNode(SourcePosition.create(ctx.start), LogicalOperator.AND);
        node.setLeft(ctx.logical_expression(0).accept(this));
        node.setRight(ctx.logical_expression(1).accept(this));
        return node;
    }

    @Override
    public Node visitEquality_expression(GLSLParser.Equality_expressionContext ctx) {
        RelationalOperator op = null;
        if (ctx.EQ_OP() != null) {
            op = RelationalOperator.Equal;
        } else if (ctx.NE_OP() != null) {
            op = RelationalOperator.NotEqual;
        } else {
            assert false : "The rule 'equality_expression' is not implemented correctly";
        }

        final RelationalOperationNode node = new RelationalOperationNode(SourcePosition.create(ctx.start), op);
        node.setLeft(ctx.logical_expression(0).accept(this));
        node.setRight(ctx.logical_expression(1).accept(this));
        return node;
    }

    @Override
    public Node visitRelational_expression(GLSLParser.Relational_expressionContext ctx) {
        RelationalOperator op = null;
        if (ctx.LEFT_ANGLE() != null) {
            op = RelationalOperator.LessThan;
        } else if (ctx.RIGHT_ANGLE() != null) {
            op = RelationalOperator.GreaterThan;
        } else if (ctx.LE_OP() != null) {
            op = RelationalOperator.LessThanOrEqual;
        } else if (ctx.GE_OP() != null) {
            op = RelationalOperator.GreaterThanOrEqual;
        } else {
            assert false : "The rule 'relational_expression' is not implemented correctly";
        }

        final RelationalOperationNode node = new RelationalOperationNode(SourcePosition.create(ctx.start), op);
        node.setLeft(ctx.logical_expression(0).accept(this));
        node.setRight(ctx.logical_expression(1).accept(this));
        return node;
    }

    @Override
    public Node visitLogical_or_expression(GLSLParser.Logical_or_expressionContext ctx) {
        final LogicalOperationNode node = new LogicalOperationNode(SourcePosition.create(ctx.start), LogicalOperator.OR);
        node.setLeft(ctx.logical_expression(0).accept(this));
        node.setRight(ctx.logical_expression(1).accept(this));
        return node;

    }

    @Override
    public Node visitLogical_xor_expression(GLSLParser.Logical_xor_expressionContext ctx) {
        final LogicalOperationNode node = new LogicalOperationNode(SourcePosition.create(ctx.start), LogicalOperator.XOR);
        node.setLeft(ctx.logical_expression(0).accept(this));
        node.setRight(ctx.logical_expression(1).accept(this));
        return node;
    }

    @Override
    public Node visitPostfix_expression(GLSLParser.Postfix_expressionContext ctx) {
        if (ctx.primary_expression() != null) {
            return ctx.primary_expression().accept(this);
        }
        if (ctx.function_call() != null) {
            return ctx.function_call().accept(this);
        }

        // resolve the postfix_expression
        final Node expression = ctx.postfix_expression().accept(this);

        // handle array index
        if (ctx.integer_expression() != null) {
            final Node arrayIndex = ctx.integer_expression().accept(this);
            final SourcePosition position = SourcePosition.create(ctx.integer_expression().start);
            return new ArrayIndexNode(position, expression, arrayIndex);
        }

        // handle field selection
        if (ctx.field_selection() != null) {
            final String selection = ctx.field_selection().IDENTIFIER().getText();
            final SourcePosition position = SourcePosition.create(ctx.field_selection().start);
            final FieldSelectionNode fieldSelectionNode = new FieldSelectionNode(position, selection);
            fieldSelectionNode.setExpression(expression);
            return fieldSelectionNode;
        }

        final SourcePosition position = SourcePosition.create(ctx.start);

        // handle the postfix operators
        if (ctx.INC_OP() != null) {
            final PostfixOperationNode operationNode = new PostfixOperationNode(position, UnaryOperator.INCREASE);
            operationNode.setExpression(expression);
            return operationNode;
        }

        if (ctx.DEC_OP() != null) {
            final PostfixOperationNode operationNode = new PostfixOperationNode(position, UnaryOperator.DECREASE);
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
    public Node visitPrecision_declaration(GLSLParser.Precision_declarationContext ctx) {
        final PrecisionQualifier qualifier = HasToken.fromToken(ctx.precision_qualifier(), PrecisionQualifier.values());
        final GLSLType type = TypeParserHelper.parseTypeSpecifier(this, ctx.type_specifier());

        final SourcePosition typePosition = SourcePosition.create(ctx.type_specifier().start);

        if (!(type instanceof PredefinedType) || type.isArray()) {
            throw new SourcePositionException(typePosition, Errors.Syntax.TYPE_DOES_NOT_SUPPORT_PRECISION(ctx.type_specifier().getText()));
        }

        final PredefinedType predefinedType = (PredefinedType) type;
        final SourcePosition position = SourcePosition.create(ctx.start);

        switch (predefinedType) {
            case INT:
            case UINT:
            case FLOAT:
            case DOUBLE:
                return new PrecisionDeclarationNode(position, qualifier, predefinedType);
        }

        if (predefinedType.category() != TypeCategory.NoFields) {
            return new PrecisionDeclarationNode(position, qualifier, predefinedType);
        }

        throw new SourcePositionException(typePosition, Errors.Syntax.TYPE_DOES_NOT_SUPPORT_PRECISION(ctx.type_specifier().getText()));
    }

    @Override
    public Node visitSingle_declaration(GLSLParser.Single_declarationContext ctx) {
        if (ctx.IDENTIFIER() == null) {
            // TODO: early type declaration, add specific node for it
            throw new UnsupportedOperationException("early type declaration");
        }

        final String identifier = ctx.IDENTIFIER().getText();
        FullySpecifiedType fullySpecifiedType = TypeParserHelper.parseFullySpecifiedType(this, ctx.fully_specified_type());

        if (ctx.array_specifier() != null) {
            // the declaration has an array specifier, modify the type to reflect that
            final Node node = ctx.array_specifier().accept(this);
            final GLSLType type = new ArrayType(fullySpecifiedType.getType(), node);
            fullySpecifiedType = new FullySpecifiedType(fullySpecifiedType.getQualifiers(), type);
        }

        Node initializer = null;
        if (ctx.initializer() != null) {
            initializer = ctx.initializer().accept(this);
        }

        final SourcePosition position = SourcePosition.create(ctx.start);
        final VariableDeclarationNode node = new VariableDeclarationNode(position, false, fullySpecifiedType, identifier, initializer);

        // register the declaration and usage of the type to enable easy look up during optimization
        parserContext.getVariableRegistry().declareVariable(parserContext.currentContext(), node);
        parserContext.getTypeRegistry().usage(parserContext.currentContext(), fullySpecifiedType.getType(), node);
        return node;
    }

    @Override
    public Node visitStatement_list(GLSLParser.Statement_listContext ctx) {
        final StatementListNode listNode = new StatementListNode(SourcePosition.create(ctx.start));
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
        throw new UnsupportedOperationException("Structs are not supported");
    }

    @Override
    public Node visitStruct_declaration_list(GLSLParser.Struct_declaration_listContext ctx) {
        throw new UnsupportedOperationException("Structs are not supported");
    }

    @Override
    public Node visitStruct_declarator(GLSLParser.Struct_declaratorContext ctx) {
        throw new UnsupportedOperationException("Structs are not supported");
    }

    @Override
    public Node visitStruct_declarator_list(GLSLParser.Struct_declarator_listContext ctx) {
        throw new UnsupportedOperationException("Structs are not supported");
    }

    @Override
    public Node visitStruct_specifier(GLSLParser.Struct_specifierContext ctx) {
        throw new UnsupportedOperationException("Structs are not supported");
    }

    @Override
    public Node visitTranslation_unit(GLSLParser.Translation_unitContext ctx) {
        final StatementListNode listNode = new StatementListNode(SourcePosition.create(ctx.start));
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
    public Node visitUnary_expression(GLSLParser.Unary_expressionContext ctx) {
        if (ctx.postfix_expression() != null) {
            return ctx.postfix_expression().accept(this);
        }

        final SourcePosition position = SourcePosition.create(ctx.start);
        final Node expression = ctx.unary_expression().accept(this);

        if (ctx.INC_OP() != null) {
            final PrefixOperationNode prefixOperationNode = new PrefixOperationNode(position, UnaryOperator.INCREASE);
            prefixOperationNode.setExpression(expression);
            return prefixOperationNode;
        }

        if (ctx.DEC_OP() != null) {
            final PrefixOperationNode prefixOperationNode = new PrefixOperationNode(position, UnaryOperator.DECREASE);
            prefixOperationNode.setExpression(expression);
            return prefixOperationNode;
        }

        final UnaryOperator operator = HasToken.fromToken(ctx.unary_operator(), UnaryOperator.values());
        final UnaryOperationNode unaryOperationNode = new UnaryOperationNode(position, operator);
        unaryOperationNode.setExpression(expression);
        return unaryOperationNode;
    }

    @Override
    public Node visitIteration_while_statement(GLSLParser.Iteration_while_statementContext ctx) {
        final WhileIterationNode node = new WhileIterationNode(SourcePosition.create(ctx.start));
        node.setCondition(ctx.condition().accept(this));
        node.setStatement(ctx.statement_no_new_scope().accept(this));
        return node;
    }

    @Override
    public Node visitFunction_definition(GLSLParser.Function_definitionContext ctx) {
        // create the context
        final FunctionDefinitionNode definitionNode = new FunctionDefinitionNode(SourcePosition.create(ctx.start), null, null);
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
        final SourcePosition position = SourcePosition.create(ctx.start);
        final String functionName = ctx.function_call_header().function_identifier().getText();
        final FunctionCallNode functionCallNode = new FunctionCallNode(position, functionName);

        // check if the function call has arguments
        if (ctx.VOID() == null) {
            final List<Node> arguments = new ArrayList<>();
            for (GLSLParser.Assignment_expressionContext argCtx : ctx.assignment_expression()) {
                // parse each argument and mAdd them to the function
                final Node argument = argCtx.accept(this);
                functionCallNode.addChild(argument);
            }
        }

        return functionCallNode;
    }

    @Override
    public Node visitCompound_statement_no_new_scope(GLSLParser.Compound_statement_no_new_scopeContext ctx) {
        StatementListNode statementList = new StatementListNode(SourcePosition.create(ctx.start));

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

        final StatementListNode listNode = new StatementListNode(SourcePosition.create(ctx.start));
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

        return new ConditionNode(SourcePosition.create(ctx.start), expression, thenNode, elseNode);
    }

    @Override
    public Node visitAssignment_expression(GLSLParser.Assignment_expressionContext ctx) {
        if (ctx.conditional_expression() != null) {
            return ctx.conditional_expression().accept(this);
        }

        final AssignmentOperator operator = HasToken.fromToken(ctx.assignment_operator(), AssignmentOperator.values());
        final Node lparam = ctx.unary_expression().accept(this);
        final Node rparam = ctx.assignment_expression().accept(this);

        return new AssignmentNode(SourcePosition.create(ctx.start), lparam, operator, rparam);
    }

    @Override
    public Node visitPrimary_expression(GLSLParser.Primary_expressionContext ctx) {
        final SourcePosition position = SourcePosition.create(ctx.start);

        if (ctx.INTCONSTANT() != null) {
            Numeric numeric = Numeric.create(ctx.INTCONSTANT().getText());
            return new IntLeafNode(position, numeric);
        }
        if (ctx.FLOATCONSTANT() != null) {
            Numeric numeric = Numeric.create(ctx.FLOATCONSTANT().getText());
            return new FloatLeafNode(position, numeric);
        }
        if (ctx.BOOLCONSTANT() != null) {
            return new BooleanLeafNode(position, Boolean.valueOf(ctx.BOOLCONSTANT().getText()));
        }
        if (ctx.LEFT_PAREN() != null) {
            final Node expression = ctx.expression().accept(this);
            return new ParenthesisNode(position, expression);
        }

        // resolve variable
        final VariableRegistry variableRegistry = parserContext.getVariableRegistry();
        final GLSLContext currentContext = parserContext.currentContext();
        final String variableName = ctx.variable_identifier().IDENTIFIER().getText();

        try {
            final ResolutionResult result = variableRegistry.resolve(currentContext, variableName, Identifier.Mode.Original);
            final VariableNode node = new VariableNode(position, result.getDeclaration());

            variableRegistry.registerVariableUsage(currentContext, variableName, node);

            return node;
        } catch (VariableException e) {
            throw new SourcePositionException(position, e.getMessage(), e);
        }
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

    /**
     * Check if the provided SourcePosition originates from an included file.
     * This requires that the SourcePositionMapper from the preprocessor is passed to the visitor during construction.
     */
    private boolean isIncluded(SourcePosition sourcePosition) {
        if (mapper == null) {
            return false;
        }
        final SourcePositionId sourcePositionId = mapper.map(sourcePosition);
        return !sourcePositionId.isDefaultFile();
    }
}
