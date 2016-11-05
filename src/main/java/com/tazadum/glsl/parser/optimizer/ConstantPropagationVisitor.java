package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.HasNumeric;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ReplacingASTVisitor;
import com.tazadum.glsl.ast.arithmetic.PostfixOperationNode;
import com.tazadum.glsl.ast.arithmetic.PrefixOperationNode;
import com.tazadum.glsl.ast.function.FunctionCallNode;
import com.tazadum.glsl.ast.function.FunctionDefinitionNode;
import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
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
    private int changes = 0;

    public ConstantPropagationVisitor(ParserContext parserContext) {
        super(parserContext, false);
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

            if (usageNodes.size() == 1) {
                final VariableNode usage = (VariableNode) usageNodes.get(0);
                if (usage.getParentNode() instanceof PrefixOperationNode || usage.getParentNode() instanceof PostfixOperationNode) {
                    return null;
                }

                usage.getParentNode().replaceChild(usage, node.getInitializer());

                changes++;
                return ReplacingASTVisitor.REMOVE;
            } else {
                // TODO: verify if it's worth it
            }
        }

        return null;
    }

    private boolean isConstant(VariableDeclarationNode node) {
        if (node.getFullySpecifiedType().getQualifier() == TypeQualifier.CONST) {
            return true;
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
