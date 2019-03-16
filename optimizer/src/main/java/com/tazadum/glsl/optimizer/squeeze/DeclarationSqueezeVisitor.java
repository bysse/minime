package com.tazadum.glsl.optimizer.squeeze;

import com.tazadum.glsl.ast.ReplacingASTVisitor;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.function.FunctionCallNode;
import com.tazadum.glsl.language.ast.function.FunctionDefinitionNode;
import com.tazadum.glsl.language.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.language.ast.struct.InterfaceBlockNode;
import com.tazadum.glsl.language.ast.struct.StructDeclarationNode;
import com.tazadum.glsl.language.ast.util.NodeFinder;
import com.tazadum.glsl.language.ast.util.NodeUtil;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationListNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.ast.variable.VariableNode;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.language.function.FunctionRegistry;
import com.tazadum.glsl.language.type.FullySpecifiedType;
import com.tazadum.glsl.language.variable.VariableRegistry;
import com.tazadum.glsl.optimizer.OptimizerVisitor;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.Usage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Squeezes multiple variable declarations into a single line.
 * Created by Erik on 2016-10-23.
 */
public class DeclarationSqueezeVisitor extends ReplacingASTVisitor implements OptimizerVisitor {
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
    public Node visitVariableDeclarationList(VariableDeclarationListNode declarationList) {
        super.visitVariableDeclarationList(declarationList);

        if (declarationList.getChildCount() == 0) {
            // quick dead code removal
            changes++;
            return ReplacingASTVisitor.REMOVE;
        }

        // if this declaration is part of an interface block we can't squeeze it
        if (NodeFinder.findParent(declarationList, InterfaceBlockNode.class) != null) {
            return null;
        }

        // if this is a struct we can only squeeze inside the struct
        final StructDeclarationNode parentStruct = NodeFinder.findParent(declarationList, StructDeclarationNode.class);

        final GLSLContext context = parserContext.findContext(declarationList);
        final ContextDeclarations declarations = contextMap.computeIfAbsent(context, ContextDeclarations::new);
        final SortedSet<VariableDeclarationListNode> allTypeDeclarations = declarations.findDeclarations(declarationList.getFullySpecifiedType());

        // find earlier declaration lists of the same type
        for (VariableDeclarationListNode previousDeclaration : allTypeDeclarations) {
            if (previousDeclaration.getId() >= declarationList.getId()) {
                // the declaration is after the current node id. since the set is sorted in id order, so once the
                // declarations are after the current declarationList no match will ever be found
                declarations.register(declarationList);
                return null;
            }

            if (parentStruct != null) {
                // when declarations are inside a struct we have to limit the squeeze inside of the struct
                final StructDeclarationNode declarationStruct = NodeFinder.findParent(previousDeclaration, StructDeclarationNode.class);
                if (declarationStruct == null) {
                    // this declaration is not part of a struct
                    continue;
                }
                if (!parentStruct.hasEqualId(declarationStruct)) {
                    // this declaration is not part of the same struct
                    continue;
                }
            }

            // check if any of the variable initializers are modified between the current declaration of the variable
            // and the intended declaration point earlier in the source code
            for (int i = 0; i < declarationList.getChildCount(); i++) {
                final VariableDeclarationNode declaration = declarationList.getChildAs(i);

                // if the variable has no initializer it can be moved up safely
                if (declaration.getInitializer() == null) {
                    // move the declaration to the previously existing declaration list
                    declarationList.removeChild(declaration);
                    previousDeclaration.addChild(declaration);
                    changes++;
                    i--;
                    continue;
                }

                // check if the initializer contains any functions or variables
                final SortedSet<VariableNode> variables = NodeFinder.findAll(declaration, VariableNode.class);
                final SortedSet<FunctionCallNode> functionCalls = NodeFinder.findAll(declaration, FunctionCallNode.class);

                if (variables.isEmpty() && functionCalls.isEmpty()) {
                    // no variables or function calls.
                    declarationList.removeChild(declaration);
                    previousDeclaration.addChild(declaration);
                    changes++;
                    i--;
                    continue;
                }

                // since there can be quite a long list of declarations squeezed together we need to take the id of
                // the last child in the list since that's the point where we would like to insert a new declaration.
                final Node lastChild = previousDeclaration.getChild(previousDeclaration.getChildCount() - 1);
                final int previousDeclarationId = lastChild.getId();

                if (NodeUtil.variablesMutated(variableRegistry, variables, previousDeclarationId, declarationList.getId())) {
                    // at least one of the variables used in the initializer was mutated somewhere
                    // between the current declaration and the intended one.
                    continue;
                }

                if (mutatingFunctions(variables, functionCalls, previousDeclarationId, declarationList.getId())) {
                    continue;
                }

                // move the declaration to the previously existing declaration list
                declarationList.removeChild(declaration);
                previousDeclaration.addChild(declaration);
                changes++;
                i--;
            }

            if (changes > 0) {
                return null;
            }
        }

        // no previous declarations found or the squeeze couldn't be made
        // register this list as another squeeze point
        declarations.register(declarationList);

        return null;
    }

    private boolean mutatingFunctions(SortedSet<VariableNode> variables, SortedSet<FunctionCallNode> functionCalls, int fromId, int toId) {
        for (FunctionCallNode functionCall : functionCalls) {
            FunctionPrototypeNode declarationNode = functionCall.getDeclarationNode();
            if (declarationNode.mutatesGlobalState()) {
                return true;
            }
        }

        final Set<FunctionDefinitionNode> mutatingFunctions = new TreeSet<>();
        for (VariableNode variable : variables) {
            final boolean variableIsGlobal = parserContext.findContext(variable.getDeclarationNode()).isGlobal();
            if (!variableIsGlobal) {
                continue;
            }

            mutatingFunctions.clear();

            // if one of the variables used in the initializer is declared in global scope there's a possibility
            // that it's modified in a function call in the targeted range. We'll search for all variable usages
            // see if the functions that contains them are mutating it and keep a list of 'bad' functions.
            final Usage<VariableDeclarationNode> usage = variableRegistry.resolve(variable.getDeclarationNode());

            for (Node usageNode : usage.getUsageNodes()) {
                final GLSLContext context = parserContext.findContext(usageNode);
                if (context.isGlobal() || !(context instanceof FunctionDefinitionNode)) {
                    // not a function
                    continue;
                }
                final FunctionDefinitionNode functionDefinition = (FunctionDefinitionNode) context;

                final FunctionCallNode functionCall = NodeFinder.findNearestFunctionCall(usageNode);
                if (functionCall != null) {
                    // the global variable is being used in a function as part of a function call, verify it.
                    if (NodeUtil.functionCallMutates(functionCall, usageNode)) {
                        // this is a bad function, add it to the set
                        mutatingFunctions.add(functionDefinition);
                        continue;
                    }
                }

                if (!NodeFinder.isMutated(usageNode)) {
                    // the usage is not part of a mutating operation
                    continue;
                }

                // this is a bad function, add it to the set
                mutatingFunctions.add(functionDefinition);
            }

            if (mutatingFunctions.isEmpty()) {
                // no mutating function found for the global variable
                continue;
            }

            final FunctionRegistry functionRegistry = parserContext.getFunctionRegistry();

            // we have a list of functions which mutates a global variable that is used in
            // the initialization of another variables. Let's iterate and see if the function
            // is being used in the range that we want to move the declaration in.
            for (FunctionDefinitionNode definitionNode : mutatingFunctions) {
                // check if the functions are being used in the target range
                Set<Node> usagesBetween = functionRegistry
                    .resolve(definitionNode.getFunctionPrototype())
                    .getUsagesBetween(fromId, toId);

                if (usagesBetween.isEmpty()) {
                    // no usages, this function was sage in this case
                    continue;
                }

                // this function is used and it mutates the state, so we have to abort the squeeze
                return true;
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

        SortedSet<VariableDeclarationListNode> findDeclarations(FullySpecifiedType type) {
            final TreeSet<VariableDeclarationListNode> nodes = typeMap.get(type);
            if (nodes == null || nodes.isEmpty()) {
                return Collections.emptySortedSet();
            }
            nodes.removeIf(node -> node.getChildCount() == 0);
            return nodes;
        }
    }
}
