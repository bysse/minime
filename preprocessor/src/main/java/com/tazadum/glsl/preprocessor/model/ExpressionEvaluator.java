package com.tazadum.glsl.preprocessor.model;

import com.tazadum.glsl.preprocessor.PreprocessorException;
import com.tazadum.glsl.preprocessor.language.Expression;
import com.tazadum.glsl.preprocessor.language.ast.expression.*;

import static com.tazadum.glsl.preprocessor.model.BoolIntLogic.*;

/**
 * Evaluates an expression assuming all macro substitutions already has been made.
 */
public class ExpressionEvaluator implements Expression.Visitor {
    private MacroRegistry registry;

    public ExpressionEvaluator(MacroRegistry registry) {
        this.registry = registry;
    }

    @Override
    public int visit(IntegerNode node) {
        return node.getValue();
    }

    @Override
    public int visit(IdentifierNode node) {
        throw new PreprocessorException(node.getSourcePosition(), "Unknown symbol " + node.getIdentifier());
    }

    @Override
    public int visit(DefinedNode node) {
        return registry.isDefined(node.getIdentifier()) ? TRUE : FALSE;
    }

    @Override
    public int visit(UnaryExpressionNode node) {
        final int value = node.getExpression().accept(this);

        switch (node.getOperator()) {
            case PLUS:
                return value;
            case NEGATE:
                return -value;
            case BITWISE_NOT:
                return ~value;
            case NOT:
                return logicNot(value);
            default:
                throw new UnsupportedOperationException("Unsupported operator " + node.getOperator());
        }
    }

    @Override
    public int visit(BinaryExpressionNode node) {
        final int left = node.getLeft().accept(this);
        final int right = node.getRight().accept(this);

        switch (node.getOperator()) {
            case MULTIPLY:
                return left * right;
            case DIVIDE:
                return left / right;
            case MOD:
                return left % right;
            case ADD:
                return left + right;
            case SUBTRACT:
                return left - right;
            case BITWISE_OR:
                return left | right;
            case LEFT_SHIFT:
                return left << right;
            case RIGHT_SHIFT:
                return left >> right;
            case BITWISE_AND:
                return left & right;
            case BITWISE_XOR:
                return left ^ right;
            default:
                throw new UnsupportedOperationException("Unsupported operator " + node.getOperator());
        }
    }

    @Override
    public int visit(RelationalExpressionNode node) {
        final int left = node.getLeft().accept(this);
        final int right = node.getRight().accept(this);

        switch (node.getOperator()) {
            case EQUALS:
                return toInt(left == right);
            case LESS_THAN:
                return toInt(left < right);
            case NOT_EQUALS:
                return toInt(left != right);
            case GREATER_THAN:
                return toInt(left > right);
            case LESS_THAN_EQUALS:
                return toInt(left <= right);
            case GREATER_THAN_EQUALS:
                return toInt(left >= right);
            default:
                throw new UnsupportedOperationException("Unsupported operator " + node.getOperator());
        }
    }

    @Override
    public int visit(OrExpressionNode node) {
        if (isTrue(node.getLeft().accept(this))) {
            return TRUE;
        }
        return node.getRight().accept(this);
    }

    @Override
    public int visit(AndExpressionNode node) {
        final int left = node.getLeft().accept(this);
        final int right = node.getRight().accept(this);
        return toInt(isTrue(left) & isTrue(right));
    }
}
