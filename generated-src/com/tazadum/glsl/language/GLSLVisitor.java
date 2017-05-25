// Generated from GLSL.g4 by ANTLR 4.7
package com.tazadum.glsl.language;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link GLSLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface GLSLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link GLSLParser#translation_unit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTranslation_unit(GLSLParser.Translation_unitContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#variable_identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable_identifier(GLSLParser.Variable_identifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#primary_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary_expression(GLSLParser.Primary_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#postfix_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostfix_expression(GLSLParser.Postfix_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#primary_expression_or_function_call}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary_expression_or_function_call(GLSLParser.Primary_expression_or_function_callContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#integer_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInteger_expression(GLSLParser.Integer_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#function_call}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_call(GLSLParser.Function_callContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#function_call_header}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_call_header(GLSLParser.Function_call_headerContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#function_identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_identifier(GLSLParser.Function_identifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#constructor_identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructor_identifier(GLSLParser.Constructor_identifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#unary_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary_expression(GLSLParser.Unary_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#unary_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary_operator(GLSLParser.Unary_operatorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SubtractionExpression}
	 * labeled alternative in {@link GLSLParser#numeric_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubtractionExpression(GLSLParser.SubtractionExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AdditiveExpression}
	 * labeled alternative in {@link GLSLParser#numeric_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveExpression(GLSLParser.AdditiveExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryExpression}
	 * labeled alternative in {@link GLSLParser#numeric_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpression(GLSLParser.UnaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DivisionExpression}
	 * labeled alternative in {@link GLSLParser#numeric_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDivisionExpression(GLSLParser.DivisionExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MultiplicationExpression}
	 * labeled alternative in {@link GLSLParser#numeric_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicationExpression(GLSLParser.MultiplicationExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LogicalInEquality}
	 * labeled alternative in {@link GLSLParser#logical_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalInEquality(GLSLParser.LogicalInEqualityContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NumericDelegator}
	 * labeled alternative in {@link GLSLParser#logical_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumericDelegator(GLSLParser.NumericDelegatorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LogicalAnd}
	 * labeled alternative in {@link GLSLParser#logical_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalAnd(GLSLParser.LogicalAndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Relational}
	 * labeled alternative in {@link GLSLParser#logical_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelational(GLSLParser.RelationalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LogicalXor}
	 * labeled alternative in {@link GLSLParser#logical_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalXor(GLSLParser.LogicalXorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LogicalEquality}
	 * labeled alternative in {@link GLSLParser#logical_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalEquality(GLSLParser.LogicalEqualityContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LogicalOr}
	 * labeled alternative in {@link GLSLParser#logical_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalOr(GLSLParser.LogicalOrContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#conditional_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditional_expression(GLSLParser.Conditional_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#assignment_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_expression(GLSLParser.Assignment_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#assignment_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_operator(GLSLParser.Assignment_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(GLSLParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#constant_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant_expression(GLSLParser.Constant_expressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FunctionDeclaration}
	 * labeled alternative in {@link GLSLParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDeclaration(GLSLParser.FunctionDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VariableDeclaration}
	 * labeled alternative in {@link GLSLParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclaration(GLSLParser.VariableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrecisionDeclaration}
	 * labeled alternative in {@link GLSLParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrecisionDeclaration(GLSLParser.PrecisionDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#function_prototype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_prototype(GLSLParser.Function_prototypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#function_declarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_declarator(GLSLParser.Function_declaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#function_header}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_header(GLSLParser.Function_headerContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#parameter_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter_declaration(GLSLParser.Parameter_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#parameter_qualifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter_qualifier(GLSLParser.Parameter_qualifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#init_declarator_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInit_declarator_list(GLSLParser.Init_declarator_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#array_specifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_specifier(GLSLParser.Array_specifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#single_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingle_declaration(GLSLParser.Single_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#fully_specified_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFully_specified_type(GLSLParser.Fully_specified_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#type_qualifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_qualifier(GLSLParser.Type_qualifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#type_specifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_specifier(GLSLParser.Type_specifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#type_specifier_no_prec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_specifier_no_prec(GLSLParser.Type_specifier_no_precContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#precision_qualifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrecision_qualifier(GLSLParser.Precision_qualifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#struct_specifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStruct_specifier(GLSLParser.Struct_specifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#struct_declaration_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStruct_declaration_list(GLSLParser.Struct_declaration_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#struct_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStruct_declaration(GLSLParser.Struct_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#struct_declarator_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStruct_declarator_list(GLSLParser.Struct_declarator_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#struct_declarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStruct_declarator(GLSLParser.Struct_declaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#initializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitializer(GLSLParser.InitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#declaration_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration_statement(GLSLParser.Declaration_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#statement_no_new_scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement_no_new_scope(GLSLParser.Statement_no_new_scopeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#simple_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple_statement(GLSLParser.Simple_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#compound_statement_with_scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompound_statement_with_scope(GLSLParser.Compound_statement_with_scopeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#statement_with_scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement_with_scope(GLSLParser.Statement_with_scopeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#compound_statement_no_new_scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompound_statement_no_new_scope(GLSLParser.Compound_statement_no_new_scopeContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#statement_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement_list(GLSLParser.Statement_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#expression_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_statement(GLSLParser.Expression_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#selection_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelection_statement(GLSLParser.Selection_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondition(GLSLParser.ConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code whileIterationStatement}
	 * labeled alternative in {@link GLSLParser#iteration_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileIterationStatement(GLSLParser.WhileIterationStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code doIterationStatement}
	 * labeled alternative in {@link GLSLParser#iteration_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoIterationStatement(GLSLParser.DoIterationStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code forIterationStatement}
	 * labeled alternative in {@link GLSLParser#iteration_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForIterationStatement(GLSLParser.ForIterationStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#for_init_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_init_statement(GLSLParser.For_init_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#for_rest_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_rest_statement(GLSLParser.For_rest_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#jump_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJump_statement(GLSLParser.Jump_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#external_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExternal_declaration(GLSLParser.External_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#function_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_definition(GLSLParser.Function_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GLSLParser#field_selection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitField_selection(GLSLParser.Field_selectionContext ctx);
}