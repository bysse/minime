package com.tazadum.glsl.parser.listener;

import com.tazadum.glsl.language.GLSLListener;
import com.tazadum.glsl.language.GLSLParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * @author erikb
 * @since 2016-07-31
 */
public class PrintingListener implements GLSLListener {
    private GLSLListener delegate;
    private final boolean showEnter;
    private final boolean showExit;

    private String indentation = "";

    public static ParseTreeListener wrap(GLSLListener listener) {
        return new PrintingListener(listener, true, true);
    }

    public PrintingListener(GLSLListener delegate, boolean showEnter, boolean showExit) {
        this.delegate = delegate;
        this.showEnter = showEnter;
        this.showExit = showExit;
    }

    @Override
    public void enterTranslation_unit(GLSLParser.Translation_unitContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterTranslation_unit");
        }
        if (delegate != null) {
            delegate.enterTranslation_unit(ctx);
        }
    }

    @Override
    public void exitTranslation_unit(GLSLParser.Translation_unitContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitTranslation_unit");
        }
        if (delegate != null) {
            delegate.exitTranslation_unit(ctx);
        }
    }

    @Override
    public void enterVariable_identifier(GLSLParser.Variable_identifierContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterVariable_identifier");
        }
        if (delegate != null) {
            delegate.enterVariable_identifier(ctx);
        }
    }

    @Override
    public void exitVariable_identifier(GLSLParser.Variable_identifierContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ enterexitVariable_identifier");
        }
        if (delegate != null) {
            delegate.exitVariable_identifier(ctx);
        }
    }

    @Override
    public void enterPrimary_expression(GLSLParser.Primary_expressionContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterPrimary_expression");
        }
        if (delegate != null) {
            delegate.enterPrimary_expression(ctx);
        }
    }

    @Override
    public void exitPrimary_expression(GLSLParser.Primary_expressionContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitPrimary_expression");
        }
        if (delegate != null) {
            delegate.exitPrimary_expression(ctx);
        }
    }

    @Override
    public void enterPostfix_expression(GLSLParser.Postfix_expressionContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterPostfix_expression");
        }
        if (delegate != null) {
            delegate.enterPostfix_expression(ctx);
        }
    }

    @Override
    public void exitPostfix_expression(GLSLParser.Postfix_expressionContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitPostfix_expression");
        }
        if (delegate != null) {
            delegate.exitPostfix_expression(ctx);
        }
    }

    @Override
    public void enterPrimary_expression_or_function_call(GLSLParser.Primary_expression_or_function_callContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterPrimary_expression_or_function_call");
        }
        if (delegate != null) {
            delegate.enterPrimary_expression_or_function_call(ctx);
        }
    }

    @Override
    public void exitPrimary_expression_or_function_call(GLSLParser.Primary_expression_or_function_callContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitPrimary_expression_or_function_call");
        }
        if (delegate != null) {
            delegate.exitPrimary_expression_or_function_call(ctx);
        }
    }

    @Override
    public void enterInteger_expression(GLSLParser.Integer_expressionContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterPrimary_expression");
        }
        if (delegate != null) {
            delegate.enterInteger_expression(ctx);
        }
    }

    @Override
    public void exitInteger_expression(GLSLParser.Integer_expressionContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitInteger_expression");
        }
        if (delegate != null) {
            delegate.exitInteger_expression(ctx);
        }
    }

    @Override
    public void enterFunction_call(GLSLParser.Function_callContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterFunction_call");
        }
        if (delegate != null) {
            delegate.enterFunction_call(ctx);
        }
    }

    @Override
    public void exitFunction_call(GLSLParser.Function_callContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitFunction_call");
        }
        if (delegate != null) {
            delegate.exitFunction_call(ctx);
        }
    }

    @Override
    public void enterFunction_call_header(GLSLParser.Function_call_headerContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterFunction_call_header");
        }
        if (delegate != null) {
            delegate.enterFunction_call_header(ctx);
        }
    }

    @Override
    public void exitFunction_call_header(GLSLParser.Function_call_headerContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitFunction_call_header");
        }
        if (delegate != null) {
            delegate.exitFunction_call_header(ctx);
        }
    }

    @Override
    public void enterFunction_identifier(GLSLParser.Function_identifierContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterFunction_identifier");
        }
        if (delegate != null) {
            delegate.enterFunction_identifier(ctx);
        }
    }

    @Override
    public void exitFunction_identifier(GLSLParser.Function_identifierContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitFunction_identifier");
        }
        if (delegate != null) {
            delegate.exitFunction_identifier(ctx);
        }
    }

    @Override
    public void enterConstructor_identifier(GLSLParser.Constructor_identifierContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterConstructor_identifier");
        }
        if (delegate != null) {
            delegate.enterConstructor_identifier(ctx);
        }
    }

    @Override
    public void exitConstructor_identifier(GLSLParser.Constructor_identifierContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitConstructor_identifier");
        }
        if (delegate != null) {
            delegate.exitConstructor_identifier(ctx);
        }
    }

    @Override
    public void enterUnary_expression(GLSLParser.Unary_expressionContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterUnary_expression");
        }
        if (delegate != null) {
            delegate.enterUnary_expression(ctx);
        }
    }

    @Override
    public void exitUnary_expression(GLSLParser.Unary_expressionContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitUnary_expression");
        }
        if (delegate != null) {
            delegate.exitUnary_expression(ctx);
        }
    }

    @Override
    public void enterUnary_operator(GLSLParser.Unary_operatorContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterUnary_operator");
        }
        if (delegate != null) {
            delegate.enterUnary_operator(ctx);
        }
    }

    @Override
    public void exitUnary_operator(GLSLParser.Unary_operatorContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitUnary_operator");
        }
        if (delegate != null) {
            delegate.exitUnary_operator(ctx);
        }
    }

    @Override
    public void enterSubtractionExpression(GLSLParser.SubtractionExpressionContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterSubtractionExpression");
        }
        if (delegate != null) {
            delegate.enterSubtractionExpression(ctx);
        }
    }

    @Override
    public void exitSubtractionExpression(GLSLParser.SubtractionExpressionContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitSubtractionExpression");
        }
        if (delegate != null) {
            delegate.exitSubtractionExpression(ctx);
        }
    }

    @Override
    public void enterAdditiveExpression(GLSLParser.AdditiveExpressionContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterAdditiveExpression");
        }
        if (delegate != null) {
            delegate.enterAdditiveExpression(ctx);
        }
    }

    @Override
    public void exitAdditiveExpression(GLSLParser.AdditiveExpressionContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitAdditiveExpression");
        }
        if (delegate != null) {
            delegate.exitAdditiveExpression(ctx);
        }
    }

    @Override
    public void enterUnaryExpression(GLSLParser.UnaryExpressionContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterUnaryExpression");
        }
        if (delegate != null) {
            delegate.enterUnaryExpression(ctx);
        }
    }

    @Override
    public void exitUnaryExpression(GLSLParser.UnaryExpressionContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitUnaryExpression");
        }
        if (delegate != null) {
            delegate.exitUnaryExpression(ctx);
        }
    }

    @Override
    public void enterDivisionExpression(GLSLParser.DivisionExpressionContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterDivisionExpression");
        }
        if (delegate != null) {
            delegate.enterDivisionExpression(ctx);
        }
    }

    @Override
    public void exitDivisionExpression(GLSLParser.DivisionExpressionContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitDivisionExpression");
        }
        if (delegate != null) {
            delegate.exitDivisionExpression(ctx);
        }
    }

    @Override
    public void enterMultiplicationExpression(GLSLParser.MultiplicationExpressionContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterMultiplicationExpression");
        }
        if (delegate != null) {
            delegate.enterMultiplicationExpression(ctx);
        }
    }

    @Override
    public void exitMultiplicationExpression(GLSLParser.MultiplicationExpressionContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitMultiplicationExpression");
        }
        if (delegate != null) {
            delegate.exitMultiplicationExpression(ctx);
        }
    }

    @Override
    public void enterLogicalInEquality(GLSLParser.LogicalInEqualityContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterLogicalInEquality");
        }
        if (delegate != null) {
            delegate.enterLogicalInEquality(ctx);
        }
    }

    @Override
    public void exitLogicalInEquality(GLSLParser.LogicalInEqualityContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitLogicalInEquality");
        }
        if (delegate != null) {
            delegate.exitLogicalInEquality(ctx);
        }
    }

    @Override
    public void enterNumericDelegator(GLSLParser.NumericDelegatorContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterNumericDelegator");
        }
        if (delegate != null) {
            delegate.enterNumericDelegator(ctx);
        }
    }

    @Override
    public void exitNumericDelegator(GLSLParser.NumericDelegatorContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitNumericDelegator");
        }
        if (delegate != null) {
            delegate.exitNumericDelegator(ctx);
        }
    }

    @Override
    public void enterLogicalAnd(GLSLParser.LogicalAndContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterLogicalAnd");
        }
        if (delegate != null) {
            delegate.enterLogicalAnd(ctx);
        }
    }

    @Override
    public void exitLogicalAnd(GLSLParser.LogicalAndContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitLogicalAnd");
        }
        if (delegate != null) {
            delegate.exitLogicalAnd(ctx);
        }
    }

    @Override
    public void enterRelational(GLSLParser.RelationalContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterRelational");
        }
        if (delegate != null) {
            delegate.enterRelational(ctx);
        }
    }

    @Override
    public void exitRelational(GLSLParser.RelationalContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitRelational");
        }
        if (delegate != null) {
            delegate.exitRelational(ctx);
        }
    }

    @Override
    public void enterLogicalXor(GLSLParser.LogicalXorContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterLogicalXor");
        }
        if (delegate != null) {
            delegate.enterLogicalXor(ctx);
        }
    }

    @Override
    public void exitLogicalXor(GLSLParser.LogicalXorContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitLogicalXor");
        }
        if (delegate != null) {
            delegate.exitLogicalXor(ctx);
        }
    }

    @Override
    public void enterLogicalEquality(GLSLParser.LogicalEqualityContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterLogicalEquality");
        }
        if (delegate != null) {
            delegate.enterLogicalEquality(ctx);
        }
    }

    @Override
    public void exitLogicalEquality(GLSLParser.LogicalEqualityContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitLogicalEquality");
        }
        if (delegate != null) {
            delegate.exitLogicalEquality(ctx);
        }
    }

    @Override
    public void enterLogicalOr(GLSLParser.LogicalOrContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterLogicalOr");
        }
        if (delegate != null) {
            delegate.enterLogicalOr(ctx);
        }
    }

    @Override
    public void exitLogicalOr(GLSLParser.LogicalOrContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitLogicalOr");
        }
        if (delegate != null) {
            delegate.exitLogicalOr(ctx);
        }
    }

    @Override
    public void enterConditional_expression(GLSLParser.Conditional_expressionContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterConditional_expression");
        }
        if (delegate != null) {
            delegate.enterConditional_expression(ctx);
        }
    }

    @Override
    public void exitConditional_expression(GLSLParser.Conditional_expressionContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitConditional_expression");
        }
        if (delegate != null) {
            delegate.exitConditional_expression(ctx);
        }
    }

    @Override
    public void enterAssignment_expression(GLSLParser.Assignment_expressionContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterAssignment_expression");
        }
        if (delegate != null) {
            delegate.enterAssignment_expression(ctx);
        }
    }

    @Override
    public void exitAssignment_expression(GLSLParser.Assignment_expressionContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitAssignment_expression");
        }
        if (delegate != null) {
            delegate.exitAssignment_expression(ctx);
        }
    }

    @Override
    public void enterAssignment_operator(GLSLParser.Assignment_operatorContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterAssignment_operator");
        }
        if (delegate != null) {
            delegate.enterAssignment_operator(ctx);
        }
    }

    @Override
    public void exitAssignment_operator(GLSLParser.Assignment_operatorContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitAssignment_operator");
        }
        if (delegate != null) {
            delegate.exitAssignment_operator(ctx);
        }
    }

    @Override
    public void enterExpression(GLSLParser.ExpressionContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterExpression");
        }
        if (delegate != null) {
            delegate.enterExpression(ctx);
        }
    }

    @Override
    public void exitExpression(GLSLParser.ExpressionContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitExpression");
        }
        if (delegate != null) {
            delegate.exitExpression(ctx);
        }
    }

    @Override
    public void enterConstant_expression(GLSLParser.Constant_expressionContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterConstant_expression");
        }
        if (delegate != null) {
            delegate.enterConstant_expression(ctx);
        }
    }

    @Override
    public void exitConstant_expression(GLSLParser.Constant_expressionContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitConstant_expression");
        }
        if (delegate != null) {
            delegate.exitConstant_expression(ctx);
        }
    }

    @Override
    public void enterFunctionDeclaration(GLSLParser.FunctionDeclarationContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterFunctionDeclaration");
        }
        if (delegate != null) {
            delegate.enterFunctionDeclaration(ctx);
        }
    }

    @Override
    public void exitFunctionDeclaration(GLSLParser.FunctionDeclarationContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitFunctionDeclaration");
        }
        if (delegate != null) {
            delegate.exitFunctionDeclaration(ctx);
        }
    }

    @Override
    public void enterVariableDeclaration(GLSLParser.VariableDeclarationContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterVariableDeclaration");
        }
        if (delegate != null) {
            delegate.enterVariableDeclaration(ctx);
        }
    }

    @Override
    public void exitVariableDeclaration(GLSLParser.VariableDeclarationContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitVariableDeclaration");
        }
        if (delegate != null) {
            delegate.exitVariableDeclaration(ctx);
        }
    }

    @Override
    public void enterPrecisionDeclaration(GLSLParser.PrecisionDeclarationContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterPrecisionDeclaration");
        }
        if (delegate != null) {
            delegate.enterPrecisionDeclaration(ctx);
        }
    }

    @Override
    public void exitPrecisionDeclaration(GLSLParser.PrecisionDeclarationContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitPrecisionDeclaration");
        }
        if (delegate != null) {
            delegate.exitPrecisionDeclaration(ctx);
        }
    }

    @Override
    public void enterFunction_prototype(GLSLParser.Function_prototypeContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterFunction_prototype");
        }
        if (delegate != null) {
            delegate.enterFunction_prototype(ctx);
        }
    }

    @Override
    public void exitFunction_prototype(GLSLParser.Function_prototypeContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitFunction_prototype");
        }
        if (delegate != null) {
            delegate.exitFunction_prototype(ctx);
        }
    }

    @Override
    public void enterFunction_declarator(GLSLParser.Function_declaratorContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterFunction_declarator");
        }
        if (delegate != null) {
            delegate.enterFunction_declarator(ctx);
        }
    }

    @Override
    public void exitFunction_declarator(GLSLParser.Function_declaratorContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitFunction_declarator");
        }
        if (delegate != null) {
            delegate.exitFunction_declarator(ctx);
        }
    }

    @Override
    public void enterFunction_header(GLSLParser.Function_headerContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterFunction_header");
        }
        if (delegate != null) {
            delegate.enterFunction_header(ctx);
        }
    }

    @Override
    public void exitFunction_header(GLSLParser.Function_headerContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitFunction_header");
        }
        if (delegate != null) {
            delegate.exitFunction_header(ctx);
        }
    }

    @Override
    public void enterParameter_declaration(GLSLParser.Parameter_declarationContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterParameter_declaration");
        }
        if (delegate != null) {
            delegate.enterParameter_declaration(ctx);
        }
    }

    @Override
    public void exitParameter_declaration(GLSLParser.Parameter_declarationContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitParameter_declaration");
        }
        if (delegate != null) {
            delegate.exitParameter_declaration(ctx);
        }
    }

    @Override
    public void enterParameter_qualifier(GLSLParser.Parameter_qualifierContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterParameter_qualifier");
        }
        if (delegate != null) {
            delegate.enterParameter_qualifier(ctx);
        }
    }

    @Override
    public void exitParameter_qualifier(GLSLParser.Parameter_qualifierContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitParameter_qualifier");
        }
        if (delegate != null) {
            delegate.exitParameter_qualifier(ctx);
        }
    }

    @Override
    public void enterInit_declarator_list(GLSLParser.Init_declarator_listContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterInit_declarator_list");
        }
        if (delegate != null) {
            delegate.enterInit_declarator_list(ctx);
        }
    }

    @Override
    public void exitInit_declarator_list(GLSLParser.Init_declarator_listContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitInit_declarator_list");
        }
        if (delegate != null) {
            delegate.exitInit_declarator_list(ctx);
        }
    }

    @Override
    public void enterArray_specifier(GLSLParser.Array_specifierContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterArray_specifier");
        }
        if (delegate != null) {
            delegate.enterArray_specifier(ctx);
        }
    }

    @Override
    public void exitArray_specifier(GLSLParser.Array_specifierContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitArray_specifier");
        }
        if (delegate != null) {
            delegate.exitArray_specifier(ctx);
        }
    }

    @Override
    public void enterSingle_declaration(GLSLParser.Single_declarationContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterSingle_declaration");
        }
        if (delegate != null) {
            delegate.enterSingle_declaration(ctx);
        }
    }

    @Override
    public void exitSingle_declaration(GLSLParser.Single_declarationContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitSingle_declaration");
        }
        if (delegate != null) {
            delegate.exitSingle_declaration(ctx);
        }
    }

    @Override
    public void enterFully_specified_type(GLSLParser.Fully_specified_typeContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterFully_specified_type");
        }
        if (delegate != null) {
            delegate.enterFully_specified_type(ctx);
        }
    }

    @Override
    public void exitFully_specified_type(GLSLParser.Fully_specified_typeContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitFully_specified_type");
        }
        if (delegate != null) {
            delegate.exitFully_specified_type(ctx);
        }
    }

    @Override
    public void enterType_qualifier(GLSLParser.Type_qualifierContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterType_qualifier");
        }
        if (delegate != null) {
            delegate.enterType_qualifier(ctx);
        }
    }

    @Override
    public void exitType_qualifier(GLSLParser.Type_qualifierContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitType_qualifier");
        }
        if (delegate != null) {
            delegate.exitType_qualifier(ctx);
        }
    }

    @Override
    public void enterType_specifier(GLSLParser.Type_specifierContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterType_specifier");
        }
        if (delegate != null) {
            delegate.enterType_specifier(ctx);
        }
    }

    @Override
    public void exitType_specifier(GLSLParser.Type_specifierContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitType_specifier");
        }
        if (delegate != null) {
            delegate.exitType_specifier(ctx);
        }
    }

    @Override
    public void enterType_specifier_no_prec(GLSLParser.Type_specifier_no_precContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterType_specifier_no_prec");
        }
        if (delegate != null) {
            delegate.enterType_specifier_no_prec(ctx);
        }
    }

    @Override
    public void exitType_specifier_no_prec(GLSLParser.Type_specifier_no_precContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitType_specifier_no_prec");
        }
        if (delegate != null) {
            delegate.exitType_specifier_no_prec(ctx);
        }
    }

    @Override
    public void enterPrecision_qualifier(GLSLParser.Precision_qualifierContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterPrecision_qualifier");
        }
        if (delegate != null) {
            delegate.enterPrecision_qualifier(ctx);
        }
    }

    @Override
    public void exitPrecision_qualifier(GLSLParser.Precision_qualifierContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitPrecision_qualifier");
        }
        if (delegate != null) {
            delegate.exitPrecision_qualifier(ctx);
        }
    }

    @Override
    public void enterStruct_specifier(GLSLParser.Struct_specifierContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterStruct_specifier");
        }
        if (delegate != null) {
            delegate.enterStruct_specifier(ctx);
        }
    }

    @Override
    public void exitStruct_specifier(GLSLParser.Struct_specifierContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitStruct_specifier");
        }
        if (delegate != null) {
            delegate.exitStruct_specifier(ctx);
        }
    }

    @Override
    public void enterStruct_declaration_list(GLSLParser.Struct_declaration_listContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterStruct_declaration_list");
        }
        if (delegate != null) {
            delegate.enterStruct_declaration_list(ctx);
        }
    }

    @Override
    public void exitStruct_declaration_list(GLSLParser.Struct_declaration_listContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitStruct_declaration_list");
        }
        if (delegate != null) {
            delegate.exitStruct_declaration_list(ctx);
        }
    }

    @Override
    public void enterStruct_declaration(GLSLParser.Struct_declarationContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterStruct_declaration");
        }
        if (delegate != null) {
            delegate.enterStruct_declaration(ctx);
        }
    }

    @Override
    public void exitStruct_declaration(GLSLParser.Struct_declarationContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitStruct_declaration");
        }
        if (delegate != null) {
            delegate.exitStruct_declaration(ctx);
        }
    }

    @Override
    public void enterStruct_declarator_list(GLSLParser.Struct_declarator_listContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterStruct_declarator_list");
        }
        if (delegate != null) {
            delegate.enterStruct_declarator_list(ctx);
        }
    }

    @Override
    public void exitStruct_declarator_list(GLSLParser.Struct_declarator_listContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitStruct_declarator_list");
        }
        if (delegate != null) {
            delegate.exitStruct_declarator_list(ctx);
        }
    }

    @Override
    public void enterStruct_declarator(GLSLParser.Struct_declaratorContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterStruct_declarator");
        }
        if (delegate != null) {
            delegate.enterStruct_declarator(ctx);
        }
    }

    @Override
    public void exitStruct_declarator(GLSLParser.Struct_declaratorContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitStruct_declarator");
        }
        if (delegate != null) {
            delegate.exitStruct_declarator(ctx);
        }
    }

    @Override
    public void enterInitializer(GLSLParser.InitializerContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterInitializer");
        }
        if (delegate != null) {
            delegate.enterInitializer(ctx);
        }
    }

    @Override
    public void exitInitializer(GLSLParser.InitializerContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitInitializer");
        }
        if (delegate != null) {
            delegate.exitInitializer(ctx);
        }
    }

    @Override
    public void enterDeclaration_statement(GLSLParser.Declaration_statementContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterDeclaration_statement");
        }
        if (delegate != null) {
            delegate.enterDeclaration_statement(ctx);
        }
    }

    @Override
    public void exitDeclaration_statement(GLSLParser.Declaration_statementContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitDeclaration_statement");
        }
        if (delegate != null) {
            delegate.exitDeclaration_statement(ctx);
        }
    }

    @Override
    public void enterStatement_no_new_scope(GLSLParser.Statement_no_new_scopeContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterStatement_no_new_scope");
        }
        if (delegate != null) {
            delegate.enterStatement_no_new_scope(ctx);
        }
    }

    @Override
    public void exitStatement_no_new_scope(GLSLParser.Statement_no_new_scopeContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitStatement_no_new_scope");
        }
        if (delegate != null) {
            delegate.exitStatement_no_new_scope(ctx);
        }
    }

    @Override
    public void enterSimple_statement(GLSLParser.Simple_statementContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterSimple_statement");
        }
        if (delegate != null) {
            delegate.enterSimple_statement(ctx);
        }
    }

    @Override
    public void exitSimple_statement(GLSLParser.Simple_statementContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitSimple_statement");
        }
        if (delegate != null) {
            delegate.exitSimple_statement(ctx);
        }
    }

    @Override
    public void enterCompound_statement_with_scope(GLSLParser.Compound_statement_with_scopeContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterCompound_statement_with_scope");
        }
        if (delegate != null) {
            delegate.enterCompound_statement_with_scope(ctx);
        }
    }

    @Override
    public void exitCompound_statement_with_scope(GLSLParser.Compound_statement_with_scopeContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitCompound_statement_with_scope");
        }
        if (delegate != null) {
            delegate.exitCompound_statement_with_scope(ctx);
        }
    }

    @Override
    public void enterStatement_with_scope(GLSLParser.Statement_with_scopeContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterStatement_with_scope");
        }
        if (delegate != null) {
            delegate.enterStatement_with_scope(ctx);
        }
    }

    @Override
    public void exitStatement_with_scope(GLSLParser.Statement_with_scopeContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitStatement_with_scope");
        }
        if (delegate != null) {
            delegate.exitStatement_with_scope(ctx);
        }
    }

    @Override
    public void enterCompound_statement_no_new_scope(GLSLParser.Compound_statement_no_new_scopeContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterCompound_statement_no_new_scope");
        }
        if (showEnter) {
            System.out.println(enter() + "+ enterCompound_statement_no_new_scope");
        }
        if (delegate != null) {
            delegate.enterCompound_statement_no_new_scope(ctx);
        }
    }

    @Override
    public void exitCompound_statement_no_new_scope(GLSLParser.Compound_statement_no_new_scopeContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitCompound_statement_no_new_scope");
        }
        if (delegate != null) {
            delegate.exitCompound_statement_no_new_scope(ctx);
        }
    }

    @Override
    public void enterStatement_list(GLSLParser.Statement_listContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterStatement_list");
        }
        if (delegate != null) {
            delegate.enterStatement_list(ctx);
        }
    }

    @Override
    public void exitStatement_list(GLSLParser.Statement_listContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitStatement_list");
        }
        if (delegate != null) {
            delegate.exitStatement_list(ctx);
        }
    }

    @Override
    public void enterExpression_statement(GLSLParser.Expression_statementContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterExpression_statement");
        }
        if (delegate != null) {
            delegate.enterExpression_statement(ctx);
        }
    }

    @Override
    public void exitExpression_statement(GLSLParser.Expression_statementContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitExpression_statement");
        }
        if (delegate != null) {
            delegate.exitExpression_statement(ctx);
        }
    }

    @Override
    public void enterSelection_statement(GLSLParser.Selection_statementContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterSelection_statement");
        }
        if (delegate != null) {
            delegate.enterSelection_statement(ctx);
        }
    }

    @Override
    public void exitSelection_statement(GLSLParser.Selection_statementContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitSelection_statement");
        }
        if (delegate != null) {
            delegate.exitSelection_statement(ctx);
        }
    }

    @Override
    public void enterCondition(GLSLParser.ConditionContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterCondition");
        }
        if (delegate != null) {
            delegate.enterCondition(ctx);
        }
    }

    @Override
    public void exitCondition(GLSLParser.ConditionContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitCondition");
        }
        if (delegate != null) {
            delegate.exitCondition(ctx);
        }
    }

    @Override
    public void enterWhileIterationStatement(GLSLParser.WhileIterationStatementContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterWhileIterationStatement");
        }
        if (delegate != null) {
            delegate.enterWhileIterationStatement(ctx);
        }
    }

    @Override
    public void exitWhileIterationStatement(GLSLParser.WhileIterationStatementContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitWhileIterationStatement");
        }
        if (delegate != null) {
            delegate.exitWhileIterationStatement(ctx);
        }
    }

    @Override
    public void enterDoIterationStatement(GLSLParser.DoIterationStatementContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterDoIterationStatement");
        }
        if (delegate != null) {
            delegate.enterDoIterationStatement(ctx);
        }
    }

    @Override
    public void exitDoIterationStatement(GLSLParser.DoIterationStatementContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitDoIterationStatement");
        }
        if (delegate != null) {
            delegate.exitDoIterationStatement(ctx);
        }
    }

    @Override
    public void enterForIterationStatement(GLSLParser.ForIterationStatementContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterForIterationStatement");
        }
        if (delegate != null) {
            delegate.enterForIterationStatement(ctx);
        }
    }

    @Override
    public void exitForIterationStatement(GLSLParser.ForIterationStatementContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitForIterationStatement");
        }
        if (delegate != null) {
            delegate.exitForIterationStatement(ctx);
        }
    }

    @Override
    public void enterFor_init_statement(GLSLParser.For_init_statementContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterFor_init_statement");
        }
        if (delegate != null) {
            delegate.enterFor_init_statement(ctx);
        }
    }

    @Override
    public void exitFor_init_statement(GLSLParser.For_init_statementContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitFor_init_statement");
        }
        if (delegate != null) {
            delegate.exitFor_init_statement(ctx);
        }
    }

    @Override
    public void enterFor_rest_statement(GLSLParser.For_rest_statementContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterFor_rest_statement");
        }
        if (delegate != null) {
            delegate.enterFor_rest_statement(ctx);
        }
    }

    @Override
    public void exitFor_rest_statement(GLSLParser.For_rest_statementContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitFor_rest_statement");
        }
        if (delegate != null) {
            delegate.exitFor_rest_statement(ctx);
        }
    }

    @Override
    public void enterJump_statement(GLSLParser.Jump_statementContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterJump_statement");
        }
        if (delegate != null) {
            delegate.enterJump_statement(ctx);
        }
    }

    @Override
    public void exitJump_statement(GLSLParser.Jump_statementContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitJump_statement");
        }
        if (delegate != null) {
            delegate.exitJump_statement(ctx);
        }
    }

    @Override
    public void enterExternal_declaration(GLSLParser.External_declarationContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterExternal_declaration");
        }
        if (delegate != null) {
            delegate.enterExternal_declaration(ctx);
        }
    }

    @Override
    public void exitExternal_declaration(GLSLParser.External_declarationContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitExternal_declaration");
        }
        if (delegate != null) {
            delegate.exitExternal_declaration(ctx);
        }
    }

    @Override
    public void enterFunction_definition(GLSLParser.Function_definitionContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterFunction_definition");
        }
        if (delegate != null) {
            delegate.enterFunction_definition(ctx);
        }
    }

    @Override
    public void exitFunction_definition(GLSLParser.Function_definitionContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitFunction_definition");
        }
        if (delegate != null) {
            delegate.exitFunction_definition(ctx);
        }
    }

    @Override
    public void enterField_selection(GLSLParser.Field_selectionContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterField_selection");
        }
        if (delegate != null) {
            delegate.enterField_selection(ctx);
        }
    }

    @Override
    public void exitField_selection(GLSLParser.Field_selectionContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitField_selection");
        }
        if (delegate != null) {
            delegate.exitField_selection(ctx);
        }
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        if (delegate != null) {
            delegate.visitTerminal(node);
        }
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        if (delegate != null) {
            delegate.visitErrorNode(node);
        }
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        if (delegate != null) {
            delegate.enterEveryRule(ctx);
        }
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        if (delegate != null) {
            delegate.exitEveryRule(ctx);
        }
    }


    private String enter() {
        final String space = indentation;
        indentation += "  ";
        return space;
    }

    private String exit() {
        indentation = indentation.substring(0, indentation.length() - 2);
        return indentation;
    }
}
