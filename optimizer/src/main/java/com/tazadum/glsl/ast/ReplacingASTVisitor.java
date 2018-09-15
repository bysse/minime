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
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.optimizer.OptimizerBranch;

/**
 * Created by Erik on 2016-10-20.
 */
public class ReplacingASTVisitor implements ASTVisitor<Node> {
    public static final Node REMOVE = new LeafNode();

    protected ParserContext parserContext;
    protected Node firstNode;

    private boolean dereference;
    private boolean reference;

    public ReplacingASTVisitor(ParserContext parserContext, boolean dereference) {
        this(parserContext, dereference, dereference);
    }

    public ReplacingASTVisitor(ParserContext parserContext, boolean dereference, boolean reference) {
        this.parserContext = parserContext;
        this.dereference = dereference;
        this.reference = reference;
    }

    /**
     * Creates an OptimizerBranch from the first encountered branch and the ParserContext.
     * @return An instance of OptimizerBranch.
     */
    public OptimizerBranch createBranch() {
        if (firstNode == null) {
            throw new IllegalStateException("'firstNode' was null, has the visitor been used?");
        }
        return OptimizerBranch.remap(parserContext, firstNode);
    }

    @Override
    public Node visitBoolean(BooleanLeafNode node) {
        processLeafNode(node);
        return null;
    }

    @Override
    public Node visitStatementList(StatementListNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitParenthesis(ParenthesisNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitVariable(VariableNode node) {
        processLeafNode(node);
        return null;
    }

    @Override
    public Node visitVariableDeclaration(VariableDeclarationNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitVariableDeclarationList(VariableDeclarationListNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitPrecision(PrecisionDeclarationNode node) {
        processLeafNode(node);
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
        processLeafNode(node);
        return null;
    }

    @Override
    public Node visitDiscard(DiscardLeafNode node) {
        processLeafNode(node);
        return null;
    }

    @Override
    public Node visitContinue(ContinueLeafNode node) {
        processLeafNode(node);
        return null;
    }

    @Override
    public Node visitCondition(ConditionNode node) {
        processLeafNode(node);
        return null;
    }

    @Override
    public Node visitBreak(BreakLeafNode node) {
        processLeafNode(node);
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
        processLeafNode(node);
        return null;
    }

    @Override
    public Node visitFloat(FloatLeafNode node) {
        processLeafNode(node);
        return null;
    }

    protected void processLeafNode(Node node) {
        if (firstNode == null) {
            firstNode = node;
        }
    }

    protected void processParentNode(ParentNode node) {
        if (firstNode == null) {
            firstNode = node;
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            Node child = node.getChild(i);
            if (child == null) {
                continue;
            }
            final Node replacement = child.accept(this);
            if (replacement != null) {
                if (dereference) {
                    parserContext.dereferenceTree(child);
                }
                if (replacement.equals(REMOVE)) {
                    node.removeChild(child);
                    i--;
                } else {
                    node.setChild(i, replacement);

                    if (reference) {
                        // add references to all variables and function calls in the new child
                        parserContext.referenceTree(replacement);
                    }
                }
            }
        }
    }
}
