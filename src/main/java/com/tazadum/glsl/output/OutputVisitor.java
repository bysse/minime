package com.tazadum.glsl.output;

import com.tazadum.glsl.ast.*;
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
import com.tazadum.glsl.language.Numeric;
import com.tazadum.glsl.parser.type.FullySpecifiedType;

import java.util.Locale;

/**
 * Created by Erik on 2016-10-13.
 */
public class OutputVisitor implements ASTVisitor<String> {
    private final OutputConfig config;
    private String indentation;

    public OutputVisitor(OutputConfig config) {
        this.config = config;
        this.indentation = "";
    }

    @Override
    public String visitBoolean(BooleanLeafNode node) {
        return String.valueOf(node.getValue());
    }

    @Override
    public String visitStatementList(StatementListNode node) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < node.getChildCount(); i++) {
            final Node child = node.getChild(i);
            builder.append(indentation()).append(child.accept(this));

            // some statements do not require a semicolon
            if (!noSemiColon(child)) {
                builder.append(';');
            }
            builder.append(newLine());
        }
        return builder.toString();
    }

    @Override
    public String visitParenthesis(ParenthesisNode node) {
        return "(" + visitChildren(node) + ")";
    }

    @Override
    public String visitVariable(VariableNode node) {
        return identifier(node.getDeclarationNode().getIdentifier());
    }

    @Override
    public String visitVariableDeclaration(VariableDeclarationNode node) {
        final StringBuilder builder = new StringBuilder();

        // Don't output type, it should come from the VariableDeclarationList
        builder.append(identifier(node.getIdentifier()));

        if (node.getArraySpecifier() != null) {
            builder.append('[').append(node.getArraySpecifier().accept(this)).append(']');
        }

        if (node.getInitializer() != null) {
            builder.append('=').append(node.getInitializer().accept(this));
        }
        return builder.toString();
    }

    @Override
    public String visitVariableDeclarationList(VariableDeclarationListNode node) {
        final StringBuilder builder = new StringBuilder();

        builder.append(outputType(node.getFullySpecifiedType()));
        builder.append(identifierSpacing());

        for (int i = 0; i < node.getChildCount(); i++) {
            if (i > 0) {
                builder.append(',');
            }
            builder.append(node.getChild(i).accept(this));
        }

        return builder.toString();
    }

    @Override
    public String visitPrecision(PrecisionDeclarationNode node) {
        return node.getQualifier().token() + " " + node.getBuiltInType().token();
    }

    @Override
    public String visitParameterDeclaration(ParameterDeclarationNode node) {
        final StringBuilder builder = new StringBuilder();

        // type
        builder.append(outputType(node.getFullySpecifiedType()));
        builder.append(identifierSpacing());
        builder.append(identifier(node.getIdentifier()));

        if (node.getArraySpecifier() != null) {
            builder.append('[').append(node.getArraySpecifier().accept(this)).append(']');
        }

        if (node.getInitializer() != null) {
            builder.append('=').append(node.getInitializer().accept(this));
        }
        return builder.toString();
    }

    @Override
    public String visitFieldSelection(FieldSelectionNode node) {
        return node.getExpression().accept(this) + "." + node.getSelection();
    }

    @Override
    public String visitArrayIndex(ArrayIndexNode node) {
        return node.getExpression().accept(this) + '[' + node.getIndex().accept(this) + ']';
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
        final StringBuilder builder = new StringBuilder();
        builder.append("for(");
        if (node.getInitialization() != null) {
            builder.append(node.getInitialization().accept(this)).append(';');
        }
        if (node.getCondition() != null) {
            builder.append(node.getCondition().accept(this)).append(';');
        }
        if (node.getExpression() != null) {
            builder.append(node.getExpression().accept(this));
        }
        builder.append(')');

        outputBlock(builder, node.getStatement());
        return builder.toString();
    }

    @Override
    public String visitDoWhileIteration(DoWhileIterationNode node) {
        return visitChildren(node);
    }

    @Override
    public String visitFunctionPrototype(FunctionPrototypeNode node) {
        final StringBuilder builder = new StringBuilder();

        builder.append(outputType(node.getReturnType()));
        builder.append(identifierSpacing());
        builder.append(identifier(node.getIdentifier()));

        // output the function arguments
        builder.append('(');
        if (node.getChildCount() > 0) {
            outputChildCSV(builder, node);
        }
        builder.append(')');

        return builder.toString();
    }

    @Override
    public String visitFunctionDefinition(FunctionDefinitionNode node) {
        final StringBuilder builder = new StringBuilder();
        builder.append(node.getFunctionPrototype().accept(this));
        builder.append('{').append(newLine());

        enterScope();
        builder.append(node.getStatements().accept(this));
        exitScope();

        builder.append('}');
        return builder.toString();
    }

    @Override
    public String visitFunctionCall(FunctionCallNode node) {
        if (node.getChildCount() == 0) {
            return identifier(node.getIdentifier()) + "()";
        }

        final StringBuilder builder = new StringBuilder();
        builder.append(identifier(node.getIdentifier()));
        builder.append('(');
        outputChildCSV(builder, node);
        builder.append(')');
        return builder.toString();
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
            return "return";
        }
        return "return " + node.getExpression().accept(this);
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
        final StringBuilder builder = new StringBuilder();

        builder.append("if(");
        builder.append(node.getCondition().accept(this));
        builder.append(')');

        outputBlock(builder, node.getThen());

        if (node.getElse() != null) {
            builder.append("else");
            outputBlock(builder, node.getElse());
        }

        return builder.toString();
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
        return formatNumeric(node.getValue());
    }

    @Override
    public String visitFloat(FloatLeafNode node) {
        return formatNumeric(node.getValue());
    }

    private String formatInt(Numeric numeric) {
        return String.format("%d", (int) numeric.getValue());
    }

    private String formatNumeric(Numeric numeric) {
        if (numeric.hasFraction()) {
            String format = String.format("%%.%df", numeric.getDecimals());
            String result = String.format(Locale.US, format, numeric.getValue());
            if(result.startsWith("0")) {
                return result.substring(1);
            }
            return result;
        }
        if (numeric.isFloat()) {
            return String.format(Locale.US, "%d.", (int)numeric.getValue());
        }
        return String.format(Locale.US, "%d", (int)numeric.getValue());
    }

    private void enterScope() {
        for (int i = 0; i < config.getIndentation(); i++) {
            indentation += " ";
        }
    }

    private void exitScope() {
        int n = config.getIndentation();
        if (n > 0) {
            indentation = indentation.substring(0, indentation.length() - n);
        }
    }

    private boolean noSemiColon(Node node) {
        return node instanceof FunctionDefinitionNode ||
                node instanceof IterationNode ||
                node instanceof ConditionNode;
    }

    private String indentation() {
        return indentation;
    }

    private String newLine() {
        return config.isNewlines() ? "\n" : "";
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

    private void outputBlock(StringBuilder builder, Node node) {
        if (node == null) {
            return;
        }
        if (node instanceof StatementListNode) {
            builder.append('{').append(newLine());

            enterScope();
            builder.append(node.accept(this));
            exitScope();

            builder.append(indentation()).append('}');
        } else {
            enterScope();
            builder.append(node.accept(this));
            exitScope();
        }
    }

    private void outputChildCSV(StringBuilder builder, ParentNode node) {
        for (int i = 0; i < node.getChildCount(); i++) {
            if (i > 0) {
                builder.append(',');
            }
            builder.append(node.getChild(i).accept(this));
        }
    }

    private <T extends ParentNode> String visitChildren(T node) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < node.getChildCount(); i++) {
            builder.append(node.getChild(i).accept(this));
        }
        return builder.toString();
    }
}