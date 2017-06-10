package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.*;
import com.tazadum.glsl.ast.arithmetic.NumericOperationNode;
import com.tazadum.glsl.ast.function.FunctionCallNode;
import com.tazadum.glsl.ast.function.FunctionDefinitionNode;
import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.ast.iteration.ForIterationNode;
import com.tazadum.glsl.ast.variable.VariableDeclarationListNode;
import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.ast.variable.VariableNode;
import com.tazadum.glsl.language.NumericOperator;
import com.tazadum.glsl.language.TypeQualifier;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.Usage;
import com.tazadum.glsl.parser.finder.NodeFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Erik on 2016-10-29.
 */
public class ConstantPropagationVisitor extends ReplacingASTVisitor {
    private final Logger logger = LoggerFactory.getLogger(ConstantPropagationVisitor.class);
    private final OptimizationDecider decider;
    private int changes = 0;

    public ConstantPropagationVisitor(ParserContext parserContext, OptimizationDecider decider) {
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
    public Node visitVariableDeclaration(VariableDeclarationNode node) {
        super.visitVariableDeclaration(node);

        if (node.getArraySpecifier() != null || node.getInitializer() == null) {
            return null;
        }

        if (isConstant(node)) {
            final Usage<VariableDeclarationNode> nodeUsage = parserContext.getVariableRegistry().resolve(node);
            final List<Node> usageNodes = nodeUsage.getUsageNodes();

            if (isDestroyingOptimization(node, usageNodes)) {
                return null;
            }

            if (usageNodes.size() == 1 || isWorthIt(node, usageNodes)) {

                List<NodeUsage> newNodeUsage = new ArrayList<>();
                boolean firstNode = true;

                for (Node usageNode : usageNodes) {
                    final VariableNode usage = (VariableNode) usageNode;
                    if (usage.getParentNode() instanceof MutatingOperation) {
                        return null;
                    }

                    Node initializer = node.getInitializer();

                    // The first usage node can be ignored because the references just moves
                    if (!firstNode) {
                        GLSLContext usageContext = parserContext.findContext(usageNode);

                        // we need to clone the initializer because it's going to be insert in multiple places in the AST
                        initializer = initializer.clone(null);

                        // find all variables
                        newNodeUsage.addAll(NodeFinder.findAll(initializer, VariableNode.class).stream()
                                .map(variableNode -> new NodeUsage(usageContext, variableNode.getDeclarationNode().getIdentifier(), variableNode))
                                .collect(Collectors.toList()));

                        // find all functions
                        newNodeUsage.addAll(NodeFinder.findAll(initializer, FunctionCallNode.class).stream()
                                .map(functionCallNode -> new NodeUsage(usageContext, functionCallNode.getDeclarationNode().getIdentifier(), functionCallNode))
                                .collect(Collectors.toList()));
                    }
                    firstNode = false;

                    if (initializerNeedWrapping(initializer)) {
                        initializer = new ParenthesisNode(initializer);
                    }

                    usage.getParentNode().replaceChild(usage, initializer);

                    changes++;
                }

                for (NodeUsage usage : newNodeUsage) {
                    parserContext.getVariableRegistry().usage(usage.context, usage.identifier.original(), usage.node);
                }

                parserContext.getVariableRegistry().dereference(node);

                return ReplacingASTVisitor.REMOVE;
            }
        }

        return null;
    }

    private boolean initializerNeedWrapping(Node initializer) {
        if (initializer instanceof NumericOperationNode){
            return ((NumericOperationNode) initializer).getOperator() != NumericOperator.MUL;
        }
        return false;
    }

    private boolean isDestroyingOptimization(VariableDeclarationNode node, List<Node> usageNodes) {
        // check if node is declared within a for loop and is the usage is inside the same loop
        Node sourceIterationNode = NodeFinder.findParent(node, (n) -> n instanceof IterationNode);

        if (sourceIterationNode == null) {
            // the declaration was not in any loop
            for (Node usage : usageNodes) {
                if (NodeFinder.findParent(usage, (n) -> n instanceof IterationNode) != null) {
                    // if a usage node is  inside a loop, don't propagate the constant
                    return true;
                }
            }
        } else {
            // the declaration was in any loop, make sure the usage is in the same loop
            for (Node usage : usageNodes) {
                if (!sourceIterationNode.equals(NodeFinder.findParent(usage, (n) -> n instanceof IterationNode))) {
                    // the usage node is inside another loop, don't propagate the constant
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isWorthIt(VariableDeclarationNode node, List<Node> usageNodes) {
        // create a separate tree for this declaration to include the type declaration when determining size
        final VariableDeclarationListNode listNode = new VariableDeclarationListNode(node.getFullySpecifiedType());
        listNode.addChild(node.clone(listNode));

        // assume we can get a single character variable identifier
        final int identifierSize = 1;

        final int valueScore = decider.score(node.getInitializer()) + (initializerNeedWrapping(node.getInitializer())?2:0);
        final int declarationScore = decider.score(listNode) - valueScore + identifierSize;

        return valueScore * usageNodes.size() < declarationScore;
    }

    private boolean isConstant(VariableDeclarationNode node) {
        if (node.getFullySpecifiedType().getQualifier() == TypeQualifier.CONST) {
            return true;
        }
        if (node.getParentNode().getParentNode() instanceof ForIterationNode) {
            return false;
        }
        if (node.getInitializer() instanceof HasNumeric) {
            return true;
        }

        final Set<FunctionCallNode> functionCalls = NodeFinder.findAll(node.getInitializer(), FunctionCallNode.class);
        final Set<VariableNode> variables = NodeFinder.findAll(node.getInitializer(), VariableNode.class);

        if (functionCalls.isEmpty() && variables.isEmpty()) {
            return true;
        }

        for (FunctionCallNode functionCall : functionCalls) {
            final FunctionPrototypeNode declarationNode = functionCall.getDeclarationNode();
            if (!declarationNode.getPrototype().isBuiltIn()) {
                if (((FunctionDefinitionNode) declarationNode.getParentNode()).mutatesGlobalState()) {
                    // if the function mutates global state abort
                    return false;
                }
            }
        }

        for (VariableNode variableNode : variables) {
            Usage<VariableDeclarationNode> nodeUsage = parserContext.getVariableRegistry().resolve(variableNode.getDeclarationNode());
            for (Node usage : nodeUsage.getUsageNodes()) {
                if (NodeFinder.isMutated(usage)) {
                    // the variable is mutated somewhere don't inline it
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
            changes++;
            return ReplacingASTVisitor.REMOVE;
        }

        return null;
    }

    @Override
    public Node visitVariable(VariableNode node) {
        super.visitVariable(node);

        return null;
    }

    private static class NodeUsage {
        Identifier identifier;
        Node node;
        GLSLContext context;

        public NodeUsage(GLSLContext context, Identifier identifier, Node node) {
            this.identifier = identifier;
            this.node = node;
            this.context = context;
        }
    }
}
