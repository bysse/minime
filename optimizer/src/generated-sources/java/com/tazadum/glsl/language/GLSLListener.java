// Generated from C:\Dev\Java\glsl-optimizer\src\main\antlr\GLSL.g4 by ANTLR 4.7
package com.tazadum.glsl.language;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link GLSLParser}.
 */
public interface GLSLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link GLSLParser#translation_unit}.
	 * @param ctx the parse tree
	 */
	void enterTranslation_unit(GLSLParser.Translation_unitContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#translation_unit}.
	 * @param ctx the parse tree
	 */
	void exitTranslation_unit(GLSLParser.Translation_unitContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#variable_identifier}.
	 * @param ctx the parse tree
	 */
	void enterVariable_identifier(GLSLParser.Variable_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#variable_identifier}.
	 * @param ctx the parse tree
	 */
	void exitVariable_identifier(GLSLParser.Variable_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#primary_expression}.
	 * @param ctx the parse tree
	 */
	void enterPrimary_expression(GLSLParser.Primary_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#primary_expression}.
	 * @param ctx the parse tree
	 */
	void exitPrimary_expression(GLSLParser.Primary_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#postfix_expression}.
	 * @param ctx the parse tree
	 */
	void enterPostfix_expression(GLSLParser.Postfix_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#postfix_expression}.
	 * @param ctx the parse tree
	 */
	void exitPostfix_expression(GLSLParser.Postfix_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#primary_expression_or_function_call}.
	 * @param ctx the parse tree
	 */
	void enterPrimary_expression_or_function_call(GLSLParser.Primary_expression_or_function_callContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#primary_expression_or_function_call}.
	 * @param ctx the parse tree
	 */
	void exitPrimary_expression_or_function_call(GLSLParser.Primary_expression_or_function_callContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#integer_expression}.
	 * @param ctx the parse tree
	 */
	void enterInteger_expression(GLSLParser.Integer_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#integer_expression}.
	 * @param ctx the parse tree
	 */
	void exitInteger_expression(GLSLParser.Integer_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#function_call}.
	 * @param ctx the parse tree
	 */
	void enterFunction_call(GLSLParser.Function_callContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#function_call}.
	 * @param ctx the parse tree
	 */
	void exitFunction_call(GLSLParser.Function_callContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#function_call_header}.
	 * @param ctx the parse tree
	 */
	void enterFunction_call_header(GLSLParser.Function_call_headerContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#function_call_header}.
	 * @param ctx the parse tree
	 */
	void exitFunction_call_header(GLSLParser.Function_call_headerContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#function_identifier}.
	 * @param ctx the parse tree
	 */
	void enterFunction_identifier(GLSLParser.Function_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#function_identifier}.
	 * @param ctx the parse tree
	 */
	void exitFunction_identifier(GLSLParser.Function_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#constructor_identifier}.
	 * @param ctx the parse tree
	 */
	void enterConstructor_identifier(GLSLParser.Constructor_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#constructor_identifier}.
	 * @param ctx the parse tree
	 */
	void exitConstructor_identifier(GLSLParser.Constructor_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#unary_expression}.
	 * @param ctx the parse tree
	 */
	void enterUnary_expression(GLSLParser.Unary_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#unary_expression}.
	 * @param ctx the parse tree
	 */
	void exitUnary_expression(GLSLParser.Unary_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#unary_operator}.
	 * @param ctx the parse tree
	 */
	void enterUnary_operator(GLSLParser.Unary_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#unary_operator}.
	 * @param ctx the parse tree
	 */
	void exitUnary_operator(GLSLParser.Unary_operatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SubtractionExpression}
	 * labeled alternative in {@link GLSLParser#numeric_expression}.
	 * @param ctx the parse tree
	 */
	void enterSubtractionExpression(GLSLParser.SubtractionExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SubtractionExpression}
	 * labeled alternative in {@link GLSLParser#numeric_expression}.
	 * @param ctx the parse tree
	 */
	void exitSubtractionExpression(GLSLParser.SubtractionExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AdditiveExpression}
	 * labeled alternative in {@link GLSLParser#numeric_expression}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpression(GLSLParser.AdditiveExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AdditiveExpression}
	 * labeled alternative in {@link GLSLParser#numeric_expression}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpression(GLSLParser.AdditiveExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnaryExpression}
	 * labeled alternative in {@link GLSLParser#numeric_expression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpression(GLSLParser.UnaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnaryExpression}
	 * labeled alternative in {@link GLSLParser#numeric_expression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpression(GLSLParser.UnaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DivisionExpression}
	 * labeled alternative in {@link GLSLParser#numeric_expression}.
	 * @param ctx the parse tree
	 */
	void enterDivisionExpression(GLSLParser.DivisionExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DivisionExpression}
	 * labeled alternative in {@link GLSLParser#numeric_expression}.
	 * @param ctx the parse tree
	 */
	void exitDivisionExpression(GLSLParser.DivisionExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MultiplicationExpression}
	 * labeled alternative in {@link GLSLParser#numeric_expression}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicationExpression(GLSLParser.MultiplicationExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MultiplicationExpression}
	 * labeled alternative in {@link GLSLParser#numeric_expression}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicationExpression(GLSLParser.MultiplicationExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LogicalInEquality}
	 * labeled alternative in {@link GLSLParser#logical_expression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalInEquality(GLSLParser.LogicalInEqualityContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LogicalInEquality}
	 * labeled alternative in {@link GLSLParser#logical_expression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalInEquality(GLSLParser.LogicalInEqualityContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NumericDelegator}
	 * labeled alternative in {@link GLSLParser#logical_expression}.
	 * @param ctx the parse tree
	 */
	void enterNumericDelegator(GLSLParser.NumericDelegatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NumericDelegator}
	 * labeled alternative in {@link GLSLParser#logical_expression}.
	 * @param ctx the parse tree
	 */
	void exitNumericDelegator(GLSLParser.NumericDelegatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LogicalAnd}
	 * labeled alternative in {@link GLSLParser#logical_expression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalAnd(GLSLParser.LogicalAndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LogicalAnd}
	 * labeled alternative in {@link GLSLParser#logical_expression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalAnd(GLSLParser.LogicalAndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Relational}
	 * labeled alternative in {@link GLSLParser#logical_expression}.
	 * @param ctx the parse tree
	 */
	void enterRelational(GLSLParser.RelationalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Relational}
	 * labeled alternative in {@link GLSLParser#logical_expression}.
	 * @param ctx the parse tree
	 */
	void exitRelational(GLSLParser.RelationalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LogicalXor}
	 * labeled alternative in {@link GLSLParser#logical_expression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalXor(GLSLParser.LogicalXorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LogicalXor}
	 * labeled alternative in {@link GLSLParser#logical_expression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalXor(GLSLParser.LogicalXorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LogicalEquality}
	 * labeled alternative in {@link GLSLParser#logical_expression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalEquality(GLSLParser.LogicalEqualityContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LogicalEquality}
	 * labeled alternative in {@link GLSLParser#logical_expression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalEquality(GLSLParser.LogicalEqualityContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LogicalOr}
	 * labeled alternative in {@link GLSLParser#logical_expression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalOr(GLSLParser.LogicalOrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LogicalOr}
	 * labeled alternative in {@link GLSLParser#logical_expression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalOr(GLSLParser.LogicalOrContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#conditional_expression}.
	 * @param ctx the parse tree
	 */
	void enterConditional_expression(GLSLParser.Conditional_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#conditional_expression}.
	 * @param ctx the parse tree
	 */
	void exitConditional_expression(GLSLParser.Conditional_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#assignment_expression}.
	 * @param ctx the parse tree
	 */
	void enterAssignment_expression(GLSLParser.Assignment_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#assignment_expression}.
	 * @param ctx the parse tree
	 */
	void exitAssignment_expression(GLSLParser.Assignment_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#assignment_operator}.
	 * @param ctx the parse tree
	 */
	void enterAssignment_operator(GLSLParser.Assignment_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#assignment_operator}.
	 * @param ctx the parse tree
	 */
	void exitAssignment_operator(GLSLParser.Assignment_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(GLSLParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(GLSLParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#constant_expression}.
	 * @param ctx the parse tree
	 */
	void enterConstant_expression(GLSLParser.Constant_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#constant_expression}.
	 * @param ctx the parse tree
	 */
	void exitConstant_expression(GLSLParser.Constant_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FunctionDeclaration}
	 * labeled alternative in {@link GLSLParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDeclaration(GLSLParser.FunctionDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FunctionDeclaration}
	 * labeled alternative in {@link GLSLParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDeclaration(GLSLParser.FunctionDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VariableDeclaration}
	 * labeled alternative in {@link GLSLParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaration(GLSLParser.VariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VariableDeclaration}
	 * labeled alternative in {@link GLSLParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaration(GLSLParser.VariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PrecisionDeclaration}
	 * labeled alternative in {@link GLSLParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterPrecisionDeclaration(GLSLParser.PrecisionDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PrecisionDeclaration}
	 * labeled alternative in {@link GLSLParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitPrecisionDeclaration(GLSLParser.PrecisionDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#function_prototype}.
	 * @param ctx the parse tree
	 */
	void enterFunction_prototype(GLSLParser.Function_prototypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#function_prototype}.
	 * @param ctx the parse tree
	 */
	void exitFunction_prototype(GLSLParser.Function_prototypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#function_declarator}.
	 * @param ctx the parse tree
	 */
	void enterFunction_declarator(GLSLParser.Function_declaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#function_declarator}.
	 * @param ctx the parse tree
	 */
	void exitFunction_declarator(GLSLParser.Function_declaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#function_header}.
	 * @param ctx the parse tree
	 */
	void enterFunction_header(GLSLParser.Function_headerContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#function_header}.
	 * @param ctx the parse tree
	 */
	void exitFunction_header(GLSLParser.Function_headerContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#parameter_declaration}.
	 * @param ctx the parse tree
	 */
	void enterParameter_declaration(GLSLParser.Parameter_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#parameter_declaration}.
	 * @param ctx the parse tree
	 */
	void exitParameter_declaration(GLSLParser.Parameter_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#parameter_qualifier}.
	 * @param ctx the parse tree
	 */
	void enterParameter_qualifier(GLSLParser.Parameter_qualifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#parameter_qualifier}.
	 * @param ctx the parse tree
	 */
	void exitParameter_qualifier(GLSLParser.Parameter_qualifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#init_declarator_list}.
	 * @param ctx the parse tree
	 */
	void enterInit_declarator_list(GLSLParser.Init_declarator_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#init_declarator_list}.
	 * @param ctx the parse tree
	 */
	void exitInit_declarator_list(GLSLParser.Init_declarator_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#array_specifier}.
	 * @param ctx the parse tree
	 */
	void enterArray_specifier(GLSLParser.Array_specifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#array_specifier}.
	 * @param ctx the parse tree
	 */
	void exitArray_specifier(GLSLParser.Array_specifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#single_declaration}.
	 * @param ctx the parse tree
	 */
	void enterSingle_declaration(GLSLParser.Single_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#single_declaration}.
	 * @param ctx the parse tree
	 */
	void exitSingle_declaration(GLSLParser.Single_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#fully_specified_type}.
	 * @param ctx the parse tree
	 */
	void enterFully_specified_type(GLSLParser.Fully_specified_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#fully_specified_type}.
	 * @param ctx the parse tree
	 */
	void exitFully_specified_type(GLSLParser.Fully_specified_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#type_qualifier}.
	 * @param ctx the parse tree
	 */
	void enterType_qualifier(GLSLParser.Type_qualifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#type_qualifier}.
	 * @param ctx the parse tree
	 */
	void exitType_qualifier(GLSLParser.Type_qualifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#type_specifier}.
	 * @param ctx the parse tree
	 */
	void enterType_specifier(GLSLParser.Type_specifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#type_specifier}.
	 * @param ctx the parse tree
	 */
	void exitType_specifier(GLSLParser.Type_specifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#type_specifier_no_prec}.
	 * @param ctx the parse tree
	 */
	void enterType_specifier_no_prec(GLSLParser.Type_specifier_no_precContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#type_specifier_no_prec}.
	 * @param ctx the parse tree
	 */
	void exitType_specifier_no_prec(GLSLParser.Type_specifier_no_precContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#precision_qualifier}.
	 * @param ctx the parse tree
	 */
	void enterPrecision_qualifier(GLSLParser.Precision_qualifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#precision_qualifier}.
	 * @param ctx the parse tree
	 */
	void exitPrecision_qualifier(GLSLParser.Precision_qualifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#struct_specifier}.
	 * @param ctx the parse tree
	 */
	void enterStruct_specifier(GLSLParser.Struct_specifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#struct_specifier}.
	 * @param ctx the parse tree
	 */
	void exitStruct_specifier(GLSLParser.Struct_specifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#struct_declaration_list}.
	 * @param ctx the parse tree
	 */
	void enterStruct_declaration_list(GLSLParser.Struct_declaration_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#struct_declaration_list}.
	 * @param ctx the parse tree
	 */
	void exitStruct_declaration_list(GLSLParser.Struct_declaration_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#struct_declaration}.
	 * @param ctx the parse tree
	 */
	void enterStruct_declaration(GLSLParser.Struct_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#struct_declaration}.
	 * @param ctx the parse tree
	 */
	void exitStruct_declaration(GLSLParser.Struct_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#struct_declarator_list}.
	 * @param ctx the parse tree
	 */
	void enterStruct_declarator_list(GLSLParser.Struct_declarator_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#struct_declarator_list}.
	 * @param ctx the parse tree
	 */
	void exitStruct_declarator_list(GLSLParser.Struct_declarator_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#struct_declarator}.
	 * @param ctx the parse tree
	 */
	void enterStruct_declarator(GLSLParser.Struct_declaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#struct_declarator}.
	 * @param ctx the parse tree
	 */
	void exitStruct_declarator(GLSLParser.Struct_declaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#initializer}.
	 * @param ctx the parse tree
	 */
	void enterInitializer(GLSLParser.InitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#initializer}.
	 * @param ctx the parse tree
	 */
	void exitInitializer(GLSLParser.InitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#declaration_statement}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration_statement(GLSLParser.Declaration_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#declaration_statement}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration_statement(GLSLParser.Declaration_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#statement_no_new_scope}.
	 * @param ctx the parse tree
	 */
	void enterStatement_no_new_scope(GLSLParser.Statement_no_new_scopeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#statement_no_new_scope}.
	 * @param ctx the parse tree
	 */
	void exitStatement_no_new_scope(GLSLParser.Statement_no_new_scopeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#simple_statement}.
	 * @param ctx the parse tree
	 */
	void enterSimple_statement(GLSLParser.Simple_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#simple_statement}.
	 * @param ctx the parse tree
	 */
	void exitSimple_statement(GLSLParser.Simple_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#compound_statement_with_scope}.
	 * @param ctx the parse tree
	 */
	void enterCompound_statement_with_scope(GLSLParser.Compound_statement_with_scopeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#compound_statement_with_scope}.
	 * @param ctx the parse tree
	 */
	void exitCompound_statement_with_scope(GLSLParser.Compound_statement_with_scopeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#statement_with_scope}.
	 * @param ctx the parse tree
	 */
	void enterStatement_with_scope(GLSLParser.Statement_with_scopeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#statement_with_scope}.
	 * @param ctx the parse tree
	 */
	void exitStatement_with_scope(GLSLParser.Statement_with_scopeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#compound_statement_no_new_scope}.
	 * @param ctx the parse tree
	 */
	void enterCompound_statement_no_new_scope(GLSLParser.Compound_statement_no_new_scopeContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#compound_statement_no_new_scope}.
	 * @param ctx the parse tree
	 */
	void exitCompound_statement_no_new_scope(GLSLParser.Compound_statement_no_new_scopeContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#statement_list}.
	 * @param ctx the parse tree
	 */
	void enterStatement_list(GLSLParser.Statement_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#statement_list}.
	 * @param ctx the parse tree
	 */
	void exitStatement_list(GLSLParser.Statement_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#expression_statement}.
	 * @param ctx the parse tree
	 */
	void enterExpression_statement(GLSLParser.Expression_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#expression_statement}.
	 * @param ctx the parse tree
	 */
	void exitExpression_statement(GLSLParser.Expression_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#selection_statement}.
	 * @param ctx the parse tree
	 */
	void enterSelection_statement(GLSLParser.Selection_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#selection_statement}.
	 * @param ctx the parse tree
	 */
	void exitSelection_statement(GLSLParser.Selection_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(GLSLParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(GLSLParser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code whileIterationStatement}
	 * labeled alternative in {@link GLSLParser#iteration_statement}.
	 * @param ctx the parse tree
	 */
	void enterWhileIterationStatement(GLSLParser.WhileIterationStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code whileIterationStatement}
	 * labeled alternative in {@link GLSLParser#iteration_statement}.
	 * @param ctx the parse tree
	 */
	void exitWhileIterationStatement(GLSLParser.WhileIterationStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code doIterationStatement}
	 * labeled alternative in {@link GLSLParser#iteration_statement}.
	 * @param ctx the parse tree
	 */
	void enterDoIterationStatement(GLSLParser.DoIterationStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code doIterationStatement}
	 * labeled alternative in {@link GLSLParser#iteration_statement}.
	 * @param ctx the parse tree
	 */
	void exitDoIterationStatement(GLSLParser.DoIterationStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code forIterationStatement}
	 * labeled alternative in {@link GLSLParser#iteration_statement}.
	 * @param ctx the parse tree
	 */
	void enterForIterationStatement(GLSLParser.ForIterationStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code forIterationStatement}
	 * labeled alternative in {@link GLSLParser#iteration_statement}.
	 * @param ctx the parse tree
	 */
	void exitForIterationStatement(GLSLParser.ForIterationStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#for_init_statement}.
	 * @param ctx the parse tree
	 */
	void enterFor_init_statement(GLSLParser.For_init_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#for_init_statement}.
	 * @param ctx the parse tree
	 */
	void exitFor_init_statement(GLSLParser.For_init_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#for_rest_statement}.
	 * @param ctx the parse tree
	 */
	void enterFor_rest_statement(GLSLParser.For_rest_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#for_rest_statement}.
	 * @param ctx the parse tree
	 */
	void exitFor_rest_statement(GLSLParser.For_rest_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#jump_statement}.
	 * @param ctx the parse tree
	 */
	void enterJump_statement(GLSLParser.Jump_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#jump_statement}.
	 * @param ctx the parse tree
	 */
	void exitJump_statement(GLSLParser.Jump_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#external_declaration}.
	 * @param ctx the parse tree
	 */
	void enterExternal_declaration(GLSLParser.External_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#external_declaration}.
	 * @param ctx the parse tree
	 */
	void exitExternal_declaration(GLSLParser.External_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#function_definition}.
	 * @param ctx the parse tree
	 */
	void enterFunction_definition(GLSLParser.Function_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#function_definition}.
	 * @param ctx the parse tree
	 */
	void exitFunction_definition(GLSLParser.Function_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GLSLParser#field_selection}.
	 * @param ctx the parse tree
	 */
	void enterField_selection(GLSLParser.Field_selectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GLSLParser#field_selection}.
	 * @param ctx the parse tree
	 */
	void exitField_selection(GLSLParser.Field_selectionContext ctx);
}