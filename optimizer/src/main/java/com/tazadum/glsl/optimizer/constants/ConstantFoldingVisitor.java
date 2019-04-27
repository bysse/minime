package com.tazadum.glsl.optimizer.constants;

import com.tazadum.glsl.ast.ReplacingASTVisitor;
import com.tazadum.glsl.exception.BadImplementationException;
import com.tazadum.glsl.exception.NoSuchFieldException;
import com.tazadum.glsl.exception.SourcePositionException;
import com.tazadum.glsl.exception.TypeException;
import com.tazadum.glsl.language.ast.LeafNode;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.arithmetic.NumericLeafNode;
import com.tazadum.glsl.language.ast.arithmetic.NumericOperationNode;
import com.tazadum.glsl.language.ast.expression.ParenthesisNode;
import com.tazadum.glsl.language.ast.function.FunctionCallNode;
import com.tazadum.glsl.language.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.language.ast.traits.HasNumeric;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.ast.variable.FieldSelectionNode;
import com.tazadum.glsl.language.type.*;
import com.tazadum.glsl.optimizer.Branch;
import com.tazadum.glsl.optimizer.BranchRegistry;
import com.tazadum.glsl.optimizer.OptimizationDecider;
import com.tazadum.glsl.optimizer.OptimizerVisitor;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.TypeCombination;
import com.tazadum.glsl.preprocessor.model.HasToken;
import com.tazadum.glsl.util.SourcePosition;

import java.util.ArrayList;
import java.util.List;

/**
 * Simplifies redundant constant expressions like vector constructions and swizzle operations.
 * Created by Erik on 2016-10-20.
 */
public class ConstantFoldingVisitor extends ReplacingASTVisitor implements OptimizerVisitor {
    private final BranchRegistry branchRegistry;
    private final OptimizationDecider decider;

    private int changes;
    private List<Branch> branches;

    public ConstantFoldingVisitor(ParserContext parserContext, BranchRegistry branchRegistry, OptimizationDecider decider) {
        super(parserContext, true, true);
        this.branchRegistry = branchRegistry;
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
    public List<Branch> getBranches() {
        return branches;
    }

    @Override
    public Node visitFunctionCall(FunctionCallNode node) {
        super.visitFunctionCall(node);

        final FunctionPrototypeNode declarationNode = node.getDeclarationNode();
        if (declarationNode.getPrototype().isBuiltIn()) {
            final String functionName = declarationNode.getIdentifier().original();
            final PredefinedType typeConstructor = HasToken.fromString(functionName, PredefinedType.values());
            if (!TypeCombination.ofAnyCategory(typeConstructor, TypeCategory.Scalar, TypeCategory.Vector)) {
                return null;
            }

            return optimizeVectorConstruction(node, typeConstructor);
        }

        return null;
    }

    @Override
    public Node visitFieldSelection(FieldSelectionNode node) {
        super.visitFieldSelection(node);

        GLSLType expressionType = node.getExpression().getType();
        if (expressionType instanceof PredefinedType) {
            // optimize field selections for predefined types
            PredefinedType type = (PredefinedType) expressionType;
            try {
                VectorField field = new VectorField(type.baseType(), type, node.getSelection());

                if (type.category() == TypeCategory.Vector &&
                        field.components() <= type.components() &&
                        node.getExpression() instanceof FunctionCallNode) {

                    // this could be a constructor call with a field selection
                    final FunctionCallNode functionCall = (FunctionCallNode) node.getExpression();
                    final PredefinedType functionType = HasToken.fromString(functionCall.getIdentifier().original(), PredefinedType.values());

                    if (type == functionType) {
                        // this is a constructor of a basic type
                        if (field.components() == 1) {
                            // extract one of the components and replace the entire node with that
                            Node argument = functionCall.getChild(field.indexOf(0));

                            changes++;
                            return CloneUtils.clone(argument, null);
                        }

                        // TODO: this could be optimized further vec3(1,2,3).xz, but watch out for strange vector
                        // initializations with arguments of different sizes.
                    }

                }

                if (!field.componentsInOrder()) {
                    // if the fields are not in order there's nothing we can do
                    return null;
                }
                if (!expressionType.isAssignableBy(field.getType())) {
                    // the types are not compatible
                    return null;
                }

                // we can remove the entire field selection and replace it with the expression
                changes++;
                return node.getExpression();
            } catch (NoSuchFieldException | TypeException e) {
                // something went wrong, which means that the optimization is invalid
                return null;
            }
        }

        return null;
    }

    private Node optimizeVectorConstruction(FunctionCallNode functionCall, PredefinedType type) {
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
                // check if there are double vector creation ie vec2(vec2(...))
                String parentFunction = functionCall.getIdentifier().original();
                String childFunction = ((FunctionCallNode) child).getIdentifier().original();
                if (parentFunction.equals(childFunction)) {
                    changes++;
                    return child;
                }
            }
        }

        final int components = type.components();
        if (components == 0) {
            throw new BadImplementationException();
        }

        Node parameterSource = null;
        StringBuilder replacementSelection = new StringBuilder();

        // try to find cases where we can optimize vector construction with a swizzle
        int start = -1;
        int rle = 0;
        for (int i = 0; i < components && i < functionCall.getChildCount(); i++) {
            Node child = functionCall.getChild(i);
            if (child instanceof FieldSelectionNode) {
                final Node expression = ((FieldSelectionNode) child).getExpression();

                if (parameterSource == null) {
                    start = i;
                    parameterSource = expression;
                } else if (!parameterSource.equals(expression)) {
                    // the source node is different for some of the parameters
                    final String selection = replacementSelection.toString();
                    replacementSelection = new StringBuilder();

                    if (rle > 1) {
                        // check if we can replace some of the parameters with a swizzle
                        Node clonedSource = CloneUtils.clone(parameterSource, null);
                        Node node = createReplacementNode(parameterSource.getSourcePosition(), clonedSource, type.baseType(), rle, selection);


                        if (node != null) {
                            // only a few arguments can be replaced
                            changes++;

                            // replace and dereference the node that is at position start
                            Node firstNode = functionCall.getChild(start);
                            functionCall.replaceChild(firstNode, node);
                            parserContext.dereferenceTree(firstNode);

                            // reference the added node
                            parserContext.referenceTree(node);

                            for (int j = 0; j < rle - 1; j++) {
                                // dereference the node
                                Node nodeToRemove = functionCall.getChild(start + 1);
                                functionCall.removeChild(nodeToRemove); // it's a list of nodes
                                parserContext.dereferenceTree(nodeToRemove);
                            }
                            i = start;
                        }

                        start = -1;
                        rle = 0;
                        parameterSource = null;
                        continue;
                    }

                    // the run length wasn't long enough to bother with a replacement, just start a new RLE.
                    start = i;
                    rle = 0;
                    parameterSource = expression;
                }

                replacementSelection.append(((FieldSelectionNode) child).getSelection());
                rle++;
                continue;
            }
            break;
        }

        if (parameterSource == null || rle <= 1 || start < 0) {
            // this could happen if there are other nodes than field selections in the constructor
            return null;
        }

        Node clonedSource = CloneUtils.clone(parameterSource, null);
        Node node = createReplacementNode(functionCall.getSourcePosition(), clonedSource, type.baseType(), rle, replacementSelection.toString());
        if (start == 0) {
            // the entire node can be replaced
            changes++;
            return node;
        }

        // only a few arguments can be replaced
        for (int i = 0; i < rle; i++) {
            Node nodeToRemove = functionCall.getChild(start);
            functionCall.removeChild(nodeToRemove); // it's a list of nodes
            parserContext.dereferenceTree(nodeToRemove);
        }

        functionCall.addChild(node);
        parserContext.referenceTree(node);
        changes++;
        return null;
    }

    private Node createReplacementNode(SourcePosition position, Node swizzleSource, PredefinedType constructorBaseType, int components, String selection) {
        final GLSLType glslSourceType = swizzleSource.getType();
        if (!(glslSourceType instanceof PredefinedType)) {
            // the field selection was for a custom type, not much to do.
            return null;
        }

        final PredefinedType swizzleSourceType = (PredefinedType) glslSourceType;

        if (constructorBaseType == null || !constructorBaseType.isAssignableBy(swizzleSourceType.baseType())) {
            // the base type of the source node and constructor node are different and can't be used together
            return null;
        }

        try {
            final VectorField field = new VectorField(constructorBaseType, swizzleSourceType, selection);
            if (field.componentsInOrder() && field.components() == swizzleSourceType.components()) {
                // replace the entire swizzle construction with the swizzle source
                return swizzleSource;
            }

            // we can remove the entire field selection and replace it with a swizzle
            FieldSelectionNode replacement = new FieldSelectionNode(position, field.render(VectorField.DEFAULT_SET));
            replacement.setExpression(swizzleSource); // this will disconnect the sub-tree from it's previous parent

            return replacement;
        } catch (NoSuchFieldException e) {
            // something went wrong, which means that the optimization is invalid
            return null;
        }
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
     *
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

        Numeric numeric = null;

        try {
            switch (node.getOperator()) {
                case ADD:
                    numeric = NumericOperation.add(left, right);
                    break;
                case SUB:
                    numeric = NumericOperation.sub(left, right);
                    break;
                case MUL:
                    numeric = NumericOperation.mul(left, right);
                    break;
                case DIV:
                    numeric = NumericOperation.div(left, right);
                    break;
                case MOD:
                    numeric = NumericOperation.mod(left, right);
                    break;
            }
        } catch (TypeException e) {
            throw new SourcePositionException(node, e.getMessage(), e);
        }

        Node result = new NumericLeafNode(node.getSourcePosition(), numeric);

        final int score = decider.score(result);
        if (score <= previousScore) {
            changes++;
            return result;
        }

        if (score <= previousScore * 1.5f) {
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
