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

    public void ENTER_S(GLSLParser.Struct_specifierContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterStruct_specifier");
        }
        if (delegate != null) {
            delegate.enterStruct_specifier(ctx);
        }
    }

    public void EXIT_S(GLSLParser.Struct_specifierContext ctx) {
        if (showExit) {
            System.out.println(exit() + "+ exitStruct_specifier");
        }
        if (delegate != null) {
            delegate.exitStruct_specifier(ctx);
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

    @Override
    public void enterTranslation_unit(GLSLParser.Translation_unitContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterTranslation_unit");
        }
        if (delegate != null) {
            delegate.enterTranslation_unit(ctx);
        }
        if (showEnter) {
            System.out.println(enter() + "+ enterTranslation_unit");
        }
        if (delegate != null) {
            delegate.enterTranslation_unit(ctx);
        }

    }

    @Override
    public void exitTranslation_unit(GLSLParser.Translation_unitContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitTranslation_unit");
        }
        if (delegate != null) {
            delegate.exitTranslation_unit(ctx);
        }

    }

    @Override
    public void enterExternal_declaration(GLSLParser.External_declarationContext ctx) {

    }

    @Override
    public void exitExternal_declaration(GLSLParser.External_declarationContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitExternal_declaration");
        }
        if (delegate != null) {
            delegate.exitExternal_declaration(ctx);
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
        if (showEnter) {
            System.out.println(enter() + "+ enterVariable_identifier");
        }
        if (delegate != null) {
            delegate.enterVariable_identifier(ctx);
        }

    }

    @Override
    public void exitVariable_identifier(GLSLParser.Variable_identifierContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitVariable_identifier");
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
        if (showEnter) {
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
        if (showEnter) {
            System.out.println(exit() + "+ exitPostfix_expression");
        }
        if (delegate != null) {
            delegate.exitPostfix_expression(ctx);
        }

    }

    @Override
    public void enterField_selection(GLSLParser.Field_selectionContext ctx) {

    }

    @Override
    public void exitField_selection(GLSLParser.Field_selectionContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitField_selection");
        }
        if (delegate != null) {
            delegate.exitField_selection(ctx);
        }

    }

    @Override
    public void enterInteger_expression(GLSLParser.Integer_expressionContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterInteger_expression");
        }
        if (delegate != null) {
            delegate.enterInteger_expression(ctx);
        }

    }

    @Override
    public void exitInteger_expression(GLSLParser.Integer_expressionContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitInteger_expression");
        }
        if (delegate != null) {
            delegate.exitInteger_expression(ctx);
        }

    }

    @Override
    public void enterFunction_call(GLSLParser.Function_callContext ctx) {

    }

    @Override
    public void exitFunction_call(GLSLParser.Function_callContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitFunction_call");
        }
        if (delegate != null) {
            delegate.exitFunction_call(ctx);
        }

    }

    @Override
    public void enterFunction_call_header(GLSLParser.Function_call_headerContext ctx) {

    }

    @Override
    public void exitFunction_call_header(GLSLParser.Function_call_headerContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitFunction_call_header");
        }
        if (delegate != null) {
            delegate.exitFunction_call_header(ctx);
        }

    }

    @Override
    public void enterFunction_identifier(GLSLParser.Function_identifierContext ctx) {

    }

    @Override
    public void exitFunction_identifier(GLSLParser.Function_identifierContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitFunction_identifier");
        }
        if (delegate != null) {
            delegate.exitFunction_identifier(ctx);
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
        if (showEnter) {
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
        if (showEnter) {
            System.out.println(exit() + "+ exitUnary_operator");
        }
        if (delegate != null) {
            delegate.exitUnary_operator(ctx);
        }

    }

    @Override
    public void enterAdditive_expression(GLSLParser.Additive_expressionContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterAdditive_expression");
        }
        if (delegate != null) {
            delegate.enterAdditive_expression(ctx);
        }

    }

    @Override
    public void exitAdditive_expression(GLSLParser.Additive_expressionContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitAdditive_expression");
        }
        if (delegate != null) {
            delegate.exitAdditive_expression(ctx);
        }

    }

    @Override
    public void enterShift_expression(GLSLParser.Shift_expressionContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterShift_expression");
        }
        if (delegate != null) {
            delegate.enterShift_expression(ctx);
        }

    }

    @Override
    public void exitShift_expression(GLSLParser.Shift_expressionContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitShift_expression");
        }
        if (delegate != null) {
            delegate.exitShift_expression(ctx);
        }

    }

    @Override
    public void enterUnary_expression_delegate(GLSLParser.Unary_expression_delegateContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterUnary_expression_delegate");
        }
        if (delegate != null) {
            delegate.enterUnary_expression_delegate(ctx);
        }

    }

    @Override
    public void exitUnary_expression_delegate(GLSLParser.Unary_expression_delegateContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitUnary_expression_delegate");
        }
        if (delegate != null) {
            delegate.exitUnary_expression_delegate(ctx);
        }

    }

    @Override
    public void enterMultiplicative_expression(GLSLParser.Multiplicative_expressionContext ctx) {

    }

    @Override
    public void exitMultiplicative_expression(GLSLParser.Multiplicative_expressionContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitMultiplicative_expression");
        }
        if (delegate != null) {
            delegate.exitMultiplicative_expression(ctx);
        }

    }

    @Override
    public void enterNumeric_expression_delegate(GLSLParser.Numeric_expression_delegateContext ctx) {

    }

    @Override
    public void exitNumeric_expression_delegate(GLSLParser.Numeric_expression_delegateContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitNumeric_expression_delegate");
        }
        if (delegate != null) {
            delegate.exitNumeric_expression_delegate(ctx);
        }

    }

    @Override
    public void enterEquality_expression(GLSLParser.Equality_expressionContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterEquality_expression");
        }
        if (delegate != null) {
            delegate.enterEquality_expression(ctx);
        }

    }

    @Override
    public void exitEquality_expression(GLSLParser.Equality_expressionContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitEquality_expression");
        }
        if (delegate != null) {
            delegate.exitEquality_expression(ctx);
        }

    }

    @Override
    public void enterBit_op_expression(GLSLParser.Bit_op_expressionContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterBit_op_expression");
        }
        if (delegate != null) {
            delegate.enterBit_op_expression(ctx);
        }

    }

    @Override
    public void exitBit_op_expression(GLSLParser.Bit_op_expressionContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitBit_op_expression");
        }
        if (delegate != null) {
            delegate.exitBit_op_expression(ctx);
        }

    }

    @Override
    public void enterLogical_xor_expression(GLSLParser.Logical_xor_expressionContext ctx) {

    }

    @Override
    public void exitLogical_xor_expression(GLSLParser.Logical_xor_expressionContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitLogical_xor_expression");
        }
        if (delegate != null) {
            delegate.exitLogical_xor_expression(ctx);
        }

    }

    @Override
    public void enterLogical_and_expression(GLSLParser.Logical_and_expressionContext ctx) {

    }

    @Override
    public void exitLogical_and_expression(GLSLParser.Logical_and_expressionContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitLogical_and_expression");
        }
        if (delegate != null) {
            delegate.exitLogical_and_expression(ctx);
        }

    }

    @Override
    public void enterRelational_expression(GLSLParser.Relational_expressionContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterRelational_expression");
        }
        if (delegate != null) {
            delegate.enterRelational_expression(ctx);
        }

    }

    @Override
    public void exitRelational_expression(GLSLParser.Relational_expressionContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitRelational_expression");
        }
        if (delegate != null) {
            delegate.exitRelational_expression(ctx);
        }

    }

    @Override
    public void enterLogical_or_expression(GLSLParser.Logical_or_expressionContext ctx) {

    }

    @Override
    public void exitLogical_or_expression(GLSLParser.Logical_or_expressionContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitLogical_or_expression");
        }
        if (delegate != null) {
            delegate.exitLogical_or_expression(ctx);
        }

    }

    @Override
    public void enterConditional_expression(GLSLParser.Conditional_expressionContext ctx) {

    }

    @Override
    public void exitConditional_expression(GLSLParser.Conditional_expressionContext ctx) {
        if (showEnter) {
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
        if (showEnter) {
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
        if (showEnter) {
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
        if (showEnter) {
            System.out.println(exit() + "+ exitExpression");
        }
        if (delegate != null) {
            delegate.exitExpression(ctx);
        }

    }

    @Override
    public void enterConstant_expression(GLSLParser.Constant_expressionContext ctx) {

    }

    @Override
    public void exitConstant_expression(GLSLParser.Constant_expressionContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitConstant_expression");
        }
        if (delegate != null) {
            delegate.exitConstant_expression(ctx);
        }

    }

    @Override
    public void enterFunction_declaration(GLSLParser.Function_declarationContext ctx) {

    }

    @Override
    public void exitFunction_declaration(GLSLParser.Function_declarationContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitFunction_declaration");
        }
        if (delegate != null) {
            delegate.exitFunction_declaration(ctx);
        }

    }

    @Override
    public void enterVariable_declaration(GLSLParser.Variable_declarationContext ctx) {

    }

    @Override
    public void exitVariable_declaration(GLSLParser.Variable_declarationContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitVariable_declaration");
        }
        if (delegate != null) {
            delegate.exitVariable_declaration(ctx);
        }

    }

    @Override
    public void enterPrecision_declaration(GLSLParser.Precision_declarationContext ctx) {

    }

    @Override
    public void exitPrecision_declaration(GLSLParser.Precision_declarationContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitPrecision_declaration");
        }
        if (delegate != null) {
            delegate.exitPrecision_declaration(ctx);
        }

    }

    @Override
    public void enterStruct_init_declaration(GLSLParser.Struct_init_declarationContext ctx) {

    }

    @Override
    public void exitStruct_init_declaration(GLSLParser.Struct_init_declarationContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitStruct_init_declaration");
        }
        if (delegate != null) {
            delegate.exitStruct_init_declaration(ctx);
        }

    }

    @Override
    public void enterQualifier_declaration(GLSLParser.Qualifier_declarationContext ctx) {

    }

    @Override
    public void exitQualifier_declaration(GLSLParser.Qualifier_declarationContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitQualifier_declaration");
        }
        if (delegate != null) {
            delegate.exitQualifier_declaration(ctx);
        }

    }

    @Override
    public void enterFunction_definition(GLSLParser.Function_definitionContext ctx) {

    }

    @Override
    public void exitFunction_definition(GLSLParser.Function_definitionContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitFunction_definition");
        }
        if (delegate != null) {
            delegate.exitFunction_definition(ctx);
        }

    }

    @Override
    public void enterFunction_prototype(GLSLParser.Function_prototypeContext ctx) {

    }

    @Override
    public void exitFunction_prototype(GLSLParser.Function_prototypeContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitFunction_prototype");
        }
        if (delegate != null) {
            delegate.exitFunction_prototype(ctx);
        }

    }

    @Override
    public void enterFunction_declarator(GLSLParser.Function_declaratorContext ctx) {

    }

    @Override
    public void exitFunction_declarator(GLSLParser.Function_declaratorContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitFunction_declarator");
        }
        if (delegate != null) {
            delegate.exitFunction_declarator(ctx);
        }

    }

    @Override
    public void enterFunction_header(GLSLParser.Function_headerContext ctx) {

    }

    @Override
    public void exitFunction_header(GLSLParser.Function_headerContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitFunction_header");
        }
        if (delegate != null) {
            delegate.exitFunction_header(ctx);
        }

    }

    @Override
    public void enterParameter_declaration(GLSLParser.Parameter_declarationContext ctx) {

    }

    @Override
    public void exitParameter_declaration(GLSLParser.Parameter_declarationContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitParameter_declaration");
        }
        if (delegate != null) {
            delegate.exitParameter_declaration(ctx);
        }

    }

    @Override
    public void enterInit_declarator_list(GLSLParser.Init_declarator_listContext ctx) {

    }

    @Override
    public void exitInit_declarator_list(GLSLParser.Init_declarator_listContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitInit_declarator_list");
        }
        if (delegate != null) {
            delegate.exitInit_declarator_list(ctx);
        }

    }

    @Override
    public void enterSingle_declaration(GLSLParser.Single_declarationContext ctx) {

    }

    @Override
    public void exitSingle_declaration(GLSLParser.Single_declarationContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitSingle_declaration");
        }
        if (delegate != null) {
            delegate.exitSingle_declaration(ctx);
        }

    }

    @Override
    public void enterFully_specified_type(GLSLParser.Fully_specified_typeContext ctx) {

    }

    @Override
    public void exitFully_specified_type(GLSLParser.Fully_specified_typeContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitFully_specified_type");
        }
        if (delegate != null) {
            delegate.exitFully_specified_type(ctx);
        }

    }

    @Override
    public void enterStorage_qualifier(GLSLParser.Storage_qualifierContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterStorage_qualifier");
        }
        if (delegate != null) {
            delegate.enterStorage_qualifier(ctx);
        }

    }

    @Override
    public void exitStorage_qualifier(GLSLParser.Storage_qualifierContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitStorage_qualifier");
        }
        if (delegate != null) {
            delegate.exitStorage_qualifier(ctx);
        }

    }

    @Override
    public void enterLayout_qualifier(GLSLParser.Layout_qualifierContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterLayout_qualifier");
        }
        if (delegate != null) {
            delegate.enterLayout_qualifier(ctx);
        }

    }

    @Override
    public void exitLayout_qualifier(GLSLParser.Layout_qualifierContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitLayout_qualifier");
        }
        if (delegate != null) {
            delegate.exitLayout_qualifier(ctx);
        }

    }

    @Override
    public void enterLayout_qualifier_id_list(GLSLParser.Layout_qualifier_id_listContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterLayout_qualifier_id_list");
        }
        if (delegate != null) {
            delegate.enterLayout_qualifier_id_list(ctx);
        }

    }

    @Override
    public void exitLayout_qualifier_id_list(GLSLParser.Layout_qualifier_id_listContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitLayout_qualifier_id_list");
        }
        if (delegate != null) {
            delegate.exitLayout_qualifier_id_list(ctx);
        }

    }

    @Override
    public void enterLayout_qualifier_id(GLSLParser.Layout_qualifier_idContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterLayout_qualifier_id");
        }
        if (delegate != null) {
            delegate.enterLayout_qualifier_id(ctx);
        }

    }

    @Override
    public void exitLayout_qualifier_id(GLSLParser.Layout_qualifier_idContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitLayout_qualifier_id");
        }
        if (delegate != null) {
            delegate.exitLayout_qualifier_id(ctx);
        }

    }

    @Override
    public void enterPrecision_qualifier(GLSLParser.Precision_qualifierContext ctx) {

    }

    @Override
    public void exitPrecision_qualifier(GLSLParser.Precision_qualifierContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitPrecision_qualifier");
        }
        if (delegate != null) {
            delegate.exitPrecision_qualifier(ctx);
        }

    }

    @Override
    public void enterInterpolation_qualifier(GLSLParser.Interpolation_qualifierContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterInterpolation_qualifier");
        }
        if (delegate != null) {
            delegate.enterInterpolation_qualifier(ctx);
        }

    }

    @Override
    public void exitInterpolation_qualifier(GLSLParser.Interpolation_qualifierContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitInterpolation_qualifier");
        }
        if (delegate != null) {
            delegate.exitInterpolation_qualifier(ctx);
        }

    }

    @Override
    public void enterInvariant_qualifier(GLSLParser.Invariant_qualifierContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterInvariant_qualifier");
        }
        if (delegate != null) {
            delegate.enterInvariant_qualifier(ctx);
        }

    }

    @Override
    public void exitInvariant_qualifier(GLSLParser.Invariant_qualifierContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitInvariant_qualifier");
        }
        if (delegate != null) {
            delegate.exitInvariant_qualifier(ctx);
        }

    }

    @Override
    public void enterPrecise_qualifier(GLSLParser.Precise_qualifierContext ctx) {

    }

    @Override
    public void exitPrecise_qualifier(GLSLParser.Precise_qualifierContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitPrecise_qualifier");
        }
        if (delegate != null) {
            delegate.exitPrecise_qualifier(ctx);
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
        if (showEnter) {
            System.out.println(exit() + "+ exitType_qualifier");
        }
        if (delegate != null) {
            delegate.exitType_qualifier(ctx);
        }

    }

    @Override
    public void enterSingle_type_qualifier(GLSLParser.Single_type_qualifierContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterSingle_type_qualifier");
        }
        if (delegate != null) {
            delegate.enterSingle_type_qualifier(ctx);
        }

    }

    @Override
    public void exitSingle_type_qualifier(GLSLParser.Single_type_qualifierContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitSingle_type_qualifier");
        }
        if (delegate != null) {
            delegate.exitSingle_type_qualifier(ctx);
        }

    }

    @Override
    public void enterType_name_list(GLSLParser.Type_name_listContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterType_name_list");
        }
        if (delegate != null) {
            delegate.enterType_name_list(ctx);
        }

    }

    @Override
    public void exitType_name_list(GLSLParser.Type_name_listContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitType_name_list");
        }
        if (delegate != null) {
            delegate.exitType_name_list(ctx);
        }

    }

    @Override
    public void enterType_specifier(GLSLParser.Type_specifierContext ctx) {

    }

    @Override
    public void exitType_specifier(GLSLParser.Type_specifierContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitType_specifier");
        }
        if (delegate != null) {
            delegate.exitType_specifier(ctx);
        }

    }

    @Override
    public void enterArray_specifier(GLSLParser.Array_specifierContext ctx) {

    }

    @Override
    public void exitArray_specifier(GLSLParser.Array_specifierContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitArray_specifier");
        }
        if (delegate != null) {
            delegate.exitArray_specifier(ctx);
        }

    }

    @Override
    public void enterType_specifier_no_array(GLSLParser.Type_specifier_no_arrayContext ctx) {

    }

    @Override
    public void exitType_specifier_no_array(GLSLParser.Type_specifier_no_arrayContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitType_specifier_no_array");
        }
        if (delegate != null) {
            delegate.exitType_specifier_no_array(ctx);
        }

    }

    @Override
    public void enterStruct_specifier(GLSLParser.Struct_specifierContext ctx) {

    }

    @Override
    public void exitStruct_specifier(GLSLParser.Struct_specifierContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitStruct_specifier");
        }
        if (delegate != null) {
            delegate.exitStruct_specifier(ctx);
        }

    }

    @Override
    public void enterStruct_declaration_list(GLSLParser.Struct_declaration_listContext ctx) {

    }

    @Override
    public void exitStruct_declaration_list(GLSLParser.Struct_declaration_listContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitStruct_declaration_list");
        }
        if (delegate != null) {
            delegate.exitStruct_declaration_list(ctx);
        }

    }

    @Override
    public void enterStruct_declaration(GLSLParser.Struct_declarationContext ctx) {

    }

    @Override
    public void exitStruct_declaration(GLSLParser.Struct_declarationContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitStruct_declaration");
        }
        if (delegate != null) {
            delegate.exitStruct_declaration(ctx);
        }

    }

    @Override
    public void enterStruct_declarator_list(GLSLParser.Struct_declarator_listContext ctx) {

    }

    @Override
    public void exitStruct_declarator_list(GLSLParser.Struct_declarator_listContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitStruct_declarator_list");
        }
        if (delegate != null) {
            delegate.exitStruct_declarator_list(ctx);
        }

    }

    @Override
    public void enterStruct_declarator(GLSLParser.Struct_declaratorContext ctx) {

    }

    @Override
    public void exitStruct_declarator(GLSLParser.Struct_declaratorContext ctx) {
        if (showEnter) {
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
        if (showEnter) {
            System.out.println(exit() + "+ exitInitializer");
        }
        if (delegate != null) {
            delegate.exitInitializer(ctx);
        }

    }

    @Override
    public void enterStatement_no_new_scope(GLSLParser.Statement_no_new_scopeContext ctx) {

    }

    @Override
    public void exitStatement_no_new_scope(GLSLParser.Statement_no_new_scopeContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitStatement_no_new_scope");
        }
        if (delegate != null) {
            delegate.exitStatement_no_new_scope(ctx);
        }

    }

    @Override
    public void enterStatement_with_scope(GLSLParser.Statement_with_scopeContext ctx) {

    }

    @Override
    public void exitStatement_with_scope(GLSLParser.Statement_with_scopeContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitStatement_with_scope");
        }
        if (delegate != null) {
            delegate.exitStatement_with_scope(ctx);
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
        if (showEnter) {
            System.out.println(exit() + "+ exitStatement_list");
        }
        if (delegate != null) {
            delegate.exitStatement_list(ctx);
        }

    }

    @Override
    public void enterCompound_statement_no_new_scope(GLSLParser.Compound_statement_no_new_scopeContext ctx) {

    }

    @Override
    public void exitCompound_statement_no_new_scope(GLSLParser.Compound_statement_no_new_scopeContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitCompound_statement_no_new_scope");
        }
        if (delegate != null) {
            delegate.exitCompound_statement_no_new_scope(ctx);
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
        if (showEnter) {
            System.out.println(exit() + "+ exitSimple_statement");
        }
        if (delegate != null) {
            delegate.exitSimple_statement(ctx);
        }

    }

    @Override
    public void enterDeclaration_statement(GLSLParser.Declaration_statementContext ctx) {

    }

    @Override
    public void exitDeclaration_statement(GLSLParser.Declaration_statementContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitDeclaration_statement");
        }
        if (delegate != null) {
            delegate.exitDeclaration_statement(ctx);
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
        if (showEnter) {
            System.out.println(exit() + "+ exitExpression_statement");
        }
        if (delegate != null) {
            delegate.exitExpression_statement(ctx);
        }

    }

    @Override
    public void enterSelection_statement(GLSLParser.Selection_statementContext ctx) {

    }

    @Override
    public void exitSelection_statement(GLSLParser.Selection_statementContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitSelection_statement");
        }
        if (delegate != null) {
            delegate.exitSelection_statement(ctx);
        }

    }

    @Override
    public void enterCondition(GLSLParser.ConditionContext ctx) {

    }

    @Override
    public void exitCondition(GLSLParser.ConditionContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitCondition");
        }
        if (delegate != null) {
            delegate.exitCondition(ctx);
        }

    }

    @Override
    public void enterSwitch_statement(GLSLParser.Switch_statementContext ctx) {

    }

    @Override
    public void exitSwitch_statement(GLSLParser.Switch_statementContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitSwitch_statement");
        }
        if (delegate != null) {
            delegate.exitSwitch_statement(ctx);
        }

    }

    @Override
    public void enterSwitch_case_statement(GLSLParser.Switch_case_statementContext ctx) {

    }

    @Override
    public void exitSwitch_case_statement(GLSLParser.Switch_case_statementContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitSwitch_case_statement");
        }
        if (delegate != null) {
            delegate.exitSwitch_case_statement(ctx);
        }

    }

    @Override
    public void enterIteration_while_statement(GLSLParser.Iteration_while_statementContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterIteration_while_statement");
        }
        if (delegate != null) {
            delegate.enterIteration_while_statement(ctx);
        }

    }

    @Override
    public void exitIteration_while_statement(GLSLParser.Iteration_while_statementContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitIteration_while_statement");
        }
        if (delegate != null) {
            delegate.exitIteration_while_statement(ctx);
        }

    }

    @Override
    public void enterIteration_do_while_statement(GLSLParser.Iteration_do_while_statementContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterIteration_do_while_statement");
        }
        if (delegate != null) {
            delegate.enterIteration_do_while_statement(ctx);
        }

    }

    @Override
    public void exitIteration_do_while_statement(GLSLParser.Iteration_do_while_statementContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitIteration_do_while_statement");
        }
        if (delegate != null) {
            delegate.exitIteration_do_while_statement(ctx);
        }

    }

    @Override
    public void enterIteration_for_statement(GLSLParser.Iteration_for_statementContext ctx) {
        if (showEnter) {
            System.out.println(enter() + "+ enterIteration_for_statement");
        }
        if (delegate != null) {
            delegate.enterIteration_for_statement(ctx);
        }

    }

    @Override
    public void exitIteration_for_statement(GLSLParser.Iteration_for_statementContext ctx) {
        if (showEnter) {
            System.out.println(exit() + "+ exitIteration_for_statement");
        }
        if (delegate != null) {
            delegate.exitIteration_for_statement(ctx);
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
        if (showEnter) {
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
        if (showEnter) {
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
        if (showEnter) {
            System.out.println(exit() + "+ exitJump_statement");
        }
        if (delegate != null) {
            delegate.exitJump_statement(ctx);
        }

    }
}
