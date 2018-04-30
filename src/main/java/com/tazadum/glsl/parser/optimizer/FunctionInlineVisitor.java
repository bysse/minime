package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.Node;
import com.tazadum.glsl.ast.ReplacingASTVisitor;
import com.tazadum.glsl.ast.StatementListNode;
import com.tazadum.glsl.ast.conditional.ReturnNode;
import com.tazadum.glsl.ast.expression.AssignmentNode;
import com.tazadum.glsl.ast.function.FunctionCallNode;
import com.tazadum.glsl.ast.function.FunctionDefinitionNode;
import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.ast.variable.VariableNode;
import com.tazadum.glsl.language.BuiltInType;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.Usage;
import com.tazadum.glsl.parser.finder.NodeFinder;
import com.tazadum.glsl.parser.finder.VariableFinder;
import com.tazadum.glsl.parser.function.FunctionRegistry;
import com.tazadum.glsl.parser.type.FullySpecifiedType;
import com.tazadum.glsl.util.IdentifierCreator;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

/**
 * Created by Erik on 2016-10-20.
 */
public class FunctionInlineVisitor extends ReplacingASTVisitor {
    private IdentifierCreator identifierGenerator = new IdentifierCreator("il");
    private final OptimizationDecider decider;
    private int changes = 0;

    private Map<FunctionPrototypeNode, FunctionDefinitionNode> potentialFunctions = new HashMap<>();

    public FunctionInlineVisitor(ParserContext parserContext, OptimizationDecider decider) {
        super(parserContext, true);
        this.decider = decider;
    }

    public void reset() {
        this.changes = 0;
    }

    public int getChanges() {
        return changes;
    }

    @Override
    public Node visitFunctionDefinition(FunctionDefinitionNode node) {
        // try to find out if the function is suitable to inline

        if (node.mutatesGlobalState()) {
            // functions that mutates global state are often too complicated to inline
            return null;
        }

        FullySpecifiedType returnType = node.getFunctionPrototype().getReturnType();
        if (BuiltInType.VOID.equals(returnType.getType())) {
            // if the return type is void, we can't inline it
            return null;
        }

        FunctionRegistry functionRegistry = parserContext.getFunctionRegistry();
        Usage<FunctionPrototypeNode> usage = functionRegistry.resolve(node.getFunctionPrototype());

        if (usage.getUsageNodes().isEmpty()) {
            // the node will be removed by the dead code eliminator in the next run
            return null;
        }

        if (usage.getUsageNodes().size() > 1) {
            // TODO: check if size optimization is possible
        }

        // check if all usages are ok to inline
        for (Node usingNode : usage.getUsageNodes()) {
            NodeFinder.findParent(usingNode, (parent) -> {
                if (parent instanceof AssignmentNode) {
                    // find all variables on the left side of the equal sign
                    SortedSet<VariableNode> variables = VariableFinder.findVariables(((AssignmentNode) parent).getLeft());

                    for (VariableNode variable : variables) {
                        // say no to usages in global context
                        if (parserContext.findContext(variable).isGlobal()) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            });
        }

        potentialFunctions.put(node.getFunctionPrototype(), node);
        return null;
    }

    @Override
    public Node visitFunctionCall(FunctionCallNode node) {
        FunctionPrototypeNode functionPrototype = node.getDeclarationNode();
        FunctionDefinitionNode definitionNode = potentialFunctions.get(functionPrototype);

        // check if the function is on the list
        if (definitionNode == null) {
            return null;
        }

        StatementListNode statements = definitionNode.getStatements();
        if (statements.getChildCount() > 1) {
            // the current inline logic only works with single line functions.
            return null;
        }

        Node returnStatement = statements.getChild(0);
        if (!(returnStatement instanceof ReturnNode)) {
            // this is very odd indeed
            return null;
        }

        // clone and expression in the return statement of the function
        Node inlined = ((ReturnNode) returnStatement).getExpression().clone(null);

        // TODO: remap and replace
        // insert the function node mapping the variables to the new variables

        // Replace and dereference the function usage
        changes++;
        return inlined;
    }
}
