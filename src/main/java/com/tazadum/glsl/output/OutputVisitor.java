package com.tazadum.glsl.output;

import com.tazadum.glsl.ast.*;
import com.tazadum.glsl.ast.arithmetic.*;
import com.tazadum.glsl.ast.conditional.*;
import com.tazadum.glsl.ast.expression.AssignmentNode;
import com.tazadum.glsl.ast.expression.ConstantExpressionNode;
import com.tazadum.glsl.ast.function.FunctionCallNode;
import com.tazadum.glsl.ast.function.FunctionDefinitionNode;
import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.ast.iteration.DoWhileNode;
import com.tazadum.glsl.ast.iteration.ForIterationNode;
import com.tazadum.glsl.ast.iteration.WhileIterationNode;
import com.tazadum.glsl.ast.logical.BooleanLeafNode;
import com.tazadum.glsl.ast.logical.LogicalOperationNode;
import com.tazadum.glsl.ast.logical.RelationalOperationNode;
import com.tazadum.glsl.ast.variable.*;
import com.tazadum.glsl.language.Numeric;
import com.tazadum.glsl.parser.type.FullySpecifiedType;

/**
 * Created by Erik on 2016-10-13.
 */
public class OutputVisitor implements ASTVisitor<String> {
    private final OutputConfig config;

    public OutputVisitor(OutputConfig config) {
        this.config = config;
    }

    @Override
    public String visitBoolean(BooleanLeafNode node) {
        return String.valueOf(node.getValue());
    }

    @Override
    public String visitStatementList(StatementListNode node) {
        return visitChildren(node);
    }

    @Override
    public String visitParenthesis(ParenthesisNode node) {
        return visitChildren(node);
    }

    @Override
    public String visitVariable(VariableNode node) {
        return identifier(node.getDeclarationNode().getIdentifier());
    }

    @Override
    public String visitVariableDeclaration(VariableDeclarationNode node) {
        return visitChildren(node);
    }

    @Override
    public String visitVariableDeclarationList(VariableDeclarationListNode node) {
        return visitChildren(node);
    }

    @Override
    public String visitPrecision(PrecisionDeclarationNode node) {
        return node.getQualifier().token() + " " + node.getBuiltInType().token();
    }

    @Override
    public String visitParameterDeclaration(ParameterDeclarationNode node) {
        return "(" + visitChildren(node) + ")";
    }

    @Override
    public String visitFieldSelection(FieldSelectionNode node) {
        return visitChildren(node);
    }

    @Override
    public String visitArrayIndex(ArrayIndexNode node) {
        return visitChildren(node);
    }

    @Override
    public String visitRelationalOperation(RelationalOperationNode node) {
        final String left = node.getLeft().accept(this);
        final String right = node.getRight().accept(this);
        return left + node.getOperator().token() + right;
    }

    @Override
    public String visitLogicalOperation(LogicalOperationNode node) {
        final String left = node.getLeft().accept(this);
        final String right = node.getRight().accept(this);
        return left + node.getOperator().token() + right;
    }

    @Override
    public String visitWhileIteration(WhileIterationNode node) {
        return visitChildren(node);
    }

    @Override
    public String visitForIteration(ForIterationNode node) {
        return visitChildren(node);
    }

    @Override
    public String visitDoWhileIteration(DoWhileNode node) {
        return visitChildren(node);
    }

    @Override
    public String visitFunctionPrototype(FunctionPrototypeNode node) {
        return outputType(node.getReturnType()) + identifierSpacing() + identifier(node.getIdentifier());
    }

    @Override
    public String visitFunctionDefinition(FunctionDefinitionNode node) {
        return visitChildren(node);
    }

    @Override
    public String visitFunctionCall(FunctionCallNode node) {
        return visitChildren(node);
    }

    @Override
    public String visitConstantExpression(ConstantExpressionNode node) {
        return visitChildren(node);
    }

    @Override
    public String visitAssignment(AssignmentNode node) {
        final String left = node.getLeft().accept(this);
        final String right = node.getRight().accept(this);
        return left + node.getOperator().token() + right;
    }

    @Override
    public String visitTernaryCondition(TernaryConditionNode node) {
        final String condition = node.getCondition().accept(this);
        final String thenNode = node.getThen().accept(this);
        final String elseNode = node.getElse().accept(this);
        return String.format("%s?%s:%s", condition, thenNode, elseNode);
    }

    @Override
    public String visitReturn(ReturnNode node) {
        if (node.getExpression() == null) {
            return "return;";
        }
        return "return " + node.accept(this) + ";";
    }

    @Override
    public String visitDiscard(DiscardLeafNode node) {
        return "discard;";
    }

    @Override
    public String visitContinue(ContinueLeafNode node) {
        return "continue;";
    }

    @Override
    public String visitCondition(ConditionNode node) {
        return visitChildren(node);
    }

    @Override
    public String visitBreak(BreakLeafNode node) {
        return "break;";
    }

    @Override
    public String visitUnaryOperation(UnaryOperationNode node) {
        return node.getOperator().token() + node.getExpression().accept(this);
    }

    @Override
    public String visitPrefixOperation(PrefixOperationNode node) {
        return node.getOperator().token() + node.getExpression().accept(this);
    }

    @Override
    public String visitPostfixOperation(PostfixOperationNode node) {
        return node.getExpression().accept(this) + node.getOperator().token();
    }

    @Override
    public String visitNumericOperation(NumericOperationNode node) {
        final String left = node.getLeft().accept(this);
        final String right = node.getRight().accept(this);
        return left + node.getOperator().token() + right;
    }

    @Override
    public String visitInt(IntLeafNode node) {
        return formatInt(node.getValue());
    }

    @Override
    public String visitFloat(FloatLeafNode node) {
        return formatFloat(node.getValue());
    }

    private String formatInt(Numeric numeric) {
        return String.format("%d", (int)numeric.getValue());
    }

    private String formatFloat(Numeric numeric) {
        // TODO: make better
        return String.format("%f", numeric.getValue());
    }

    private String identifierSpacing() {
        if (config.getIdentifiers() == IdentifierOutput.None) {
            return "";
        }
        return " ";
    }

    private String identifier(Identifier identifier) {
        switch (config.getIdentifiers()) {
            case None:
                return "";
            case Original:
                return identifier.original();
            case Replaced:
                return identifier.token();
        }
        throw new UnsupportedOperationException("Unknown IdentifierOutput mode : " + config.getIdentifiers());
    }

    private String outputType(FullySpecifiedType type) {
        final StringBuilder builder = new StringBuilder();
        if (type.getQualifier() != null) {
            builder.append(type.getQualifier().token()).append(' ');
        }
        if (type.getPrecision() != null) {
            builder.append(type.getPrecision().token()).append(' ');
        }
        builder.append(type.getType().token());
        return builder.toString();
    }

    private <T extends ParentNode> String visitChildren(T node) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < node.getChildCount(); i++) {
            builder.append(node.getChild(i).accept(this));
        }
        return builder.toString();
    }
}