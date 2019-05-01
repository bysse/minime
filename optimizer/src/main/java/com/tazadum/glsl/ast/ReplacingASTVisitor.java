package com.tazadum.glsl.ast;

import com.tazadum.glsl.language.ast.*;
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
import com.tazadum.glsl.language.ast.util.NodeUtil;
import com.tazadum.glsl.language.ast.variable.*;
import com.tazadum.glsl.optimizer.Branch;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.util.SourcePosition;

/**
 * Created by Erik on 2016-10-20.
 */
public class ReplacingASTVisitor implements ASTVisitor<Node> {
    public static final Node REMOVE = new LeafNode(SourcePosition.TOP);

    protected ParserContext parserContext;
    protected Node firstNode;

    protected boolean dereference;
    protected boolean reference;

    /**
     * Constructs a base visitor.
     *
     * @param parserContext The ParserContext to operate in.
     * @param dereference   Automatically dereference removes node trees.
     */
    public ReplacingASTVisitor(ParserContext parserContext, boolean dereference) {
        this(parserContext, dereference, dereference);
    }

    /**
     * Constructs a base visitor.
     *
     * @param parserContext The ParserContext to operate in.
     * @param dereference   Automatically dereference removes node trees.
     * @param reference     Automatically reference added node trees.
     */
    public ReplacingASTVisitor(ParserContext parserContext, boolean dereference, boolean reference) {
        this.parserContext = parserContext;
        this.dereference = dereference;
        this.reference = reference;
    }

    /**
     * Entry point for the Replacing visitor which handles replacements of the root node.
     */
    public Node applyOn(Node node) {
        if (node == null) {
            return null;
        }
        final Node replacement = node.accept(this);
        if (replacement == null) {
            return node;
        }

        if (replacement.equals(node)) {
            return node;
        }
        if (dereference) {
            parserContext.dereferenceTree(node);
        }
        if (replacement.equals(REMOVE)) {
           return null;
        }

        if (replacement.getId() != 1) {
            replacement.calculateId(1);
        }

        if (reference) {
            parserContext.referenceTree(replacement);
        }

        return replacement;
    }

    /**
     * Creates an OptimizerBranch from the first encountered branch and the ParserContext.
     *
     * @return An instance of OptimizerBranch.
     */
    public Branch createBranch() {
        if (firstNode == null) {
            throw new IllegalStateException("'firstNode' was null, has the visitor been used?");
        }
        return Branch.remap(parserContext, firstNode);
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
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitBreak(BreakLeafNode node) {
        processLeafNode(node);
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
    public Node visitNumeric(NumericLeafNode node) {
        processLeafNode(node);
        return null;
    }

    @Override
    public Node visitBitOperation(BitOperationNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitSwitch(SwitchNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitSwitchCase(CaseNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitSwitchDefault(DefaultCaseNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitInitializerList(InitializerListNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitTypeQualifierDeclarationNode(TypeQualifierDeclarationNode node) {
        processLeafNode(node);
        return null;
    }

    @Override
    public Node visitStructDeclarationNode(StructDeclarationNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitInterfaceBlockNode(InterfaceBlockNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitTypeDeclaration(TypeDeclarationNode node) {
        processParentNode(node);
        return null;
    }

    @Override
    public Node visitBlockNode(ContextBlockNode node) {
        processParentNode(node);
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
                if (replacement.equals(child)) {
                    continue;
                }
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
