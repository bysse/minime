package com.tazadum.glsl.language.ast;

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
import com.tazadum.glsl.language.ast.type.TypeDeclarationNode;
import com.tazadum.glsl.language.ast.type.TypeQualifierDeclarationNode;
import com.tazadum.glsl.language.ast.variable.*;

/**
 * Created by Erik on 2016-10-13.
 */
public interface ASTVisitor<T> {
    T visitBoolean(BooleanLeafNode node);

    T visitStatementList(StatementListNode node);

    T visitParenthesis(ParenthesisNode node);

    T visitVariable(VariableNode node);

    T visitVariableDeclaration(VariableDeclarationNode node);

    T visitVariableDeclarationList(VariableDeclarationListNode node);

    T visitPrecision(PrecisionDeclarationNode node);

    T visitParameterDeclaration(ParameterDeclarationNode node);

    T visitFieldSelection(FieldSelectionNode node);

    T visitArrayIndex(ArrayIndexNode node);

    T visitRelationalOperation(RelationalOperationNode node);

    T visitLogicalOperation(LogicalOperationNode node);

    T visitWhileIteration(WhileIterationNode node);

    T visitForIteration(ForIterationNode node);

    T visitDoWhileIteration(DoWhileIterationNode node);

    T visitFunctionPrototype(FunctionPrototypeNode node);

    T visitFunctionDefinition(FunctionDefinitionNode node);

    T visitFunctionCall(FunctionCallNode node);

    T visitConstantExpression(ConstantExpressionNode node);

    T visitAssignment(AssignmentNode node);

    T visitTernaryCondition(TernaryConditionNode node);

    T visitReturn(ReturnNode node);

    T visitDiscard(DiscardLeafNode node);

    T visitContinue(ContinueLeafNode node);

    T visitCondition(ConditionNode node);

    T visitBreak(BreakLeafNode node);

    T visitPrefixOperation(PrefixOperationNode node);

    T visitPostfixOperation(PostfixOperationNode node);

    T visitNumericOperation(NumericOperationNode node);

    T visitInt(IntLeafNode node);

    T visitFloat(FloatLeafNode node);

    T visitBitOperation(BitOperationNode node);

    T visitSwitch(SwitchNode node);

    T visitSwitchCase(CaseNode node);

    T visitSwitchDefault(DefaultCaseNode node);

    T visitInitializerList(InitializerListNode node);

    T visitTypeQualifierDeclarationNode(TypeQualifierDeclarationNode node);

    T visitStructDeclarationNode(StructDeclarationNode node);

    T visitInterfaceBlockNode(InterfaceBlockNode node);

    T visitTypeDeclaration(TypeDeclarationNode node);
}
