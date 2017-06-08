package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.HasNumeric;
import com.tazadum.glsl.ast.MutatingOperation;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ReplacingASTVisitor;
import com.tazadum.glsl.ast.function.FunctionCallNode;
import com.tazadum.glsl.ast.function.FunctionDefinitionNode;
import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.ast.iteration.ForIterationNode;
import com.tazadum.glsl.ast.variable.VariableDeclarationListNode;
import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.ast.variable.VariableNode;
import com.tazadum.glsl.language.TypeQualifier;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.Usage;
import com.tazadum.glsl.parser.finder.NodeFinder;

import java.util.List;
import java.util.Set;

/**
 * Created by Erik on 2016-10-29.
 */
public class ConstantPropagationVisitor extends ReplacingASTVisitor {
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

            if (usageNodes.size() == 1 || isWorthIt(node, usageNodes)) {
                for (Node usageNode : usageNodes) {
                    final VariableNode usage = (VariableNode) usageNode;
                    if (usage.getParentNode() instanceof MutatingOperation) {
                        return null;
                    }

                    usage.getParentNode().replaceChild(usage, node.getInitializer());

                    changes++;
                }
                return ReplacingASTVisitor.REMOVE;
            }
        }

        return null;
    }

    private boolean isWorthIt(VariableDeclarationNode node, List<Node> usageNodes) {
        // create a separate tree for this declaration to include the type declaration when determining size
        final VariableDeclarationListNode listNode = new VariableDeclarationListNode(node.getFullySpecifiedType());
        listNode.addChild(node.clone(listNode));

        // assume we can get a single character variable identifier
        final int identifierSize = 1;

        final int valueScore = decider.score(node.getInitializer());
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
}
