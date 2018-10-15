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
import com.tazadum.glsl.language.ast.type.*;
import com.tazadum.glsl.language.ast.unresolved.*;
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

    T visitUnaryOperation(UnaryOperationNode node);

    T visitPrefixOperation(PrefixOperationNode node);

    T visitPostfixOperation(PostfixOperationNode node);

    T visitNumericOperation(NumericOperationNode node);

    T visitInt(IntLeafNode node);

    T visitFloat(FloatLeafNode node);

    T visitTypeDeclaration(UnresolvedTypeDeclarationNode node);

    T visitBitOperation(BitOperationNode node);

    T visitSwitch(SwitchNode node);

    T visitSwitchCase(CaseNode node);

    T visitSwitchDefault(DefaultCaseNode node);

    T visitTypeNode(TypeNode node);

    T visitArrayTypeNode(ArraySpecifierNode node);

    T visitArrayTypeListNode(ArraySpecifierListNode node);

    T visitTypeNode(UnresolvedTypeNode node); // there are potential sub-trees here

    T visitTypeQualifierNode(TypeQualifierNode node); // there are potential sub-trees here

    T visitVariable(UnresolvedVariableNode node);

    T visitFunctionPrototype(UnresolvedFunctionPrototypeNode node);

    T visitVariableDeclarationList(UnresolvedVariableDeclarationListNode node);

    T visitVariableDeclaration(UnresolvedVariableDeclarationNode node);

    T visitParameterDeclaration(UnresolvedParameterDeclarationNode node);

    T visitFunctionDefinition(UnresolvedFunctionDefinitionNode node);

    T visitInitializerList(InitializerListNode node);

    T visitTypeQualifierListNode(TypeQualifierListNode node);

    T visitTypeQualifierDeclarationNode(TypeQualifierDeclarationNode node);

    T visitLayoutQualifierListNode(LayoutQualifierListNode node);

    T visitLayoutQualifierIdNode(LayoutQualifierIdNode node);

    T visitUnresolvedStructDeclarationNode(UnresolvedStructDeclarationNode node);

    T visitUnresolvedStructFieldNode(UnresolvedStructFieldNode node);

    T visitUnresolvedStructFieldDeclarationNode(UnresolvedStructFieldDeclarationNode node);

    T visitUnresolvedStructFieldListNode(UnresolvedStructFieldListNode node);

    T visitUnresolvedInterfaceBlockNode(UnresolvedInterfaceBlockNode node);
}
