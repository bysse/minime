package com.tazadum.glsl.language.ast;

import com.tazadum.glsl.exception.BadImplementationException;
import com.tazadum.glsl.exception.SourcePositionException;
import com.tazadum.glsl.exception.TypeException;
import com.tazadum.glsl.exception.VariableException;
import com.tazadum.glsl.language.ast.arithmetic.*;
import com.tazadum.glsl.language.ast.conditional.*;
import com.tazadum.glsl.language.ast.expression.AssignmentNode;
import com.tazadum.glsl.language.ast.expression.ConstantExpressionNode;
import com.tazadum.glsl.language.ast.expression.ParenthesisNode;
import com.tazadum.glsl.language.ast.function.FunctionCallNode;
import com.tazadum.glsl.language.ast.function.FunctionDefinitionNode;
import com.tazadum.glsl.language.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.language.ast.iteration.DoWhileIterationNode;
import com.tazadum.glsl.language.ast.iteration.ForIterationNode;
import com.tazadum.glsl.language.ast.iteration.WhileIterationNode;
import com.tazadum.glsl.language.ast.logical.BooleanLeafNode;
import com.tazadum.glsl.language.ast.logical.LogicalOperationNode;
import com.tazadum.glsl.language.ast.logical.RelationalOperationNode;
import com.tazadum.glsl.language.ast.struct.InterfaceBlockNode;
import com.tazadum.glsl.language.ast.struct.StructDeclarationNode;
import com.tazadum.glsl.language.ast.type.*;
import com.tazadum.glsl.language.ast.util.NodeUtil;
import com.tazadum.glsl.language.ast.variable.*;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.language.function.ConstFunction;
import com.tazadum.glsl.language.function.FunctionPrototype;
import com.tazadum.glsl.language.model.*;
import com.tazadum.glsl.language.type.*;
import com.tazadum.glsl.language.variable.ResolutionResult;
import com.tazadum.glsl.language.variable.VariableRegistry;
import com.tazadum.glsl.parser.*;
import com.tazadum.glsl.preprocessor.model.HasToken;
import com.tazadum.glsl.util.ANTLRUtils;
import com.tazadum.glsl.util.SourcePosition;
import com.tazadum.glsl.util.SourcePositionId;
import com.tazadum.glsl.util.SourcePositionMapper;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.tazadum.glsl.exception.Errors.Coarse.*;
import static com.tazadum.glsl.exception.Errors.Extras.*;
import static com.tazadum.glsl.language.ast.traits.HasConstState.isConst;
import static com.tazadum.glsl.language.model.StorageQualifier.INOUT;
import static com.tazadum.glsl.language.model.StorageQualifier.OUT;
import static com.tazadum.glsl.language.type.PredefinedType.INT;
import static com.tazadum.glsl.language.type.PredefinedType.UINT;
import static com.tazadum.glsl.parser.TypeCombination.anyOf;

public class ASTConverter extends GLSLBaseVisitor<Node> {
    private SourcePositionMapper mapper;
    private ParserContext parserContext;

    public ASTConverter(SourcePositionMapper mapper, ParserContext parserContext) {
        this.mapper = mapper;
        this.parserContext = parserContext;
    }

    private static <T extends HasToken & TypeQualifier> T from(ParserRuleContext ctx, T... values) {
        final SourcePosition position = SourcePosition.create(ctx.start);
        return HasToken.fromToken(ctx, values);
    }

    @Override
    public Node visitUnary_expression(GLSLParser.Unary_expressionContext ctx) {
        if (ctx.postfix_expression() != null) {
            return ctx.postfix_expression().accept(this);
        }

        final UnaryOperator op = HasToken.fromContext(ctx, UnaryOperator.values());
        assert op != null : "Bad implementation";

        final Node expression = ctx.unary_expression().accept(this);
        final PrefixOperationNode node = new PrefixOperationNode(SourcePosition.create(ctx.start), op);
        node.setExpression(expression);
        node.setConstant(isConst(expression));
        return node;
    }

    @Override
    public Node visitMultiplicative_expression(GLSLParser.Multiplicative_expressionContext ctx) {
        final NumericOperator op = HasToken.fromContext(ctx,
            NumericOperator.MUL,
            NumericOperator.DIV,
            NumericOperator.MOD
        );
        assert op != null : "Bad implementation";

        final NumericOperationNode node = new NumericOperationNode(SourcePosition.create(ctx.start), op);

        Node left = ctx.numeric_expression(0).accept(this);
        Node right = ctx.numeric_expression(1).accept(this);
        node.setLeft(left);
        node.setRight(right);
        node.setConstant(isConst(left) && isConst(right));

        return node;
    }

    @Override
    public Node visitAdditive_expression(GLSLParser.Additive_expressionContext ctx) {
        final NumericOperator op = HasToken.fromContext(ctx,
            NumericOperator.ADD,
            NumericOperator.SUB
        );
        assert op != null : "Bad implementation";

        final NumericOperationNode node = new NumericOperationNode(SourcePosition.create(ctx.start), op);
        Node left = ctx.numeric_expression(0).accept(this);
        Node right = ctx.numeric_expression(1).accept(this);
        node.setLeft(left);
        node.setRight(right);
        node.setConstant(isConst(left) && isConst(right));
        return node;
    }

    @Override
    public Node visitShift_expression(GLSLParser.Shift_expressionContext ctx) {
        final BitOperator op = HasToken.fromContext(ctx,
            BitOperator.SHIFT_LEFT,
            BitOperator.SHIFT_RIGHT
        );
        assert op != null : "Bad implementation";

        final BitOperationNode node = new BitOperationNode(SourcePosition.create(ctx.start), op);
        Node left = ctx.numeric_expression(0).accept(this);
        Node right = ctx.numeric_expression(1).accept(this);
        node.setLeft(left);
        node.setRight(right);
        node.setConstant(isConst(left) && isConst(right));
        return node;
    }

    @Override
    public Node visitCondition(GLSLParser.ConditionContext ctx) {
        final SourcePosition position = SourcePosition.create(ctx.start);

        if (ctx.expression() != null) {
            return ctx.expression().accept(this);
        }

        final TypeNode typeNode = NodeUtil.cast(ctx.fully_specified_type().accept(this));
        if (typeNode.getStructDeclaration() != null) {
            final SourcePosition sourcePosition = typeNode.getStructDeclaration().getSourcePosition();
            throw new SourcePositionException(sourcePosition, SYNTAX_ERROR(INVALID_STRUCT_DECLARATION));
        }

        final String identifier = ctx.IDENTIFIER().getText();
        final Node initializer = ctx.initializer().accept(this);

        final FullySpecifiedType fullySpecifiedType = typeNode.getFullySpecifiedType();

        VariableDeclarationNode declarationNode = new VariableDeclarationNode(position, false, fullySpecifiedType, identifier, null, initializer, null);

        // register the declaration and usage of the type to enable easy look up for later passes
        parserContext.getVariableRegistry().declareVariable(parserContext.currentContext(), declarationNode);
        parserContext.getTypeRegistry().usage(parserContext.currentContext(), fullySpecifiedType.getType(), declarationNode);

        final VariableDeclarationListNode listNode = new VariableDeclarationListNode(position, fullySpecifiedType);
        listNode.addChild(declarationNode);
        return listNode;
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

        TernaryConditionNode node = new TernaryConditionNode(SourcePosition.create(ctx.start), condition, thenNode, elseNode);
        node.setConstant(isConst(condition) && isConst(thenNode) && isConst(elseNode));

        return node;
    }

    @Override
    public Node visitConstant_expression(GLSLParser.Constant_expressionContext ctx) {
        final Node node = ctx.conditional_expression().accept(this);

        if (isConst(node)) {
            final SourcePosition position = SourcePosition.create(ctx.start);
            return new ConstantExpressionNode(position, node);
        }

        return node;
    }

    @Override
    public Node visitIteration_do_while_statement(GLSLParser.Iteration_do_while_statementContext ctx) {
        final DoWhileIterationNode node = new DoWhileIterationNode(SourcePosition.create(ctx.start));

        parserContext.enterContext(node);

        node.setStatement(ctx.statement_with_scope().accept(this));

        parserContext.exitContext();

        node.setCondition(ctx.expression().accept(this));
        return node;
    }

    @Override
    public Node visitField_selection(GLSLParser.Field_selectionContext ctx) {
        final SourcePosition position = SourcePosition.create(ctx.start);
        final String selection = ctx.IDENTIFIER().getText();

        if (ctx.LEFT_PAREN() != null) {
            return new LengthFunctionFieldSelectionNode(position);
        }

        return new FieldSelectionNode(position, selection);
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
        parserContext.enterContext(node);

        node.setInitialization(ctx.for_init_statement().accept(this));

        GLSLParser.For_rest_statementContext forRestStatement = ctx.for_rest_statement();
        if (forRestStatement != null) {
            node.setCondition(forRestStatement.condition().accept(this));
            node.setExpression(forRestStatement.expression().accept(this));
        }

        if (ctx.statement_no_new_scope() != null) {
            node.setStatement(ctx.statement_no_new_scope().accept(this));
        }

        parserContext.exitContext();
        return node;
    }

    @Override
    public Node visitFunction_call_header(GLSLParser.Function_call_headerContext ctx) {
        final GLSLParser.Type_specifierContext context = ctx.function_identifier().type_specifier();
        final SourcePosition position = SourcePosition.create(context.start);
        final GLSLParser.Type_specifier_no_arrayContext specifierCtx = context.type_specifier_no_array();

        String functionName = null;
        if (specifierCtx.struct_specifier() != null) {
            throw new SourcePositionException(position, SYNTAX_ERROR(INVALID_STRUCT_DECLARATION));
        } else if (specifierCtx.IDENTIFIER() != null) {
            // this is a reference to a function name or struct constructor
            functionName = specifierCtx.IDENTIFIER().getText();
        } else {
            // the type is a predefined type constructor
            functionName = specifierCtx.getText();
        }

        if (context.array_specifier() != null) {
            ArraySpecifierNode arraySpecifier = (ArraySpecifierNode) context.array_specifier().accept(this);
            return new FunctionCallNode(position, functionName, arraySpecifier.getArraySpecifiers());
        }

        return new FunctionCallNode(position, functionName);
    }

    @Override
    public Node visitFunction_prototype(GLSLParser.Function_prototypeContext ctx) {
        return ctx.function_declarator().accept(this);
    }

    @Override
    public Node visitFunction_declarator(GLSLParser.Function_declaratorContext ctx) {
        final GLSLParser.Function_headerContext functionHeader = ctx.function_header();
        final String functionName = functionHeader.IDENTIFIER().getText();

        final TypeNode returnTypeNode = NodeUtil.cast(functionHeader.fully_specified_type().accept(this));
        if (returnTypeNode.getStructDeclaration() != null) {
            throw new SourcePositionException(returnTypeNode.getStructDeclaration(), SYNTAX_ERROR(INVALID_STRUCT_DECLARATION));
        }

        final SourcePosition sourcePosition = SourcePosition.create(ctx.start);
        final FunctionPrototypeNode functionNode = new FunctionPrototypeNode(sourcePosition, functionName, returnTypeNode.getFullySpecifiedType());

        for (GLSLParser.Parameter_declarationContext parameterCtx : ctx.parameter_declaration()) {
            final ParameterDeclarationNode parameter = NodeUtil.cast(parameterCtx.accept(this));
            functionNode.addParameter(parameter);

            // prepare for some syntax checks
            FullySpecifiedType fullySpecifiedType = parameter.getFullySpecifiedType();
            TypeQualifierList qualifiers = fullySpecifiedType.getQualifiers();
            GLSLType type = fullySpecifiedType.getType();

            if (qualifiers != null) {
                if (TypeCombination.ofCategory(TypeCategory.Opaque, type)) {
                    if (qualifiers.contains(OUT) || qualifiers.contains(INOUT)) {
                        throw new SourcePositionException(parameter, SYNTAX_ERROR(OPAQUE_TYPE_LVALUE));
                    }
                }
            }
        }

        // if there's only a single parameter with the type void, remove it
        if (functionNode.getChildCount() == 1) {
            ParameterDeclarationNode parameter = functionNode.getChildAs(0);
            if (PredefinedType.VOID == parameter.getFullySpecifiedType().getType()) {
                functionNode.removeChild(parameter);
            }
        }

        if (isIncluded(sourcePosition)) {
            // mark the function as being included from a shared file.
            functionNode.setShared(true);
        }

        // construct a prototype for the function
        final GLSLType[] parameterTypes = new GLSLType[functionNode.getChildCount()];
        for (int i = 0; i < parameterTypes.length; i++) {
            ParameterDeclarationNode parameterNode = functionNode.getChildAs(i);

            // verify that the parameter doesn't have any initializers
            if (parameterNode.getInitializer() != null) {
                throw new SourcePositionException(parameterNode, SYNTAX_ERROR(INITIALIZER_ON_PARAMETER));
            }

            parameterTypes[i] = parameterNode.getFullySpecifiedType().getType();
        }

        final GLSLType returnType = returnTypeNode.getFullySpecifiedType().getType();
        functionNode.setPrototype(new FunctionPrototype(false, returnType, parameterTypes));

        // register the function
        parserContext.getFunctionRegistry().declareFunction(functionNode);

        return functionNode;
    }

    @Override
    public Node visitFunction_header(GLSLParser.Function_headerContext ctx) {
        throw new BadImplementationException();
    }

    @Override
    public Node visitFunction_identifier(GLSLParser.Function_identifierContext ctx) {
        throw new BadImplementationException();
    }

    @Override
    public Node visitInit_declarator_list(GLSLParser.Init_declarator_listContext ctx) {
        final SourcePosition position = SourcePosition.create(ctx.start);
        final boolean isShared = isIncluded(position);

        if (ctx.single_declaration() != null) {
            final VariableDeclarationNode declarationNode = NodeUtil.cast(ctx.single_declaration().accept(this));
            declarationNode.setShared(isShared);

            // always return a list node
            FullySpecifiedType originalType = declarationNode.getOriginalType();
            final VariableDeclarationListNode listNode = new VariableDeclarationListNode(position, originalType);
            listNode.setShared(isShared);
            listNode.addChild(declarationNode);

            // register the declaration and usage of the type to enable easy look up for later passes
            parserContext.getVariableRegistry().declareVariable(parserContext.currentContext(), declarationNode);
            parserContext.getTypeRegistry().usage(parserContext.currentContext(), originalType.getType(), declarationNode);

            return listNode;
        }

        final VariableDeclarationListNode listNode = NodeUtil.cast(ctx.init_declarator_list().accept(this));
        listNode.setShared(isShared);

        final String identifier = ctx.IDENTIFIER().getText();

        ArraySpecifiers arraySpecifiers = null;
        if (ctx.array_specifier() != null) {
            ArraySpecifierNode specifierNode = NodeUtil.cast(ctx.array_specifier().accept(this));
            arraySpecifiers = specifierNode.getArraySpecifiers();
        }

        Node initializer = null;
        if (ctx.initializer() != null) {
            initializer = ctx.initializer().accept(this);
        }

        // TODO: revisit for constant

        FullySpecifiedType fullySpecifiedType = listNode.getFullySpecifiedType();
        VariableDeclarationNode declarationNode = new VariableDeclarationNode(position, false, fullySpecifiedType, identifier, arraySpecifiers, initializer, null);
        declarationNode.setShared(isShared);
        listNode.addChild(declarationNode);

        // register the declaration and usage of the type to enable easy look up for later passes
        parserContext.getVariableRegistry().declareVariable(parserContext.currentContext(), declarationNode);
        parserContext.getTypeRegistry().usage(parserContext.currentContext(), fullySpecifiedType.getType(), declarationNode);

        return listNode;
    }

    @Override
    public Node visitSingle_declaration(GLSLParser.Single_declarationContext ctx) {
        final SourcePosition position = SourcePosition.create(ctx.start);
        final TypeNode typeNode = NodeUtil.cast(ctx.fully_specified_type().accept(this));
        final StructDeclarationNode structDeclaration = typeNode.getStructDeclaration();

        if (ctx.IDENTIFIER() == null) {
            // early type declaration
            return new TypeDeclarationNode(position, typeNode.getFullySpecifiedType(), typeNode.getStructDeclaration());
        }

        final String identifier = ctx.IDENTIFIER().getText();

        ArraySpecifiers arraySpecifiers = null;
        if (ctx.array_specifier() != null) {
            ArraySpecifierNode specifierNode = NodeUtil.cast(ctx.array_specifier().accept(this));
            arraySpecifiers = specifierNode.getArraySpecifiers();
        }

        Node initializer = null;
        if (ctx.initializer() != null) {
            initializer = ctx.initializer().accept(this);
        }

        return new VariableDeclarationNode(position, false, typeNode.getFullySpecifiedType(), identifier, arraySpecifiers, initializer, typeNode.getStructDeclaration());
    }

    @Override
    public Node visitParameter_declaration(GLSLParser.Parameter_declarationContext ctx) {
        final SourcePosition position = SourcePosition.create(ctx.start);

        // handle the type qualifier
        TypeQualifierListNode qualifiers = null;
        if (ctx.type_qualifier() != null) {
            qualifiers = NodeUtil.cast(ctx.type_qualifier().accept(this));
        }

        final TypeSpecifierNode typeSpecifier = NodeUtil.cast(ctx.type_specifier().accept(this));
        if (typeSpecifier.getStructDeclaration() != null) {
            throw new SourcePositionException(typeSpecifier.getStructDeclaration(), SYNTAX_ERROR(INVALID_STRUCT_DECLARATION));
        }

        final String identifier = ANTLRUtils.toString(ctx.IDENTIFIER(), null);

        FullySpecifiedType type = toFullySpecifiedType(qualifiers, typeSpecifier);
        ArraySpecifiers arraySpecifiers = null;
        if (ctx.array_specifier() != null) {
            ArraySpecifierNode specifierNode = NodeUtil.cast(ctx.array_specifier().accept(this));
            arraySpecifiers = specifierNode.getArraySpecifiers();
        }

        final ParameterDeclarationNode declarationNode = new ParameterDeclarationNode(position, type, identifier, arraySpecifiers);
        if (identifier != null && type.getType() != PredefinedType.VOID) {
            parserContext.getVariableRegistry().declareVariable(parserContext.currentContext(), declarationNode);
        }
        return declarationNode;
    }

    @Override
    public Node visitInitializer(GLSLParser.InitializerContext ctx) {
        if (ctx.assignment_expression() != null) {
            return ctx.assignment_expression().accept(this);
        }

        final SourcePosition position = SourcePosition.create(ctx.start);
        final InitializerListNode listNode = new InitializerListNode(position);

        // TODO: do something for constant expressions?

        for (GLSLParser.InitializerContext initializer : ctx.initializer()) {
            listNode.addChild(initializer.accept(this));
        }

        return listNode;
    }

    @Override
    public Node visitJump_statement(GLSLParser.Jump_statementContext ctx) {
        final SourcePosition position = SourcePosition.create(ctx.start);
        if (ctx.CONTINUE() != null) {
            return new ContinueLeafNode(position);
        }
        if (ctx.BREAK() != null) {
            return new BreakLeafNode(position);
        }
        if (ctx.DISCARD() != null) {
            return new DiscardLeafNode(position);
        }
        final ReturnNode returnNode = new ReturnNode(position);
        if (ctx.expression() != null) {
            returnNode.setExpression(ctx.expression().accept(this));
        }
        return returnNode;
    }

    @Override
    public Node visitLogical_and_expression(GLSLParser.Logical_and_expressionContext ctx) {
        final LogicalOperationNode node = new LogicalOperationNode(SourcePosition.create(ctx.start), LogicalOperator.AND);
        Node left = ctx.logical_expression(0).accept(this);
        Node right = ctx.logical_expression(1).accept(this);
        node.setLeft(left);
        node.setRight(right);
        node.setConstant(isConst(left) && isConst(right));
        return node;
    }

    @Override
    public Node visitEquality_expression(GLSLParser.Equality_expressionContext ctx) {
        final RelationalOperator op = HasToken.fromContext(ctx,
            RelationalOperator.Equal,
            RelationalOperator.NotEqual
        );
        assert op != null : "Bad implementation";

        final RelationalOperationNode node = new RelationalOperationNode(SourcePosition.create(ctx.start), op);
        Node left = ctx.logical_expression(0).accept(this);
        Node right = ctx.logical_expression(1).accept(this);
        node.setLeft(left);
        node.setRight(right);
        node.setConstant(isConst(left) && isConst(right));
        return node;
    }

    @Override
    public Node visitRelational_expression(GLSLParser.Relational_expressionContext ctx) {
        final RelationalOperator op = HasToken.fromContext(ctx,
            RelationalOperator.LessThan,
            RelationalOperator.GreaterThan,
            RelationalOperator.LessThanOrEqual,
            RelationalOperator.GreaterThanOrEqual
        );
        assert op != null : "Bad implementation";

        final RelationalOperationNode node = new RelationalOperationNode(SourcePosition.create(ctx.start), op);
        Node left = ctx.logical_expression(0).accept(this);
        Node right = ctx.logical_expression(1).accept(this);
        node.setLeft(left);
        node.setRight(right);
        node.setConstant(isConst(left) && isConst(right));
        return node;
    }

    @Override
    public Node visitLogical_or_expression(GLSLParser.Logical_or_expressionContext ctx) {
        final LogicalOperationNode node = new LogicalOperationNode(SourcePosition.create(ctx.start), LogicalOperator.OR);
        Node left = ctx.logical_expression(0).accept(this);
        Node right = ctx.logical_expression(1).accept(this);
        node.setLeft(left);
        node.setRight(right);
        node.setConstant(isConst(left) && isConst(right));
        return node;
    }

    @Override
    public Node visitLogical_xor_expression(GLSLParser.Logical_xor_expressionContext ctx) {
        final LogicalOperationNode node = new LogicalOperationNode(SourcePosition.create(ctx.start), LogicalOperator.XOR);
        Node left = ctx.logical_expression(0).accept(this);
        Node right = ctx.logical_expression(1).accept(this);
        node.setLeft(left);
        node.setRight(right);
        node.setConstant(isConst(left) && isConst(right));
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
            final FieldSelectionNode fieldSelectionNode = NodeUtil.cast(ctx.field_selection().accept(this));
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
        final PredefinedType type = HasToken.fromContext(ctx.type_specifier(), PredefinedType.values());

        if (type != null) {
            if (type == INT || type == PredefinedType.FLOAT || type.category() == TypeCategory.Opaque) {
                final SourcePosition position = SourcePosition.create(ctx.start);
                return new PrecisionDeclarationNode(position, qualifier, type);
            }
        }

        final SourcePosition typePosition = SourcePosition.create(ctx.type_specifier().start);
        final String typeName = ctx.type_specifier().getText();
        throw new SourcePositionException(typePosition, SYNTAX_ERROR(typeName, PRECISION_NOT_SUPPORTED));
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
        final SourcePosition position = SourcePosition.create(ctx.start);

        TypeQualifierListNode qualifiers = null;
        if (ctx.type_qualifier() != null) {
            qualifiers = NodeUtil.cast(ctx.type_qualifier().accept(this));
        }

        final TypeSpecifierNode typeSpecifier = NodeUtil.cast(ctx.type_specifier().accept(this));
        final FullySpecifiedType type = toFullySpecifiedType(qualifiers, typeSpecifier);
        final VariableDeclarationListNode declarationList = new VariableDeclarationListNode(position, type);

        StructDeclarationNode structDeclaration = typeSpecifier.getStructDeclaration();

        for (GLSLParser.Struct_declaratorContext fieldCtx : ctx.struct_declarator_list().struct_declarator()) {
            String identifier = fieldCtx.IDENTIFIER().getText();

            ArraySpecifiers arraySpecifiers = null;
            if (fieldCtx.array_specifier() != null) {
                ArraySpecifierNode specifierNode = NodeUtil.cast(fieldCtx.array_specifier().accept(this));
                arraySpecifiers = specifierNode.getArraySpecifiers();
            }

            final SourcePosition fieldPosition = SourcePosition.create(fieldCtx.start);
            declarationList.addChild(new VariableDeclarationNode(fieldPosition, false, type, identifier, arraySpecifiers, null, structDeclaration));
            structDeclaration = null; // only register the struct on the first variable

        }

        return declarationList;
    }

    @Override
    public Node visitStruct_declarator_list(GLSLParser.Struct_declarator_listContext ctx) {
        throw new BadImplementationException();
    }

    @Override
    public Node visitStruct_declarator(GLSLParser.Struct_declaratorContext ctx) {
        throw new BadImplementationException();
    }

    @Override
    public Node visitStruct_specifier(GLSLParser.Struct_specifierContext ctx) {
        final SourcePosition position = SourcePosition.create(ctx.start);
        final String identifier = ANTLRUtils.toString(ctx.IDENTIFIER(), null);

        final StructDeclarationNode declarationNode = new StructDeclarationNode(position, identifier);
        for (GLSLParser.Struct_declarationContext declarationContext : ctx.struct_declaration()) {
            VariableDeclarationListNode variableDeclaration = NodeUtil.cast(declarationContext.accept(this));
            declarationNode.addFieldDeclaration(variableDeclaration);
        }

        return declarationNode;
    }

    @Override
    public Node visitStruct_init_declaration(GLSLParser.Struct_init_declarationContext ctx) {
        final SourcePosition position = SourcePosition.create(ctx.start);
        final TypeQualifierListNode qualifiers = NodeUtil.cast(ctx.type_qualifier().accept(this));
        final String structIdentifier = ANTLRUtils.toString(ctx.IDENTIFIER(0), null);

        StructDeclarationNode structDeclaration = new StructDeclarationNode(SourcePosition.create(ctx.struct_declaration(0).start), structIdentifier);
        for (GLSLParser.Struct_declarationContext declarationContext : ctx.struct_declaration()) {
            VariableDeclarationListNode fieldDeclaration = NodeUtil.cast(declarationContext.accept(this));
            structDeclaration.addFieldDeclaration(fieldDeclaration);
        }

        final String identifier = ANTLRUtils.toString(ctx.IDENTIFIER(1), null);

        ArraySpecifiers arraySpecifiers = null;
        if (ctx.array_specifier() != null) {
            final ArraySpecifierNode specifierNode = NodeUtil.cast(ctx.array_specifier().accept(this));
            arraySpecifiers = specifierNode.getArraySpecifiers();
        }

        return new InterfaceBlockNode(position, qualifiers.getTypeQualifiers(), structDeclaration, identifier, arraySpecifiers);
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
        // take care of recursion
        TypeQualifierListNode qualifiers;
        if (ctx.type_qualifier() == null) {
            qualifiers = new TypeQualifierListNode(SourcePosition.create(ctx.start));
        } else {
            qualifiers = NodeUtil.cast(ctx.type_qualifier().accept(this));
        }

        final GLSLParser.Single_type_qualifierContext context = ctx.single_type_qualifier();
        if (context.storage_qualifier() != null) {
            // storage qualifier
            final SourcePosition position = SourcePosition.create(context.storage_qualifier().start);

            StorageQualifier storageQualifier = HasToken.fromString(context.storage_qualifier().getText(), StorageQualifier.values());
            if (storageQualifier == StorageQualifier.SUBROUTINE) {
                GLSLParser.Type_name_listContext typeNamesCtx = context.storage_qualifier().type_name_list();


                if (typeNamesCtx == null) {
                    qualifiers.addTypeQualifier(new SubroutineQualifier(position));
                } else {
                    List<String> typeNames = typeNamesCtx.IDENTIFIER().stream()
                        .map(TerminalNode::getText)
                        .collect(Collectors.toList());

                    qualifiers.addTypeQualifier(new SubroutineQualifier(position, typeNames));
                }
            }

            qualifiers.addTypeQualifier(storageQualifier);
        } else if (context.memory_qualifier() != null) {
            // memory qualifier
            qualifiers.addTypeQualifier(from(context.memory_qualifier(), MemoryQualifier.values()));
        } else if (context.layout_qualifier() != null) {
            // layout qualifier
            final LayoutQualifier qualifier = new LayoutQualifier(SourcePosition.create(context.layout_qualifier().start));
            final GLSLParser.Layout_qualifier_id_listContext listContext = context.layout_qualifier().layout_qualifier_id_list();

            for (GLSLParser.Layout_qualifier_idContext idCtx : listContext.layout_qualifier_id()) {
                final SourcePosition position = SourcePosition.create(idCtx.start);
                final String qualifierName = ANTLRUtils.toString(idCtx.SHARED(), ANTLRUtils.toString(idCtx.IDENTIFIER(), null));

                if (idCtx.constant_expression() == null) {
                    qualifier.addQualifierId(new LayoutQualifierId(position, qualifierName));
                } else {
                    Node expression = idCtx.constant_expression().accept(this);
                    int value = evaluateInt(expression);
                    parserContext.dereferenceTree(expression); // get rid of variable references in the expression
                    qualifier.addQualifierId(new LayoutQualifierId(position, qualifierName, value));
                }
            }
            qualifiers.addTypeQualifier(qualifier);
        } else if (context.precision_qualifier() != null) {
            // precision qualifier
            qualifiers.addTypeQualifier(from(context.precision_qualifier(), PrecisionQualifier.values()));
        } else if (context.interpolation_qualifier() != null) {
            // interpolation qualifier
            qualifiers.addTypeQualifier(from(context.interpolation_qualifier(), InterpolationQualifier.values()));
        } else if (context.invariant_qualifier() != null) {
            // invariant qualifier
            qualifiers.addTypeQualifier(from(context.invariant_qualifier(), InvariantQualifier.values()));
        } else if (context.precise_qualifier() != null) {
            // precise qualifier
            qualifiers.addTypeQualifier(from(context.precise_qualifier(), PrecisionQualifier.values()));
        } else {
            assert false : "No TypeQualifier match!";
        }

        return qualifiers;
    }

    @Override
    public Node visitQualifier_declaration(GLSLParser.Qualifier_declarationContext ctx) {
        final SourcePosition position = SourcePosition.create(ctx.start);
        final TypeQualifierListNode qualifiers = NodeUtil.cast(ctx.type_qualifier().accept(this));

        List<String> identifiers = new ArrayList<>();
        for (TerminalNode node : ctx.IDENTIFIER()) {
            identifiers.add(node.getText());
        }

        return new TypeQualifierDeclarationNode(position, qualifiers.getTypeQualifiers(), identifiers);
    }

    @Override
    public Node visitFully_specified_type(GLSLParser.Fully_specified_typeContext ctx) {
        final SourcePosition position = SourcePosition.create(ctx.start);

        TypeQualifierListNode qualifiers = null;
        if (ctx.type_qualifier() != null) {
            qualifiers = NodeUtil.cast(ctx.type_qualifier().accept(this));
        }
        TypeSpecifierNode typeSpecifier = NodeUtil.cast(ctx.type_specifier().accept(this));
        FullySpecifiedType type = toFullySpecifiedType(qualifiers, typeSpecifier);

        return new TypeNode(position, type, typeSpecifier.getStructDeclaration());
    }

    @Override
    public Node visitType_specifier(GLSLParser.Type_specifierContext ctx) {
        final SourcePosition position = SourcePosition.create(ctx.start);
        final GLSLParser.Type_specifier_no_arrayContext specifierCtx = ctx.type_specifier_no_array();

        StructDeclarationNode structDeclaration = null;
        GLSLType baseType;
        if (specifierCtx.struct_specifier() != null) {
            // the type is a struct
            structDeclaration = NodeUtil.cast(specifierCtx.struct_specifier().accept(this));
            baseType = new StructType(structDeclaration);

            // register the struct
            parserContext.getTypeRegistry().declare(new FullySpecifiedType(baseType));
        } else if (specifierCtx.IDENTIFIER() != null) {
            // this is a reference to a struct declaration
            final String customTypeName = specifierCtx.IDENTIFIER().getText();
            try {
                final FullySpecifiedType fullySpecifiedType = parserContext.getTypeRegistry().resolve(customTypeName);
                baseType = fullySpecifiedType.getType();
            } catch (TypeException e) {
                throw new SourcePositionException(position, UNKNOWN_SYMBOL(customTypeName));
            }
        } else {
            // the type is a predefined type
            baseType = HasToken.fromString(specifierCtx.getText(), PredefinedType.values());
        }

        if (ctx.array_specifier() != null) {
            ArraySpecifierNode arraySpecifier = (ArraySpecifierNode) ctx.array_specifier().accept(this);
            return new TypeSpecifierNode(position, structDeclaration, baseType, arraySpecifier.getArraySpecifiers());
        }

        return new TypeSpecifierNode(position, structDeclaration, baseType, null);
    }

    @Override
    public Node visitArray_specifier(GLSLParser.Array_specifierContext ctx) {
        final SourcePosition position = SourcePosition.create(ctx.start);

        ArraySpecifierNode list;
        if (ctx.array_specifier() != null) {
            list = (ArraySpecifierNode) ctx.array_specifier().accept(this);
        } else {
            list = new ArraySpecifierNode(position);
        }

        ArraySpecifiers specifiers = list.getArraySpecifiers();
        if (ctx.constant_expression() == null) {
            specifiers.addSpecifier(new ArraySpecifier(position));
        } else {
            Node expression = ctx.constant_expression().accept(this);
            int dimension = evaluateInt(expression);
            parserContext.dereferenceTree(expression); // get rid of variable references in the expression
            specifiers.addSpecifier(new ArraySpecifier(position, dimension));
        }

        return list;
    }

    @Override
    public Node visitIteration_while_statement(GLSLParser.Iteration_while_statementContext ctx) {
        final WhileIterationNode node = new WhileIterationNode(SourcePosition.create(ctx.start));
        node.setCondition(ctx.condition().accept(this));

        if (ctx.statement_no_new_scope() != null) {
            parserContext.enterContext(node);
            node.setStatement(ctx.statement_no_new_scope().accept(this));
            parserContext.exitContext();
        }

        return node;
    }

    @Override
    public Node visitFunction_definition(GLSLParser.Function_definitionContext ctx) {
        final SourcePosition position = SourcePosition.create(ctx.start);
        final FunctionDefinitionNode node = new FunctionDefinitionNode(position, null, null);

        parserContext.enterContext(node);

        final FunctionPrototypeNode prototype = NodeUtil.cast(ctx.function_prototype().accept(this));
        node.setFunctionPrototype(prototype);
        node.setStatements(NodeUtil.cast(ctx.compound_statement_no_new_scope().accept(this)));

        parserContext.exitContext();
        parserContext.getFunctionRegistry().declareFunction(prototype);

        return node;
    }

    @Override
    public Node visitFunction_call(GLSLParser.Function_callContext ctx) {
        final FunctionCallNode functionCallNode = NodeUtil.cast(ctx.function_call_header().accept(this));

        // check if the function call has arguments
        boolean allArgumentsAreConst = true;
        if (ctx.VOID() == null) {
            for (GLSLParser.Assignment_expressionContext argCtx : ctx.assignment_expression()) {
                // parse each argument and add them to the function
                final Node argument = argCtx.accept(this);
                allArgumentsAreConst &= isConst(argument);
                functionCallNode.addChild(argument);
            }
        }

        if (allArgumentsAreConst && functionCallNode.getArraySpecifiers() == null) {
            final String functionName = functionCallNode.getIdentifier().original();
            final ConstFunction constFunction = HasToken.fromString(functionName, ConstFunction.values());
            if (constFunction != null) {
                functionCallNode.setConstant(true);
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
            return new NumericLeafNode(position, numeric);
        }
        if (ctx.FLOATCONSTANT() != null) {
            Numeric numeric = Numeric.create(ctx.FLOATCONSTANT().getText());
            return new NumericLeafNode(position, numeric);
        }
        if (ctx.BOOLCONSTANT() != null) {
            return new BooleanLeafNode(position, Boolean.valueOf(ctx.BOOLCONSTANT().getText()));
        }
        if (ctx.expression() != null) {
            final Node expression = ctx.expression().accept(this);
            return new ParenthesisNode(position, expression);
        }

        // this is a variable
        final VariableRegistry variableRegistry = parserContext.getVariableRegistry();
        final GLSLContext currentContext = parserContext.currentContext();

        final String identifier = ctx.variable_identifier().IDENTIFIER().getText();
        try {
            final ResolutionResult result = variableRegistry.resolve(currentContext, identifier, Identifier.Mode.Original);
            VariableNode variableNode = new VariableNode(position, result.getDeclaration());
            variableRegistry.registerVariableUsage(variableNode);
            return variableNode;
        } catch (VariableException e) {
            SourcePosition variablePosition = SourcePosition.create(ctx.variable_identifier().start);
            throw new SourcePositionException(variablePosition, UNKNOWN_SYMBOL(identifier), e);
        }
    }

    @Override
    public Node visitBit_op_expression(GLSLParser.Bit_op_expressionContext ctx) {
        BitOperator op;

        if (ctx.AMPERSAND() != null) {
            op = BitOperator.AND;
        } else if (ctx.VERTICAL_BAR() != null) {
            op = BitOperator.OR;
        } else if (ctx.CARET() != null) {
            op = BitOperator.XOR;
        } else {
            throw new UnsupportedOperationException("The rule 'bit_op_expressoin' is not implemented correctly.");
        }

        final BitOperationNode node = new BitOperationNode(SourcePosition.create(ctx.start), op);
        node.setLeft(ctx.logical_expression(0).accept(this));
        node.setRight(ctx.logical_expression(1).accept(this));
        return node;
    }

    @Override
    public Node visitSwitch_statement(GLSLParser.Switch_statementContext ctx) {
        final Node selector = ctx.expression().accept(this);
        SwitchNode node = new SwitchNode(SourcePosition.create(ctx.start), selector);

        parserContext.enterContext(node);

        for (GLSLParser.Switch_case_statementContext childCtx : ctx.switch_case_statement()) {
            node.addChild(childCtx.accept(this));
        }

        parserContext.exitContext();

        return node;
    }

    @Override
    public Node visitSwitch_case_statement(GLSLParser.Switch_case_statementContext ctx) {
        final SourcePosition position = SourcePosition.create(ctx.start);

        if (ctx.DEFAULT() != null) {
            final Node statements = ctx.statement_with_scope().accept(this);
            return new DefaultCaseNode(position, statements);
        }

        final Node label = ctx.expression().accept(this);
        final Node statements = ctx.statement_with_scope().accept(this);
        return new CaseNode(position, label, statements);
    }

    private FullySpecifiedType toFullySpecifiedType(TypeQualifierListNode qualifiers, TypeSpecifierNode typeSpecifier) {
        TypeQualifierList qualifierList = (qualifiers != null) ? qualifiers.getTypeQualifiers() : null;
        GLSLType type = typeSpecifier.getBaseType();
        if (type == null) {
            throw new BadImplementationException("Not implemented");
        }

        if (typeSpecifier.getArraySpecifiers() != null) {
            return new FullySpecifiedType(qualifierList, typeSpecifier.getArraySpecifiers().transform(type));
        }
        return new FullySpecifiedType(qualifierList, type);
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
        return !mapper.getDefaultId().equals(sourcePositionId.getId());
    }

    @Override
    protected Node aggregateResult(Node aggregate, Node nextResult) {
        if (nextResult == null) {
            return aggregate;
        }
        return nextResult;
    }

    @Override
    public Node visitAssignment_operator(GLSLParser.Assignment_operatorContext ctx) {
        throw new BadImplementationException();
    }

    private int evaluateInt(Node node) {
        Numeric numeric = ConstExpressionEvaluatorVisitor.evaluate(parserContext, node);
        if (anyOf(numeric.getType(), INT, UINT) && numeric.signum() >= 0) {
            return numeric.intValue();
        }
        throw new SourcePositionException(node, INCOMPATIBLE_TYPE(numeric.getType(), EXPECTED_NON_NEGATIVE_INTEGER));
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
        if (node instanceof TerminalNodeImpl) {
            return result;
        }
        if (result == null) {
            System.err.format("ERROR: null result from terminal node %s : %s\n", node.getText(), node.getClass().getSimpleName());
        }
        return result;
    }
}
