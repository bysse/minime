package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.*;
import com.tazadum.glsl.ast.arithmetic.FloatLeafNode;
import com.tazadum.glsl.ast.arithmetic.IntLeafNode;
import com.tazadum.glsl.ast.arithmetic.NumericOperationNode;
import com.tazadum.glsl.ast.arithmetic.UnaryOperationNode;
import com.tazadum.glsl.language.*;

/**
 * Created by Erik on 2016-10-20.
 */
public class ConstantFoldingVisitor extends ReplacingASTVisitor {
    private final OptimizationDecider decider;
    private int changes = 0;

    public ConstantFoldingVisitor(OptimizationDecider decider) {
        this.decider = decider;
    }

    public void reset() {
        this.changes = 0;
    }

    public int getChanges() {
        return changes;
    }

    @Override
    public Node visitParenthesis(ParenthesisNode node) {
        super.visitParenthesis(node);
        final Node expression = node.getExpression();
        if (expression instanceof ParenthesisNode || expression instanceof LeafNode) {
            changes++;
            return expression;
        }
        return null;
    }

    @Override
    public Node visitNumericOperation(NumericOperationNode node) {
        super.visitNumericOperation(node);

        // if the node implementation has equals implemented, we can detect total elimination of expressions
        if (node.getOperator() == NumericOperator.DIV && node.getLeft().equals(node.getRight())) {
            changes++;
            return createConstant(node.getType(), 1);
        }

        final Numeric left = getNumeric(node.getLeft());
        final Numeric right = getNumeric(node.getRight());

        Node replacement = null;
        switch (node.getOperator()) {
            case MUL:
                replacement = optimizeMul(node, left, right);
                break;
            case DIV:
                replacement = optimizeDiv(node, left, right);
                break;
            case ADD:
                replacement = optimizeAdd(node, left, right);
                break;
            case SUB:
                replacement = optimizeSub(node, left, right);
                break;
        }

        if (replacement != null) {
            return replacement;
        }

        if (left != null && right != null) {
            return evaluate(node, left, right);
        }

        return null;
    }

    private Node evaluate(NumericOperationNode node, Numeric left, Numeric right) {
        final int previousScore = decider.score(node);

        int decimals = 0;
        double value = 0;

        switch (node.getOperator()) {
            case ADD:
                value = left.getValue() + right.getValue();
                decimals = Math.max(left.getDecimals(), right.getDecimals());
                break;
            case SUB:
                value = left.getValue() - right.getValue();
                decimals = Math.max(left.getDecimals(), right.getDecimals());
                break;
            case MUL:
                value = left.getValue() * right.getValue();
                decimals = left.getDecimals() + right.getDecimals();
                break;
            case DIV:
                value = left.getValue() / right.getValue();
                double reminder = left.getValue() % right.getValue();
                if (reminder == 0.0) {
                    decimals = 0;
                } else {
                    decimals = Math.max(2, left.getDecimals() + right.getDecimals());
                }
                break;
        }

        final Numeric numeric = new Numeric(value, decimals, decimals != 0);

        Node result;
        if (numeric.isFloat()) {
            result = new FloatLeafNode(numeric);
        } else {
            result = new IntLeafNode(numeric);
        }

        final int score = decider.score(result);

        if (score <= previousScore) {
            changes++;
            return result;
        }
        return null;
    }

    private Node optimizeMul(NumericOperationNode node, Numeric left, Numeric right) {
        if ((left != null && left.getValue() == 0.0) || (right != null && right.getValue() == 0.0)) {
            // replace node with 0
            changes++;
            return createConstant(node.getType(), 0);
        }
        if (left != null && left.getValue() == 1.0) {
            // replace node with 'right' node
            changes++;
            return node.getRight();
        }
        if (right != null && right.getValue() == 1.0) {
            // replace node with 'left' node
            changes++;
            return node.getLeft();
        }
        return null;
    }

    private Node optimizeDiv(NumericOperationNode node, Numeric left, Numeric right) {
        if (right != null && right.getValue() == 1.0) {
            changes++;
            return node.getLeft();
        }
        if (left != null && left.getValue() == 0.0) {
            changes++;
            return createConstant(node.getType(), 0);
        }
        if (left != null && right != null && left.equals(right)) {
            changes++;
            return createConstant(node.getType(), 1);
        }
        return null;
    }

    private Node optimizeAdd(NumericOperationNode node, Numeric left, Numeric right) {
        if (left != null && left.getValue() == 0.0) {
            // replace node with 'right' node
            changes++;
            return node.getRight();
        }
        if (right != null && right.getValue() == 0.0) {
            // replace node with 'left' node
            changes++;
            return node.getLeft();
        }
        return null;
    }

    private Node optimizeSub(NumericOperationNode node, Numeric left, Numeric right) {
        if (right != null && right.getValue() == 0.0) {
            // replace node with 'left' node
            changes++;
            return node.getLeft();
        }
        if (left != null && left.getValue() == 0.0) {
            // replace node with 'right' node
            changes++;
            final UnaryOperationNode replacement = new UnaryOperationNode(UnaryOperator.MINUS);
            replacement.setExpression(node.getRight());
            return replacement;
        }
        return null;
    }

    private Numeric getNumeric(Node node) {
        if (node instanceof HasNumeric) {
            return ((HasNumeric) node).getValue();
        }
        return null;
    }

    private LeafNode createConstant(GLSLType type, int value) {
        if (BuiltInType.INT == type) {
            return new IntLeafNode(new Numeric(value, 0, false));
        }
        return new FloatLeafNode(new Numeric(value, 0, true));
    }
}
