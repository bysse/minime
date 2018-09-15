package com.tazadum.glsl.parser.listener;

import com.tazadum.glsl.language.GLSLBaseListener;
import com.tazadum.glsl.language.GLSLParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class WalkableListener extends GLSLBaseListener {
    public String SPACES = "                                                                                        ";

    public void walk(ParseTree t) {
        if (t instanceof ErrorNode) {
            visitErrorNode((ErrorNode) t);
            return;
        } else if (t instanceof TerminalNode) {
            visitTerminal((TerminalNode) t);
            return;
        }
        RuleNode r = (RuleNode) t;
        enterRule(r);
        exitRule(r);
    }

    protected void walkChildren(ParseTree t) {
        if (!process(t)) {
            return;
        }
        final RuleNode r = (RuleNode) t;
        final int n = r.getChildCount();
        for (int i = 0; i < n; i++) {
            ParseTree child = r.getChild(i);
            if (process(child)) {
                RuleNode childNode = (RuleNode) child;
                enterRule(childNode);
                exitRule(childNode);
            }
        }
    }

    private boolean process(ParseTree t) {
        if (t instanceof ErrorNode) {
            visitErrorNode((ErrorNode) t);
            return false;
        } else if (t instanceof TerminalNode) {
            visitTerminal((TerminalNode) t);
            return false;
        }
        return true;
    }

    private void enterRule(RuleNode r) {
        ParserRuleContext ctx = (ParserRuleContext) r.getRuleContext();
        enterEveryRule(ctx);
        ctx.enterRule(this);
    }

    private void exitRule(RuleNode r) {
        ParserRuleContext ctx = (ParserRuleContext) r.getRuleContext();
        ctx.exitRule(this);
        exitEveryRule(ctx);
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        System.out.println(SPACES.substring(0, 2 * ctx.depth()) + " + enter" + ctx.getClass().getSimpleName());
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        System.out.println(SPACES.substring(0, 2 * ctx.depth()) + " + exit" + ctx.getClass().getSimpleName());
    }

    @Override
    public void exitAdditiveExpression(GLSLParser.AdditiveExpressionContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitArray_specifier(GLSLParser.Array_specifierContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitAssignment_expression(GLSLParser.Assignment_expressionContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitAssignment_operator(GLSLParser.Assignment_operatorContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitCompound_statement_no_new_scope(GLSLParser.Compound_statement_no_new_scopeContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitCompound_statement_with_scope(GLSLParser.Compound_statement_with_scopeContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitCondition(GLSLParser.ConditionContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitConditional_expression(GLSLParser.Conditional_expressionContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitConstant_expression(GLSLParser.Constant_expressionContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitConstructor_identifier(GLSLParser.Constructor_identifierContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitDeclaration_statement(GLSLParser.Declaration_statementContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitDivisionExpression(GLSLParser.DivisionExpressionContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitDoIterationStatement(GLSLParser.DoIterationStatementContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitExpression(GLSLParser.ExpressionContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitExpression_statement(GLSLParser.Expression_statementContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitExternal_declaration(GLSLParser.External_declarationContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitField_selection(GLSLParser.Field_selectionContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitFor_init_statement(GLSLParser.For_init_statementContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitFor_rest_statement(GLSLParser.For_rest_statementContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitForIterationStatement(GLSLParser.ForIterationStatementContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitFully_specified_type(GLSLParser.Fully_specified_typeContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitFunction_call(GLSLParser.Function_callContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitFunction_call_header(GLSLParser.Function_call_headerContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitFunction_declarator(GLSLParser.Function_declaratorContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitFunction_definition(GLSLParser.Function_definitionContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitFunction_header(GLSLParser.Function_headerContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitFunction_identifier(GLSLParser.Function_identifierContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitFunction_prototype(GLSLParser.Function_prototypeContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitFunctionDeclaration(GLSLParser.FunctionDeclarationContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitInit_declarator_list(GLSLParser.Init_declarator_listContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitInitializer(GLSLParser.InitializerContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitInteger_expression(GLSLParser.Integer_expressionContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitJump_statement(GLSLParser.Jump_statementContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitLogicalAnd(GLSLParser.LogicalAndContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitLogicalEquality(GLSLParser.LogicalEqualityContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitLogicalInEquality(GLSLParser.LogicalInEqualityContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitLogicalOr(GLSLParser.LogicalOrContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitLogicalXor(GLSLParser.LogicalXorContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitMultiplicationExpression(GLSLParser.MultiplicationExpressionContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitNumericDelegator(GLSLParser.NumericDelegatorContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitParameter_declaration(GLSLParser.Parameter_declarationContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitParameter_qualifier(GLSLParser.Parameter_qualifierContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitPostfix_expression(GLSLParser.Postfix_expressionContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitPrecision_qualifier(GLSLParser.Precision_qualifierContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitPrecisionDeclaration(GLSLParser.PrecisionDeclarationContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitPrimary_expression(GLSLParser.Primary_expressionContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitPrimary_expression_or_function_call(GLSLParser.Primary_expression_or_function_callContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitRelational(GLSLParser.RelationalContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitSelection_statement(GLSLParser.Selection_statementContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitSimple_statement(GLSLParser.Simple_statementContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitSingle_declaration(GLSLParser.Single_declarationContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitStatement_list(GLSLParser.Statement_listContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitStatement_no_new_scope(GLSLParser.Statement_no_new_scopeContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitStatement_with_scope(GLSLParser.Statement_with_scopeContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitStruct_declaration(GLSLParser.Struct_declarationContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitStruct_declaration_list(GLSLParser.Struct_declaration_listContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitStruct_declarator(GLSLParser.Struct_declaratorContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitStruct_declarator_list(GLSLParser.Struct_declarator_listContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitStruct_specifier(GLSLParser.Struct_specifierContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitSubtractionExpression(GLSLParser.SubtractionExpressionContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitTranslation_unit(GLSLParser.Translation_unitContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitType_qualifier(GLSLParser.Type_qualifierContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitType_specifier(GLSLParser.Type_specifierContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitType_specifier_no_prec(GLSLParser.Type_specifier_no_precContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitUnary_expression(GLSLParser.Unary_expressionContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitUnary_operator(GLSLParser.Unary_operatorContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitUnaryExpression(GLSLParser.UnaryExpressionContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitVariable_identifier(GLSLParser.Variable_identifierContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitVariableDeclaration(GLSLParser.VariableDeclarationContext ctx) {
        walkChildren(ctx);
    }

    @Override
    public void exitWhileIterationStatement(GLSLParser.WhileIterationStatementContext ctx) {
        walkChildren(ctx);
    }
}
