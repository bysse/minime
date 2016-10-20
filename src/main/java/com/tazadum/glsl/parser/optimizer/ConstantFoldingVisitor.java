package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.HasNumeric;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ParenthesisNode;
import com.tazadum.glsl.ast.ReplacingASTVisitor;
import com.tazadum.glsl.ast.arithmetic.FloatLeafNode;
import com.tazadum.glsl.ast.arithmetic.IntLeafNode;
import com.tazadum.glsl.ast.arithmetic.NumericOperationNode;
import com.tazadum.glsl.language.BuiltInType;
import com.tazadum.glsl.language.Numeric;

/**
 * Created by Erik on 2016-10-20.
 */
public class ConstantFoldingVisitor extends ReplacingASTVisitor {
    private int changes = 0;

    public int getChanges() {
        return changes;
    }

    @Override
    public Node visitParenthesis(ParenthesisNode node) {
        super.visitParenthesis(node);
        final Node expression = node.getExpression();
        if (expression instanceof ParenthesisNode) {
            changes++;
            return expression;
        }
        return null;
    }

    @Override
    public Node visitNumericOperation(NumericOperationNode node) {
        super.visitNumericOperation(node);

        final Numeric left = isConstant(node.getLeft());
        final Numeric right = isConstant(node.getRight());

        if (left == null || right == null) {
            return null;
        }

        if (left.getValue() == 0.0 || right.getValue() == 0.0) {
            // replace node with 0
            changes++;
            if (BuiltInType.INT == node.getType()) {
                return new IntLeafNode(new Numeric(0.0, 0, false));
            }
            return new FloatLeafNode(new Numeric(0.0, 0, true));
        }
        if (left.getValue() == 1.0) {
            // replace node with 'right' node
            changes++;
            return node.getRight();
        }

        if (right.getValue() == 1.0) {
            // replace node with 'left' node
            changes++;
            return node.getLeft();
        }

        return null;
    }

    private Numeric isConstant(Node node) {
        if (node instanceof HasNumeric) {
            return ((HasNumeric) node).getValue();
        }
        return null;
    }
}
