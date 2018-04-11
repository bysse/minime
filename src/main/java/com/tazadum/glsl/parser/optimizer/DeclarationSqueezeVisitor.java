package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ReplacingASTVisitor;
import com.tazadum.glsl.ast.function.FunctionCallNode;
import com.tazadum.glsl.ast.function.FunctionDefinitionNode;
import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.ast.variable.ParameterDeclarationNode;
import com.tazadum.glsl.ast.variable.VariableDeclarationListNode;
import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.ast.variable.VariableNode;
import com.tazadum.glsl.language.GLSLType;
import com.tazadum.glsl.language.TypeQualifier;
import com.tazadum.glsl.parser.GLSLContext;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.Usage;
import com.tazadum.glsl.parser.finder.NodeFinder;
import com.tazadum.glsl.parser.finder.VariableFinder;
import com.tazadum.glsl.parser.type.FullySpecifiedType;
import com.tazadum.glsl.parser.variable.VariableRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by Erik on 2016-10-23.
 */
public class DeclarationSqueezeVisitor extends ReplacingASTVisitor {
    private final Logger logger = LoggerFactory.getLogger(DeclarationSqueezeVisitor.class);
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

        // Find mAny earlier declarations of this type
        final VariableDeclarationListNode previousDeclaration = declarations.findDeclaration(node.getFullySpecifiedType());
        if (previousDeclaration != null && previousDeclaration.getId() < node.getId()) {
            // Check if the usage is "safe" between this id and the previous declaration

            for (int i = 0; i < node.getChildCount(); i++) {
                final VariableDeclarationNode declaration = node.getChild(i, VariableDeclarationNode.class);

                // if the declaration has no initializer it can be moved up safely
                if (declaration.getInitializer() == null) {
                    // move the declaration to the previously existing declaration list
                    node.removeChild(declaration);
                    previousDeclaration.addChild(declaration);
                    changes++;
                    i--;
                } else {
                    // Check if the initializer contains mAny variables
                    final SortedSet<VariableNode> variables = VariableFinder.findVariables(declaration);

                    if (variables.isEmpty() || variablesAreSafe(variables, previousDeclaration.getId(), node.getId(), previousDeclaration.getType())) {
                        // move the declaration to the previously existing declaration list
                        node.removeChild(declaration);
                        previousDeclaration.addChild(declaration);
                        changes++;
                        i--;
                    }
                }
            }
        }

        //System.out.println("--------------");
        //node.accept(new IdVisitor());

        // register this declaration
        declarations.register(node);

        return null;
    }

    private boolean variablesAreSafe(SortedSet<VariableNode> variables, int fromId, int toId, GLSLType type) {
        // Check if the variables are modified anywhere
        for (VariableNode variable : variables) {
            final Usage<VariableDeclarationNode> usage = variableRegistry.resolve(variable.getDeclarationNode());
            final Set<Node> usagesBetween = usage.getUsagesBetween(fromId, toId);

            // Check if the declaration of those variables are after the attempted squeeze position and of a different type
            if (!type.isAssignableBy(variable.getDeclarationNode().getType())) {
                if (fromId < variable.getDeclarationNode().getId()) {
                    return false;
                }
            }

            // no usages of the variable
            if (usagesBetween.isEmpty()) {
                continue;
            }

            // Check if the variable was involved in a mutating operation
            for (Node node : usagesBetween) {
                if (NodeFinder.isMutated(node)) {
                    return false;
                }

                final FunctionCallNode functionCall = NodeFinder.findFunctionCall(node);
                if (functionCall != null) {
                    final FunctionPrototypeNode functionDeclaration = functionCall.getDeclarationNode();
                    if (functionDeclaration.getPrototype().isBuiltIn()) {
                        // built-in function doesn't mutate parameter state
                        continue;
                    }

                    // check if the parameter declaration has the TypeQualifier OUT or INOUT
                    for (int i = 0; i < functionCall.getChildCount(); i++) {
                        // check if argument i is equal to node
                        if (node.equals(functionCall.getChild(i))) {
                            final ParameterDeclarationNode parameterDeclaration = functionDeclaration.getChild(i, ParameterDeclarationNode.class);
                            final TypeQualifier qualifier = parameterDeclaration.getFullySpecifiedType().getQualifier();

                            if (qualifier == null) {
                                break;
                            }
                            if (qualifier != TypeQualifier.INOUT && qualifier != TypeQualifier.OUT) {
                                break;
                            }
                            return false;
                        }
                    }

                    // if the variable is in global scope there's a possibility that it's modifier anyway
                    /*
                    final GLSLContext variableContext = parserContext.findContext(variable.getDeclarationNode());
                    if (parserContext.globalContext().equals(variableContext) && modifiedInFunction(functionDeclaration, usage)) {
                        return false;
                    }
                    */
                }
            }
        }

        return true;
    }

    private boolean modifiedInFunction(FunctionPrototypeNode functionDeclaration, Usage<VariableDeclarationNode> usage) {
        final FunctionDefinitionNode definitionNode = (FunctionDefinitionNode) functionDeclaration.getParentNode();

        for (Node usageNode : usage.getUsageNodes()) {
            // check if the variable was used in the function
            if (NodeFinder.isNodeInTree(usageNode, definitionNode.getStatements())) {
                if (NodeFinder.isMutated(usageNode)) {
                    return true;
                }
            }
        }

        return false;
    }

    private class ContextDeclarations {
        private final GLSLContext context;
        private final Map<FullySpecifiedType, TreeSet<VariableDeclarationListNode>> typeMap;

        ContextDeclarations(GLSLContext context) {
            this.context = context;
            this.typeMap = new HashMap<>();
        }

        void register(VariableDeclarationListNode node) {
            typeMap.computeIfAbsent(node.getFullySpecifiedType(), (key) -> new TreeSet<>()).add(node);
        }

        VariableDeclarationListNode findDeclaration(FullySpecifiedType type) {
            final TreeSet<VariableDeclarationListNode> nodes = typeMap.get(type);
            if (nodes == null || nodes.isEmpty()) {
                return null;
            }
            for (VariableDeclarationListNode node : nodes) {
                if (node.getChildCount() > 0) {
                    return node;
                }
            }
            return null;
        }
    }
}
