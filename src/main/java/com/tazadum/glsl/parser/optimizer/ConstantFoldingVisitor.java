package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.*;
import com.tazadum.glsl.ast.arithmetic.FloatLeafNode;
import com.tazadum.glsl.ast.arithmetic.IntLeafNode;
import com.tazadum.glsl.ast.arithmetic.NumericOperationNode;
import com.tazadum.glsl.ast.function.FunctionCallNode;
import com.tazadum.glsl.ast.variable.FieldSelectionNode;
import com.tazadum.glsl.language.BuiltInType;
import com.tazadum.glsl.language.Numeric;
import com.tazadum.glsl.parser.ParserContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erik on 2016-10-20.
 */
public class ConstantFoldingVisitor extends ReplacingASTVisitor implements OptimizerVisitor {
    private final String[] validSelection = {"xrs", "ygt", "zbp", "waq"};

    private final BranchRegistry branchRegistry;
    private final OptimizationDecider decider;

    private int changes;
    private List<OptimizerBranch> branches;

    public ConstantFoldingVisitor(ParserContext parserContext, OptimizationDecider decider) {
        super(parserContext, false);
        this.branchRegistry = parserContext.getBranchRegistry();
        this.decider = decider;

        reset();
    }

    public void reset() {
        this.firstNode = null;
        this.changes = 0;
        this.branches = new ArrayList<>();
    }

    public int getChanges() {
        return changes;
    }

    @Override
    public List<OptimizerBranch> getBranches() {
        return branches;
    }

    @Override
    public Node visitFunctionCall(FunctionCallNode node) {
        super.visitFunctionCall(node);

        if (node.getDeclarationNode().getPrototype().isBuiltIn()) {
            switch (node.getDeclarationNode().getIdentifier().original()) {
                case "vec2":
                    return optimizeVectorConstruction(node, BuiltInType.VEC2);
                case "vec3":
                    return optimizeVectorConstruction(node, BuiltInType.VEC3);
                case "vec4":
                    return optimizeVectorConstruction(node, BuiltInType.VEC4);
            }
        }

        return null;
    }

    @Override
    public Node visitFieldSelection(FieldSelectionNode node) {
        super.visitFieldSelection(node);

        if (node.getType() == node.getExpression().getType()) {
            // verify the selection order
            for (int i = 0; i < node.getSelection().length(); i++) {
                if (validSelection[i].indexOf(node.getSelection().charAt(i)) < 0) {
                    return null;
                }
            }
            changes++;
            return node.getExpression();
        }

        return null;
    }

    private Node optimizeVectorConstruction(FunctionCallNode functionCall, BuiltInType type) {
        if (functionCall.getChildCount() == 0) {
            return null;
        }
        if (functionCall.getChildCount() == 1) {
            Node child = functionCall.getChild(0);
            if (type.equals(child.getType())) {
                // the argument to the vector constructor has the same type
                changes++;
                return child;
            }
            if (child instanceof FunctionCallNode) {
                String childFunction = ((FunctionCallNode) child).getIdentifier().original();
                if (functionCall.getIdentifier().original().equals(childFunction)) {
                    changes++;
                    return child;
                }
            }
        }
        final int elements = BuiltInType.elements(type);
        if (functionCall.getChildCount() == elements) {
            Node target = null;
            boolean inOrder = true;
            String replacementSelection = "";

            for (int i = 0; i < elements; i++) {
                Node child = functionCall.getChild(i);
                if (child instanceof FieldSelectionNode) {
                    Node expression = ((FieldSelectionNode) child).getExpression();
                    if (target == null) {
                        target = expression;
                    } else if (!target.equals(expression)) {
                        return null;
                    }

                    String selection = ((FieldSelectionNode) child).getSelection();
                    if (validSelection[i].contains(selection)) {
                        replacementSelection += validSelection[i].charAt(0);
                        continue;
                    }
                    // TODO: improve
                    replacementSelection += selection;
                    inOrder = false;
                    continue;
                }
                return null;
            }

            changes++;

            if (inOrder) {
                return target;
            }

            FieldSelectionNode replacement = new FieldSelectionNode(replacementSelection);
            replacement.setExpression(target);
            return replacement;
        }

        return null;
    }

    @Override
    public Node visitParenthesis(ParenthesisNode node) {
        super.visitParenthesis(node);
        final Node expression = node.getExpression();
        if (expression instanceof ParenthesisNode || expression instanceof LeafNode) {
            changes++;
            return expression;
        }

        ParentNode parentNode = node.getParentNode();
        if (parentNode instanceof FunctionCallNode) {
            changes++;
            return expression;
        }

        return null;
    }

    @Override
    public Node visitNumericOperation(NumericOperationNode node) {
        super.visitNumericOperation(node);

        final Numeric left = getNumeric(node.getLeft());
        final Numeric right = getNumeric(node.getRight());

        if (left != null && right != null) {
            return evaluate(node, left, right);
        }

        return null;
    }

    /**
     * Evaluate a numeric operation.
     * @param node
     * @param left
     * @param right
     * @return
     */
    private Node evaluate(NumericOperationNode node, Numeric left, Numeric right) {
        if (!branchRegistry.claimPoint(node, ConstantFolding.class)) {
            // this node has already been considered for optimization
            return null;
        }

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

        if (score <= previousScore*1.5f) {
            // the optimization is slightly bigger so let's branch
            changes++;
            branches.add(createBranch());
            return result;
        }

        return null;
    }

    private Numeric getNumeric(Node node) {
        if (node instanceof HasNumeric) {
            return ((HasNumeric) node).getValue();
        }
        return null;
    }
}
