package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.*;
import com.tazadum.glsl.ast.arithmetic.FloatLeafNode;
import com.tazadum.glsl.ast.arithmetic.IntLeafNode;
import com.tazadum.glsl.ast.arithmetic.NumericOperationNode;
import com.tazadum.glsl.ast.arithmetic.UnaryOperationNode;
import com.tazadum.glsl.ast.function.FunctionCallNode;
import com.tazadum.glsl.ast.variable.FieldSelectionNode;
import com.tazadum.glsl.ast.variable.VariableNode;
import com.tazadum.glsl.language.*;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.finder.NodeFinder;

import java.util.Set;

/**
 * Created by Erik on 2016-10-20.
 */
public class ConstantFoldingVisitor extends ReplacingASTVisitor implements OptimizerVisitor {
    private final String[] validSelection = {"xrs", "ygt", "zbp", "waq"};

    private final OptimizationDecider decider;
    private int changes = 0;

    public ConstantFoldingVisitor(ParserContext parserContext, OptimizationDecider decider) {
        super(parserContext, false);
        this.decider = decider;
    }

    public void reset() {
        this.changes = 0;
    }

    public int getChanges() {
        return changes;
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
                case "abs":
                    return optimizeAbs(node);
            }
        }

        return null;
    }

    private Node optimizeAbs(FunctionCallNode node) {
        Node expression = node.getChild(0);
        if (!isConstant(expression)) {
            return null;
        }

        // TODO: evaluate the expression

        return null;
    }

    private boolean isConstant(Node node) {
        final Set<FunctionCallNode> functionCalls = NodeFinder.findAll(node, FunctionCallNode.class);
        final Set<VariableNode> variables = NodeFinder.findAll(node, VariableNode.class);

        return functionCalls.isEmpty() && variables.isEmpty();
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
                }
                return null;
            }

            FieldSelectionNode replacement = new FieldSelectionNode(replacementSelection);
            replacement.setExpression(target);
            changes++;
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

        // if the node implementation has equals implemented, we can detect total elimination of expressions
        if (node.getOperator() == NumericOperator.DIV && node.getLeft().equals(node.getRight())) {
            changes++;
            parserContext.dereferenceTree(node);
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
            changes++;
            return replacement;
        }

        if (left != null && right != null) {
            return evaluate(node, left, right);
        }

        // all simple cases are exhausted, search for down the chain
        if (right != null && hasOperation(node.getOperator(), node.getLeft())) {
            Node result = handleOperation(node, (NumericOperationNode) node.getLeft(), left, right);
            if (result != null) {
                return result;
            }
        }

        if (left != null && hasOperation(node.getOperator(), node.getRight())) {
            Node result = handleOperation(node, (NumericOperationNode) node.getRight(), left, right);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    private Node handleOperation(NumericOperationNode node, NumericOperationNode child, Numeric left, Numeric right) {
        final Numeric childLeft = getNumeric(child.getLeft());
        final Numeric childRight = getNumeric(child.getRight());

        // pull down mAny constant
        if (childLeft != null) {
            final Node leftClone = child.getLeft().clone(null);
            final Node rightClone = node.getRight().clone(null);
            final NumericOperationNode dummyNode = new NumericOperationNode(node.getOperator(), leftClone, rightClone);

            if (right == null) {
                return null;
            }
            final Node result = evaluate(dummyNode, childLeft, right);
            if (result != dummyNode) {
                changes++;
                return new NumericOperationNode(node.getOperator(), result, child.getRight());
            }
        } else if (childRight != null) {
            // pull down constant
            final Node leftClone = node.getLeft().clone(null);
            final Node rightClone = child.getRight().clone(null);
            final NumericOperationNode dummyNode = new NumericOperationNode(node.getOperator(), leftClone, rightClone);

            if (left == null) {
                return null;
            }
            final Node result = evaluate(dummyNode, left, childRight);
            if (result != dummyNode) {
                changes++;
                return new NumericOperationNode(node.getOperator(), result, child.getLeft());
            }
        }
        return null;
    }

    private boolean hasOperation(NumericOperator operator, Node node) {
        if (node instanceof NumericOperationNode) {
            final NumericOperationNode child = (NumericOperationNode) node;
            return child.getOperator() == operator && (child.getLeft() instanceof HasNumeric || child.getRight() instanceof HasNumeric);
        }
        return false;
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
            parserContext.dereferenceTree(node);
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
            parserContext.dereferenceTree(node.getRight());
            return createConstant(node.getType(), 0);
        }
        if (left != null && right != null && left.equals(right)) {
            changes++;
            parserContext.dereferenceTree(node);
            return createConstant(node.getType(), 1);
        }
/*
        if (hasOperation(NumericOperator.ADD, node.getLeft()) ||
                hasOperation(NumericOperator.SUB, node.getLeft()) ||
                hasOperation(NumericOperator.DIV, node.getLeft()) ||
                hasOperation(NumericOperator.ADD, node.getRight()) ||
                hasOperation(NumericOperator.SUB, node.getRight())) {
            // can't simplify the expression without other simplifications
            return null;
        }

        if (right != null) {
            // find a nNumeric leaf node in the left hand side
            Set<HasNumeric> nodes = NodeFinder.findAll(node.getLeft(), HasNumeric.class);
            if (nodes.isEmpty()) {
                return null;
            }

            HasNumeric first = nodes.iterator().next();
            Numeric leftNumeric = first.getValue();

            // evaluate the result
            int decimals = 0;
            double value = leftNumeric.getValue() / right.getValue();
            double reminder = leftNumeric.getValue() % right.getValue();
            if (reminder == 0.0) {
                decimals = 0;
            } else {
                decimals = Math.max(2, leftNumeric.getDecimals() + right.getDecimals());
            }
            Numeric result = new Numeric(value, decimals, decimals != 0);

            // switch out the nodes
            NumericOperationNode clonedRoot = node.clone(null);
            NumericOperationNode stage2 = ReplaceUtil.replaceNumeric(parserContext, clonedRoot, (LeafNode) first, result);
            NumericOperationNode stage3 = ReplaceUtil.removeNumeric(parserContext, stage2, (LeafNode)node.getRight());

            if (stage3.getRight() == null) {
                return stage3.getLeft();
            }
            return stage3;
        }
*/
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
