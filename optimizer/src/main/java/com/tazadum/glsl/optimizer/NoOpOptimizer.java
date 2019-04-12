package com.tazadum.glsl.optimizer;

import com.tazadum.glsl.language.ast.ContextBlockNode;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.StatementListNode;
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
import com.tazadum.glsl.parser.ParserContext;

/**
 * Created by erikb on 2018-10-30.
 */
public class NoOpOptimizer extends BranchingOptimizer {
    @Override
    protected OptimizerVisitor createVisitor(ParserContext context, BranchRegistry branchRegistry, OptimizationDecider decider) {
        return new OptimizerVisitor() {
            @Override
            public void reset() {

            }

            @Override
            public int getChanges() {
                return 0;
            }

            @Override
            public Node visitBoolean(BooleanLeafNode node) {
                return null;
            }

            @Override
            public Node visitStatementList(StatementListNode node) {
                return null;
            }

            @Override
            public Node visitParenthesis(ParenthesisNode node) {
                return null;
            }

            @Override
            public Node visitVariable(VariableNode node) {
                return null;
            }

            @Override
            public Node visitVariableDeclaration(VariableDeclarationNode node) {
                return null;
            }

            @Override
            public Node visitVariableDeclarationList(VariableDeclarationListNode node) {
                return null;
            }

            @Override
            public Node visitPrecision(PrecisionDeclarationNode node) {
                return null;
            }

            @Override
            public Node visitParameterDeclaration(ParameterDeclarationNode node) {
                return null;
            }

            @Override
            public Node visitFieldSelection(FieldSelectionNode node) {
                return null;
            }

            @Override
            public Node visitArrayIndex(ArrayIndexNode node) {
                return null;
            }

            @Override
            public Node visitRelationalOperation(RelationalOperationNode node) {
                return null;
            }

            @Override
            public Node visitLogicalOperation(LogicalOperationNode node) {
                return null;
            }

            @Override
            public Node visitWhileIteration(WhileIterationNode node) {
                return null;
            }

            @Override
            public Node visitForIteration(ForIterationNode node) {
                return null;
            }

            @Override
            public Node visitDoWhileIteration(DoWhileIterationNode node) {
                return null;
            }

            @Override
            public Node visitFunctionPrototype(FunctionPrototypeNode node) {
                return null;
            }

            @Override
            public Node visitFunctionDefinition(FunctionDefinitionNode node) {
                return null;
            }

            @Override
            public Node visitFunctionCall(FunctionCallNode node) {
                return null;
            }

            @Override
            public Node visitConstantExpression(ConstantExpressionNode node) {
                return null;
            }

            @Override
            public Node visitAssignment(AssignmentNode node) {
                return null;
            }

            @Override
            public Node visitTernaryCondition(TernaryConditionNode node) {
                return null;
            }

            @Override
            public Node visitReturn(ReturnNode node) {
                return null;
            }

            @Override
            public Node visitDiscard(DiscardLeafNode node) {
                return null;
            }

            @Override
            public Node visitContinue(ContinueLeafNode node) {
                return null;
            }

            @Override
            public Node visitCondition(ConditionNode node) {
                return null;
            }

            @Override
            public Node visitBreak(BreakLeafNode node) {
                return null;
            }

            @Override
            public Node visitPrefixOperation(PrefixOperationNode node) {
                return null;
            }

            @Override
            public Node visitPostfixOperation(PostfixOperationNode node) {
                return null;
            }

            @Override
            public Node visitNumericOperation(NumericOperationNode node) {
                return null;
            }

            @Override
            public Node visitNumeric(NumericLeafNode node) {
                return null;
            }

            @Override
            public Node visitBitOperation(BitOperationNode node) {
                return null;
            }

            @Override
            public Node visitSwitch(SwitchNode node) {
                return null;
            }

            @Override
            public Node visitSwitchCase(CaseNode node) {
                return null;
            }

            @Override
            public Node visitSwitchDefault(DefaultCaseNode node) {
                return null;
            }

            @Override
            public Node visitInitializerList(InitializerListNode node) {
                return null;
            }

            @Override
            public Node visitTypeQualifierDeclarationNode(TypeQualifierDeclarationNode node) {
                return null;
            }

            @Override
            public Node visitStructDeclarationNode(StructDeclarationNode node) {
                return null;
            }

            @Override
            public Node visitInterfaceBlockNode(InterfaceBlockNode node) {
                return null;
            }

            @Override
            public Node visitTypeDeclaration(TypeDeclarationNode node) {
                return null;
            }

            @Override
            public Node visitBlockNode(ContextBlockNode node) {
                return null;
            }
        };
    }

    @Override
    public String name() {
        return "no-op";
    }
}
