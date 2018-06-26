package com.tazadum.glsl.parser.optimizer;

import com.tazadum.glsl.ast.*;
import com.tazadum.glsl.ast.arithmetic.NumericOperationNode;
import com.tazadum.glsl.ast.conditional.ReturnNode;
import com.tazadum.glsl.ast.function.FunctionCallNode;
import com.tazadum.glsl.ast.function.FunctionDefinitionNode;
import com.tazadum.glsl.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.ast.variable.ParameterDeclarationNode;
import com.tazadum.glsl.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.ast.variable.VariableNode;
import com.tazadum.glsl.language.BuiltInType;
import com.tazadum.glsl.language.NumericOperator;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.Usage;
import com.tazadum.glsl.parser.finder.VariableFinder;
import com.tazadum.glsl.parser.function.FunctionRegistry;
import com.tazadum.glsl.parser.type.FullySpecifiedType;
import com.tazadum.glsl.util.CloneUtils;
import com.tazadum.glsl.util.IdentifierCreator;
import com.tazadum.glsl.util.ReplaceUtil;

import java.util.*;

/**
 * Created by Erik on 2016-10-20.
 */
public class FunctionInlineVisitor extends ReplacingASTVisitor implements OptimizerVisitor {
    /**
     * If a function contain more statements than this. Only attempt to inline it
     * if there's a single usage point.
     */
    private static final int FUNCTION_STATEMENT_LIMIT = 5;

    private IdentifierCreator identifierGenerator = new IdentifierCreator("il");
    private final BranchRegistry branchRegistry;
    private final OptimizationDecider decider;
    private int changes = 0;

    private Map<FunctionPrototypeNode, FunctionDefinitionNode> potentialFunctions = new HashMap<>();
    private Map<FunctionPrototypeNode, Integer> originalUsageCount = new HashMap<>();
    private List<OptimizerBranch> branches;

    public FunctionInlineVisitor(ParserContext parserContext, OptimizationDecider decider) {
        super(parserContext, true, true);
        this.branchRegistry = parserContext.getBranchRegistry();
        this.decider = decider;
        this.branches = new ArrayList<>();
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
    public Node visitFunctionDefinition(FunctionDefinitionNode node) {
        super.visitFunctionDefinition(node);

        // try to find out if the function is suitable to inline
        if (node.mutatesGlobalState()) {
            // TODO: is this really a reason?
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

        /*
        // check if all usages are ok to inline
        for (Node usingNode : usage.getUsageNodes()) {
            Node parentNode = NodeFinder.findParent(usingNode, (parent) -> {
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

            if (parentNode != null) {
                return null;
            }
        }
        */

        potentialFunctions.put(node.getFunctionPrototype(), node);
        originalUsageCount.put(node.getFunctionPrototype(), usage.getUsageNodes().size());

        return null;
    }

    @Override
    public Node visitFunctionCall(FunctionCallNode functionCall) {
        FunctionPrototypeNode functionPrototype = functionCall.getDeclarationNode();
        FunctionDefinitionNode definitionNode = potentialFunctions.get(functionPrototype);

        // check if the function is on the list
        if (definitionNode == null) {
            return null;
        }

        // check if the inline decision of this function already has been taken
        if (!branchRegistry.claimPoint(definitionNode, ConstantFolding.class)) {
            // this node has already been considered for optimization
            return null;
        }

        final StatementListNode statements = definitionNode.getStatements();
        final int statementsInFunction = statements.getChildCount();
        final int functionUsageCount = originalUsageCount.get(functionPrototype);

        if (statementsInFunction <= 0 || functionUsageCount <= 0) {
            // no use to inline zero rows
            return null;
        }

        if (statementsInFunction == 1) {
            if (functionUsageCount > 1) {
                // if the function is used in more than a single place
                // then we create an optimizer branch without any modifications
                branches.add(createBranch());
            }

            // special case logic for functions with only a single return statement
            return singleStatementFunction(functionCall, statements);
        }

        if (statementsInFunction > FUNCTION_STATEMENT_LIMIT) {
            if (functionUsageCount > 1) {
                // there's a very low probability that this will be smaller after inlining.
                return null;
            }
        }

        // create a branch before without any optimizations
        branches.add(createBranch());

        return multiStatementFunction(functionCall, statements);
    }

    private Node singleStatementFunction(FunctionCallNode functionCall, StatementListNode statements) {
        Node returnStatement = statements.getChild(0);
        if (!(returnStatement instanceof ReturnNode)) {
            // this is very odd indeed
            return null;
        }

        // clone the expression in the return statement of the function
        Node expression = ((ReturnNode)returnStatement).getExpression();
        Node expressionToInline = CloneUtils.clone(expression, null);

        // try to find all parameter usage in the expression
        SortedSet<VariableNode> variables = VariableFinder.findVariables(expression);

        for (VariableNode variable : variables) {
            // flag that's true if the only thing returned is a parameter
            boolean singleVariable = variable.getParentNode().hasEqualId(returnStatement);

            final VariableDeclarationNode declarationNode = variable.getDeclarationNode();
            if (declarationNode instanceof ParameterDeclarationNode) {
                // Parameter declarations for other functions can't be reached so this
                // variable is a parameter to the function

                int index = declarationNode.getParentNode().indexOf(declarationNode);
                if (index >= 0 && index < functionCall.getChildCount()) {
                    Node clonedParameter = CloneUtils.clone(functionCall.getChild(index), null);

                    if (needWrapping(clonedParameter)) {
                        clonedParameter = new ParenthesisNode(clonedParameter);
                    }

                    if (singleVariable) {
                        // this will run if the only thing returned from a function is one of the parameters
                        expressionToInline = clonedParameter;
                    } else if (expressionToInline instanceof ParentNode) {
                        ReplaceUtil.replace(parserContext, (ParentNode) expressionToInline, variable, clonedParameter);
                    } else {
                        throw new UnsupportedOperationException("The inline state is unknown or not supported");
                    }
                } else {
                    // the parameter index is outside the range of arguments?
                    throw new IllegalStateException("The parameter index is not within the range of parameters!");
                }
            }
        }

        // check if we need to wrap the expression with parenthesis
        if (needWrapping(expression)) {
            expressionToInline = new ParenthesisNode(expressionToInline);
        }

        // Replace and dereference the function usage
        changes++;
        return expressionToInline;
    }

    private Node multiStatementFunction(FunctionCallNode functionCall, StatementListNode statements) {
        Node returnStatement = statements.getChild(0);
        if (!(returnStatement instanceof ReturnNode)) {
            // this is very odd indeed
            return null;
        }

        // TODO: declare variables for each parameter



        return null;
    }

    /**
     * Check if the provided node needs to be wrapped with parenthesis
     */
    private boolean needWrapping(Node node) {
        if (node instanceof NumericOperationNode) {
            NumericOperator operator = ((NumericOperationNode) node).getOperator();
            return operator == NumericOperator.ADD || operator == NumericOperator.SUB;
        }
        return false;
    }
}
