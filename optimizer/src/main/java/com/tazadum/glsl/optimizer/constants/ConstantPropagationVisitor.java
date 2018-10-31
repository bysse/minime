package com.tazadum.glsl.optimizer.constants;

import com.tazadum.glsl.ast.ReplacingASTVisitor;
import com.tazadum.glsl.exception.BadImplementationException;
import com.tazadum.glsl.language.ast.Identifier;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.arithmetic.NumericOperationNode;
import com.tazadum.glsl.language.ast.expression.ParenthesisNode;
import com.tazadum.glsl.language.ast.function.FunctionCallNode;
import com.tazadum.glsl.language.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.language.ast.iteration.ForIterationNode;
import com.tazadum.glsl.language.ast.traits.HasConstState;
import com.tazadum.glsl.language.ast.traits.HasNumeric;
import com.tazadum.glsl.language.ast.traits.IterationNode;
import com.tazadum.glsl.language.ast.util.NodeFinder;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationListNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.ast.variable.VariableNode;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.language.model.NumericOperator;
import com.tazadum.glsl.language.variable.VariableRegistry;
import com.tazadum.glsl.optimizer.BranchRegistry;
import com.tazadum.glsl.optimizer.OptimizationDecider;
import com.tazadum.glsl.optimizer.OptimizerVisitor;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.Usage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Propagates constants and resolves constant expressions.
 * Created by Erik on 2016-10-29.
 */
public class ConstantPropagationVisitor extends ReplacingASTVisitor implements OptimizerVisitor {
    private final Logger logger = LoggerFactory.getLogger(ConstantPropagationVisitor.class);
    private final OptimizationDecider decider;
    private int changes = 0;

    public ConstantPropagationVisitor(ParserContext parserContext, BranchRegistry branchRegistry, OptimizationDecider decider) {
        super(parserContext, true, false);
        this.decider = decider;
    }

    public void reset() {
        this.changes = 0;
    }

    public int getChanges() {
        return changes;
    }

    @Override
    public Node visitVariableDeclaration(VariableDeclarationNode node) {
        super.visitVariableDeclaration(node);

        if (node.getArraySpecifiers() == null || !node.getArraySpecifiers().isEmpty()) {
            // ignore array declarations
            return null;
        }

        if (node.getInitializer() == null) {
            // ignore variable declarations that doesn't have an initializer
            return null;
        }

        if (!isConstant(node)) {
            // the variable declaration is not a constant
            return null;
        }

        final VariableRegistry variableRegistry = parserContext.getVariableRegistry();
        final Usage<VariableDeclarationNode> nodeUsage = variableRegistry.resolve(node);
        final List<Node> usageNodes = nodeUsage.getUsageNodes();

        if (isDestroyingOptimization(node, usageNodes)) {
            // the propagation would most likely destroy a manual loop-optimization
            return null;
        }

        if (!isWorthIt(node, usageNodes)) {
            // the propagation would be bigger than the current declaration
            return null;
        }

        logger.trace("Propagating declaration of {}", node.getIdentifier().original());

        final Set<Node> deferredDeRef = new TreeSet<>();

        for (Node usageNode : usageNodes) {
            final VariableNode variableNode = (VariableNode) usageNode;
            if (NodeFinder.isMutated(variableNode)) {
                // one of the usage nodes is being mutated or is closely part of a mutating operation
                // the safest action here is to abort the entire propagation of the value.
                // note that this should be checked by isConstant
                throw new BadImplementationException();
            }

            // we need to clone the initializer because it's going to be insert in multiple places in the AST
            Node initializer = node.getInitializer().clone(null);

            final ParentNode parentNode = variableNode.getParentNode();
            if (initializerNeedWrapping(initializer, parentNode)) {
                initializer = new ParenthesisNode(variableNode.getSourcePosition(), initializer);
            }

            deferredDeRef.add(variableNode);
            parentNode.replaceChild(variableNode, initializer);

            // make sure all new nodes are registered properly
            parserContext.referenceTree(initializer);
            changes++;
        }

        for (Node deRef : deferredDeRef) {
            parserContext.dereferenceTree(deRef);
        }

        return ReplacingASTVisitor.REMOVE;
    }

    private boolean initializerNeedWrapping(Node initializer, Node parentNode) {
        if (parentNode != null && parentNode instanceof FunctionCallNode) {
            return false;
        }
        if (initializer instanceof NumericOperationNode) {
            return ((NumericOperationNode) initializer).getOperator() != NumericOperator.MUL;
        }
        return false;
    }

    /**
     * Check if the variable is declared within a for loop and/or its usage is inside the loop.
     * Breaking out some calculations outside of a loop is pretty regular optimization strategy.
     */
    private boolean isDestroyingOptimization(VariableDeclarationNode node, List<Node> usageNodes) {
        final Node sourceIterationNode = NodeFinder.findParent(node, (n) -> n instanceof IterationNode);

        if (sourceIterationNode == null) {
            // the declaration was not in any loop
            for (Node usage : usageNodes) {
                if (NodeFinder.findParent(usage, (n) -> n instanceof IterationNode) != null) {
                    // if a usage node is  inside a loop, don't propagate the constant
                    return true;
                }
            }
            return false;
        }

        // the declaration was in a loop, make sure all usages is in the same loop
        for (Node usage : usageNodes) {
            if (!sourceIterationNode.equals(NodeFinder.findParent(usage, (n) -> n instanceof IterationNode))) {
                // the usage node is inside another loop, don't propagate the constant
                return true;
            }
        }

        return false;
    }

    private boolean isWorthIt(VariableDeclarationNode node, List<Node> usageNodes) {
        // create a separate tree for this declaration to include the type declaration when determining size
        final VariableDeclarationListNode listNode = new VariableDeclarationListNode(null, node.getFullySpecifiedType());
        listNode.addChild(node.clone(listNode));

        // assume we can get a single character variable identifier + space
        final int identifierSize = 1 + 1;

        final int valueScore = decider.score(node.getInitializer()) + (initializerNeedWrapping(node.getInitializer(), listNode) ? 2 : 0);
        final int declarationScore = decider.score(listNode) + identifierSize;

        return valueScore * usageNodes.size() < declarationScore;
    }

    private boolean isConstant(VariableDeclarationNode node) {
        if (node.isConstant()) {
            return true;
        }

        if (node.getParentNode().getParentNode() instanceof ForIterationNode) {
            // disregard all variables declared in the for-statement
            return false;
        }

        if (node.getInitializer() instanceof HasNumeric) {
            return true;
        }
        if (node.getInitializer() instanceof HasConstState) {
            if (((HasConstState) node.getInitializer()).isConstant()) {
                // if the initializer has been marked const during an earlier phase we still consider it a constant
                return true;
            }
        }

        final Set<FunctionCallNode> functionCalls = NodeFinder.findAll(node.getInitializer(), FunctionCallNode.class);
        final Set<VariableNode> variables = NodeFinder.findAll(node.getInitializer(), VariableNode.class);

        if (functionCalls.isEmpty() && variables.isEmpty()) {
            // it's a good sign if the initializer contains no function calls or variables
            return true;
        }

        for (FunctionCallNode functionCall : functionCalls) {
            final FunctionPrototypeNode declarationNode = functionCall.getDeclarationNode();
            if (!declarationNode.getPrototype().isBuiltIn()) {
                if (declarationNode.mutatesGlobalState()) {
                    // if the function mutates global state abort
                    return false;
                }
            }
        }

        final VariableRegistry variableRegistry = parserContext.getVariableRegistry();
        for (VariableNode variableNode : variables) {
            Usage<VariableDeclarationNode> nodeUsage = variableRegistry.resolve(variableNode.getDeclarationNode());
            for (Node usage : nodeUsage.getUsageNodes()) {
                if (NodeFinder.isMutated(usage)) {
                    // the variable is mutated somewhere don't propagate it
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public Node visitVariableDeclarationList(VariableDeclarationListNode node) {
        super.visitVariableDeclarationList(node);

        if (node.getChildCount() == 0) {
            // remove empty declaration lists
            changes++;
            return ReplacingASTVisitor.REMOVE;
        }

        return null;
    }

    private static class NodeUsage {
        Identifier identifier;
        Node node;
        GLSLContext context;

        NodeUsage(GLSLContext context, Identifier identifier, Node node) {
            this.identifier = identifier;
            this.node = node;
            this.context = context;
        }
    }
}
