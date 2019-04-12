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
 * Created by Erik on 2016-10-20.
 */
public class DefaultASTVisitor<T> implements ASTVisitor<T> {
    @Override
    public T visitBoolean(BooleanLeafNode node) {
        return null;
    }

    @Override
    public T visitStatementList(StatementListNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitParenthesis(ParenthesisNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitVariable(VariableNode node) {
        visitLeafNode(node);
        return null;
    }

    @Override
    public T visitVariableDeclaration(VariableDeclarationNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitVariableDeclarationList(VariableDeclarationListNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitPrecision(PrecisionDeclarationNode node) {
        visitLeafNode(node);
        return null;
    }

    @Override
    public T visitParameterDeclaration(ParameterDeclarationNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitFieldSelection(FieldSelectionNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitArrayIndex(ArrayIndexNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitRelationalOperation(RelationalOperationNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitLogicalOperation(LogicalOperationNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitWhileIteration(WhileIterationNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitForIteration(ForIterationNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitDoWhileIteration(DoWhileIterationNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitFunctionPrototype(FunctionPrototypeNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitFunctionDefinition(FunctionDefinitionNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitFunctionCall(FunctionCallNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitConstantExpression(ConstantExpressionNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitAssignment(AssignmentNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitTernaryCondition(TernaryConditionNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitReturn(ReturnNode node) {
        if (node.hasExpression()) {
            visitChildren(node);
        }
        return null;
    }

    @Override
    public T visitDiscard(DiscardLeafNode node) {
        visitLeafNode(node);
        return null;
    }

    @Override
    public T visitContinue(ContinueLeafNode node) {
        visitLeafNode(node);
        return null;
    }

    @Override
    public T visitCondition(ConditionNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitBreak(BreakLeafNode node) {
        visitLeafNode(node);
        return null;
    }

    @Override
    public T visitPrefixOperation(PrefixOperationNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitPostfixOperation(PostfixOperationNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitNumericOperation(NumericOperationNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitNumeric(NumericLeafNode node) {
        visitLeafNode(node);
        return null;
    }

    @Override
    public T visitTypeDeclaration(TypeDeclarationNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitBlockNode(ContextBlockNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitBitOperation(BitOperationNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitSwitch(SwitchNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitSwitchCase(CaseNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitSwitchDefault(DefaultCaseNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitInitializerList(InitializerListNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitTypeQualifierDeclarationNode(TypeQualifierDeclarationNode node) {
        visitLeafNode(node);
        return null;
    }

    @Override
    public T visitStructDeclarationNode(StructDeclarationNode node) {
        visitChildren(node);
        return null;
    }

    @Override
    public T visitInterfaceBlockNode(InterfaceBlockNode node) {
        visitChildren(node);
        return null;
    }

    protected <V extends ParentNode> void visitChildren(V node) {
        for (int i = 0; i < node.getChildCount(); i++) {
            Node child = node.getChild(i);
            if (child != null) {
                child.accept(this);
            }
        }
    }

    protected void visitLeafNode(LeafNode leafNode) {
    }
}
