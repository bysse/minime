package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.MutatingOperation;
import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ReplacingASTVisitor;
import com.tazadum.glsl.ast.expression.AssignmentNode;
import com.tazadum.glsl.ast.variable.VariableDeclarationListNode;
import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.ast.variable.VariableNode;
import com.tazadum.glsl.language.GLSLType;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.Usage;
import com.tazadum.glsl.parser.finder.MutableOperationFinder;
import com.tazadum.glsl.parser.finder.NodeFinder;
import com.tazadum.glsl.parser.finder.VariableFinder;
import com.tazadum.glsl.parser.variable.VariableRegistry;

import java.util.*;

/**
 * Created by Erik on 2016-10-23.
 */
public class DeclarationSqueezeVisitor extends ReplacingASTVisitor {
    private final VariableRegistry variableRegistry;
    private int changes = 0;

    private Map<GLSLContext, ContextDeclarations> contextMap;

    public DeclarationSqueezeVisitor(ParserContext parserContext) {
        super(parserContext, false);
        this.variableRegistry = parserContext.getVariableRegistry();
        this.contextMap = new HashMap<>();
    }

    public void reset() {
        this.changes = 0;
        this.contextMap.clear();
    }

    public int getChanges() {
        return changes;
    }

    @Override
    public Node visitVariableDeclaration(VariableDeclarationNode node) {
        super.visitVariableDeclaration(node);
        return null;
    }

    @Override
    public Node visitVariableDeclarationList(VariableDeclarationListNode node) {
        super.visitVariableDeclarationList(node);

        if (node.getChildCount() == 0) {
            return ReplacingASTVisitor.REMOVE;
        }

        final GLSLContext context = parserContext.findContext(node);
        final ContextDeclarations declarations = contextMap.computeIfAbsent(context, ContextDeclarations::new);

        // Find any earlier declarations of this type
        final VariableDeclarationListNode previousDeclaration = declarations.findDeclaration(node.getType());
        if (previousDeclaration != null && previousDeclaration.getId() < node.getId()) {
            // Check if the usage is "safe" between this id and the previous declaration

            for (int i = 0; i < node.getChildCount(); i++) {
                final VariableDeclarationNode declaration = node.getChild(i, VariableDeclarationNode.class);

                // if the declaration has no initializer it can be moved up safely
                if (declaration.getInitializer() == null) {
                    // move the declaration to the previously existing declaration list
                    previousDeclaration.addChild(declaration);
                    node.removeChild(declaration);
                    changes++;
                    i--;
                } else {
                    // Check if the initializer contains any variables
                    final SortedSet<VariableNode> variables = VariableFinder.findVariables(declaration);
                    if (variables.isEmpty() || variablesAreSafe(variables, previousDeclaration.getId(), node.getId())) {
                        // move the declaration to the previously existing declaration list
                        previousDeclaration.addChild(declaration);
                        node.removeChild(declaration);
                        changes++;
                        i--;
                    }
                }
            }
        }

        // register this declaration
        declarations.register(node);

        return null;
    }

    private boolean variablesAreSafe(SortedSet<VariableNode> variables, int fromId, int toId) {
        // Check if the variables are modified anywhere
        for (VariableNode variable : variables) {
            final Usage<VariableDeclarationNode> usage = variableRegistry.resolve(variable.getDeclarationNode());
            final Set<Node> usagesBetween = usage.getUsagesBetween(fromId, toId);

            // no usages of the variable
            if (usagesBetween.isEmpty()) {
                continue;
            }

            // Check if the variable was involved in a mutating operation
            for (Node node : usagesBetween) {
                MutatingOperation operation = MutableOperationFinder.findMutableOperation(node);
                if (operation != null) {
                    if (operation instanceof AssignmentNode) {
                        final Node assignment = ((AssignmentNode) operation).getLeft();
                        if (NodeFinder.isNodeInTree(node, assignment)) {
                            // the variable usage is in the assignment part of an AssignmentNode
                            return false;
                        }
                        continue;
                    }
                    // The MutatingOperation was not an AssignmentNode of the 'good' type
                    return false;
                }
            }
        }

        return true;
    }

    private class ContextDeclarations {
        private final GLSLContext context;
        private final Map<GLSLType, TreeSet<VariableDeclarationListNode>> typeMap;

        ContextDeclarations(GLSLContext context) {
            this.context = context;
            this.typeMap = new HashMap<>();
        }

        void register(VariableDeclarationListNode node) {
            typeMap.computeIfAbsent(node.getType(), (key) -> new TreeSet<>()).add(node);
        }

        VariableDeclarationListNode findDeclaration(GLSLType type) {
            final TreeSet<VariableDeclarationListNode> nodes = typeMap.get(type);
            if (nodes == null || nodes.isEmpty()) {
                return null;
            }
            return nodes.first();
        }
    }
}
