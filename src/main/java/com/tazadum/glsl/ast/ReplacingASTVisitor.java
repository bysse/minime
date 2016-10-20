package com.tazadum.glsl.ast;

import com.tazadum.glsl.ast.arithmetic.*;
import com.tazadum.glsl.ast.conditional.*;
import com.tazadum.glsl.ast.expression.AssignmentNode;
import com.tazadum.glsl.ast.expression.ConstantExpressionNode;
import com.tazadum.glsl.ast.function.FunctionCallNode;
import com.tazadum.glsl.ast.function.FunctionDefinitionNode;
import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.ast.iteration.DoWhileIterationNode;
import com.tazadum.glsl.ast.iteration.ForIterationNode;
import com.tazadum.glsl.ast.iteration.WhileIterationNode;
import com.tazadum.glsl.ast.logical.BooleanLeafNode;
import com.tazadum.glsl.ast.logical.LogicalOperationNode;
import com.tazadum.glsl.ast.logical.RelationalOperationNode;
import com.tazadum.glsl.ast.variable.*;

/**
 * Created by Erik on 2016-10-20.
 */
public class ReplacingASTVisitor implements ASTVisitor<Node> {
    @Override
    public Node visitBoolean(BooleanLeafNode node) {
        return null;
    }

    @Override
    public Node visitStatementList(StatementListNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitParenthesis(ParenthesisNode node) {
        final Node replacement = node.getExpression().accept(this);
        if (replacement != null) {
            node.setExpression(replacement);
        }
        return null;
    }

    @Override
    public Node visitVariable(VariableNode node) {
        return null;
    }

    @Override
    public Node visitVariableDeclaration(VariableDeclarationNode node) {
        if (node.getArraySpecifier() != null) {
            final Node replacement = node.getArraySpecifier().accept(this);
            if (replacement != null) {
                node.setArraySpecifier(replacement);
            }
        }
        if (node.getInitializer() != null) {
            final Node replacement = node.getInitializer().accept(this);
            if (replacement != null) {
                node.setInitializer(replacement);
            }
        }
        return null;
    }

    @Override
    public Node visitVariableDeclarationList(VariableDeclarationListNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitPrecision(PrecisionDeclarationNode node) {
        return null;
    }

    @Override
    public Node visitParameterDeclaration(ParameterDeclarationNode node) {
        return visitVariableDeclaration(node);
    }

    @Override
    public Node visitFieldSelection(FieldSelectionNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitArrayIndex(ArrayIndexNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitRelationalOperation(RelationalOperationNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitLogicalOperation(LogicalOperationNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitWhileIteration(WhileIterationNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitForIteration(ForIterationNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitDoWhileIteration(DoWhileIterationNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitFunctionPrototype(FunctionPrototypeNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitFunctionDefinition(FunctionDefinitionNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitFunctionCall(FunctionCallNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitConstantExpression(ConstantExpressionNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitAssignment(AssignmentNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitTernaryCondition(TernaryConditionNode node) {
        processParentNode(node);
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
    public Node visitUnaryOperation(UnaryOperationNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitPrefixOperation(PrefixOperationNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitPostfixOperation(PostfixOperationNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitNumericOperation(NumericOperationNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitInt(IntLeafNode node) {
        return null;
    }

    @Override
    public Node visitFloat(FloatLeafNode node) {
        return null;
    }

    private void processParentNode(ParentNode node) {
        for (int i = 0; i < node.getChildCount(); i++) {
            final Node replacement = node.getChild(i).accept(this);
            if (replacement != null) {
                node.setChild(i, replacement);
            }
        }
    }
}
