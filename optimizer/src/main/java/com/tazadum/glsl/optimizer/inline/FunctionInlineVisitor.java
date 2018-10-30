package com.tazadum.glsl.optimizer.inline;

import com.tazadum.glsl.ast.ReplaceUtil;
import com.tazadum.glsl.ast.ReplacingASTVisitor;
import com.tazadum.glsl.ast.id.IdentifierCreator;
import com.tazadum.glsl.exception.BadImplementationException;
import com.tazadum.glsl.exception.SourcePositionException;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.StatementListNode;
import com.tazadum.glsl.language.ast.arithmetic.NumericOperationNode;
import com.tazadum.glsl.language.ast.conditional.ReturnNode;
import com.tazadum.glsl.language.ast.expression.ParenthesisNode;
import com.tazadum.glsl.language.ast.function.FunctionCallNode;
import com.tazadum.glsl.language.ast.function.FunctionDefinitionNode;
import com.tazadum.glsl.language.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.language.ast.util.CloneUtils;
import com.tazadum.glsl.language.ast.util.NodeFinder;
import com.tazadum.glsl.language.ast.util.NodeUtil;
import com.tazadum.glsl.language.ast.variable.ParameterDeclarationNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationListNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.ast.variable.VariableNode;
import com.tazadum.glsl.language.context.GLSLContext;
import com.tazadum.glsl.language.function.FunctionRegistry;
import com.tazadum.glsl.language.model.NumericOperator;
import com.tazadum.glsl.language.type.FullySpecifiedType;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.variable.VariableRegistry;
import com.tazadum.glsl.optimizer.Branch;
import com.tazadum.glsl.optimizer.BranchRegistry;
import com.tazadum.glsl.optimizer.OptimizationDecider;
import com.tazadum.glsl.optimizer.OptimizerVisitor;
import com.tazadum.glsl.parser.ParserContext;
import com.tazadum.glsl.parser.Usage;
import com.tazadum.glsl.util.SourcePosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.tazadum.glsl.exception.Errors.Coarse.SYNTAX_ERROR;
import static com.tazadum.glsl.exception.Errors.Extras.EXPECTED_RETURN;
import static com.tazadum.glsl.exception.Errors.Extras.RECURSION_NOT_SUPPORTED;
import static com.tazadum.glsl.language.type.PredefinedType.VOID;

/**
 * Find appropriate functions to inline.
 * Created by Erik on 2016-10-20.
 */
public class FunctionInlineVisitor extends ReplacingASTVisitor implements OptimizerVisitor {
    /**
     * If a function contains more statements than this. Only attempt to inline it
     * if there's a single usage point.
     */
    private static final int FUNCTION_STATEMENT_LIMIT = 5;
    /**
     * If a function contains multiple statements and a usage of less than this constant
     * we will create a branch and inline in it.
     */
    private static final int FUNCTION_USAGE_MAX = 5;

    private final Logger logger = LoggerFactory.getLogger(FunctionInlineVisitor.class);
    private IdentifierCreator identifierGenerator = new IdentifierCreator("gen");
    private final BranchRegistry branchRegistry;
    private final OptimizationDecider decider;
    private int changes = 0;

    private Map<FunctionPrototypeNode, FunctionDefinitionNode> potentialFunctionMap = new HashMap<>();
    private Map<FunctionPrototypeNode, Set<Integer>> mutatedParameterMap = new HashMap<>();
    private Map<FunctionPrototypeNode, Integer> originalUsageCountMap = new HashMap<>();
    private List<Branch> branches;

    public FunctionInlineVisitor(ParserContext context, BranchRegistry branchRegistry, OptimizationDecider decider) {
        super(context, true, true);
        this.branchRegistry = branchRegistry;
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
    public List<Branch> getBranches() {
        return branches;
    }

    @Override
    public Node visitFunctionDefinition(FunctionDefinitionNode node) {
        super.visitFunctionDefinition(node);

        // try to find out if the function is suitable to inline
        // if it is we add it to potentialFunctions

        final FunctionRegistry functionRegistry = parserContext.getFunctionRegistry();
        final FunctionPrototypeNode functionPrototype = node.getFunctionPrototype();

        final Usage<FunctionPrototypeNode> usage = functionRegistry.resolve(functionPrototype);
        if (usage.getUsageNodes().isEmpty()) {
            // the function is not used anywhere, it will be removed by the dead code eliminator
            return null;
        }

        final StatementListNode statements = node.getStatements();
        final int statementsInFunction = statements.getChildCount();
        final int functionUsageCount = usage.getUsageNodes().size();

        if (statementsInFunction > FUNCTION_STATEMENT_LIMIT && functionUsageCount > 1) {
            // functions that are longer than this and used more than once
            // are very unlikely to produce a smaller result
            return null;
        }

        if (functionUsageCount >= FUNCTION_USAGE_MAX) {
            // functions that are multiple line and a high usage
            // are very unlikely to produce a smaller result
            return null;
        }

        final Set<Integer> mutatedParameters = new HashSet<>();
        final VariableRegistry variableRegistry = parserContext.getVariableRegistry();

        // check which of the parameters are mutated in the function
        for (int i = 0; i < functionPrototype.getChildCount(); i++) {
            final ParameterDeclarationNode parameter = functionPrototype.getParameter(i);
            final Usage<VariableDeclarationNode> parameterUsage = variableRegistry.resolve(parameter);

            for (Node usageNode : parameterUsage.getUsageNodes()) {
                if (NodeFinder.isMutated(usageNode)) {
                    // the parameter is being mutated, continue with the next parameter
                    mutatedParameters.add(i);
                    break;
                }
            }
        }

        // register all function meta-data
        potentialFunctionMap.put(functionPrototype, node);
        mutatedParameterMap.put(functionPrototype, mutatedParameters);
        originalUsageCountMap.put(functionPrototype, usage.getUsageNodes().size());

        return null;
    }

    @Override
    public Node visitFunctionCall(FunctionCallNode functionCall) {
        super.visitFunctionCall(functionCall);

        final FunctionPrototypeNode functionPrototype = functionCall.getDeclarationNode();
        if (functionPrototype.getPrototype().isBuiltIn()) {
            // a fast check to see if the function call is to a built-in function, we don't inline those.
            return null;
        }

        final FunctionDefinitionNode definitionNode = potentialFunctionMap.get(functionPrototype);
        if (definitionNode == null) {
            // the function is not on the list of functions that can be inlined
            return null;
        }

        final StatementListNode statements = definitionNode.getStatements();
        final int statementsInFunction = statements.getChildCount();
        final int functionUsageCount = originalUsageCountMap.get(functionPrototype);

        if (statementsInFunction <= 0 || functionUsageCount <= 0) {
            // no use to call a function with 0 rows, it shouldn't even pass type checking
            return REMOVE;
        }

        final GLSLType returnType = functionPrototype.getReturnType().getType();
        final boolean voidFunction = VOID.equals(returnType);
        if (voidFunction && !(functionCall.getParentNode() instanceof StatementListNode)) {
            // we can't inline void functions that are called from nodes that aren't statement lists.
            return null;
        }

        // check if the inline decision of this function already has been taken
        if (!branchRegistry.claimPoint(definitionNode, FunctionInline.class)) {
            // this node has already been considered for optimization
            // ie in another branch this function call has already been inlined
            return null;
        }

        if (statementsInFunction == 1) {
            // special case logic for functions with only a single statement
            return singleStatementFunction(functionCall, definitionNode, voidFunction);
        }

        // there's a low probability that this will be smaller after inlining
        // but let's create a branch to explore that possibility
        branches.add(createBranch());

        return multiStatementFunction(functionCall, definitionNode, voidFunction);
    }

    private Node singleStatementFunction(FunctionCallNode functionCall, FunctionDefinitionNode functionDefinition, boolean voidFunction) {
        final StatementListNode statements = functionDefinition.getStatements();
        final FunctionPrototypeNode functionPrototype = functionDefinition.getFunctionPrototype();
        final Node returnStatement = statements.getChild(0);

        if (voidFunction) {
            // perform some simple checks on the "returnNode"
            if (returnStatement instanceof ReturnNode) {
                throw new BadImplementationException(String.format("Function '%s' has a void return type and a return statement", functionCall.getIdentifier().original()));
            }
            if (!(functionCall.getParentNode() instanceof StatementListNode)) {
                throw new BadImplementationException(String.format("Can only inline '%s' if it's in a statement list : " + functionCall.getParentNode().getClass(), functionCall.getIdentifier().original()));
            }

            // we are calling a void function without a return value.
            // clone all statements, remap all variables and insert into the parent statement list

            final StatementListNode statementList = (StatementListNode) functionCall.getParentNode();
            final ArgumentList argumentList = buildArgumentList(functionCall, functionPrototype, statementList, statementList.indexOf(functionCall));
            final StatementListNode parent = (StatementListNode) functionCall.getParentNode();

            for (int i = 0; i < statements.getChildCount(); i++) {
                Node node = CloneUtils.clone(statements.getChild(i), null);
                if (node instanceof ReturnNode) {
                    continue;
                }

                Node statement = remapVariables(node, argumentList.nodes, null, true, false);
                parent.insertChild(argumentList.index + i, statement);
                parserContext.referenceTree(statement);
            }

            changes++;
            return REMOVE;
        }

        if (!(returnStatement instanceof ReturnNode)) {
            throw new BadImplementationException(String.format("Function '%s' has non-void return type but no return statement", functionCall.getIdentifier().original()));
        }

        // clone the expression in the return statement of the function
        Node expression = ((ReturnNode) returnStatement).getExpression();
        Node expressionToInline = CloneUtils.clone(expression, null);

        if (mutatedParameterMap.get(functionPrototype).isEmpty()) {
            // no parameter re-declarations needed because there's nothing that mutates them in the function
            final List<Node> functionArguments = buildArgumentList(functionCall, functionPrototype, null, 0).nodes;
            final Node node = remapVariables(expressionToInline, functionArguments, returnStatement, false, false);
            changes++;
            return node;
        }

        // we need to find a valid insertion point for parameter re-declarations
        InsertPoint insertion = findInsertionPoint(functionCall);
        if (insertion == null) {
            // no insertion point could be found
            return null;
        }

        // we need to determine if statements can be inserted at this point without trashing the shader
        // 1. The globals used in the function, they must not be mutated between declaration and insertion point
        // 2. The globals mutated in the function, they must not be used between declaration and insertion point
        if (functionPrototype.usesGlobalState() || functionPrototype.mutatesGlobalState()) {
            // get the node ids for the interval
            final int startId = insertion.statementList.getChild(insertion.index).getId();
            final int endId = functionCall.getId();

            if (!validateInsertionRange(functionDefinition, startId, endId, 10)) {
                return null;
            }
        }

        final List<Node> functionArguments = buildArgumentList(functionCall, functionPrototype, insertion.statementList, insertion.index).nodes;
        final Node node = remapVariables(expressionToInline, functionArguments, returnStatement, false, false);
        changes++;
        return node;
    }

    private Node multiStatementFunction(FunctionCallNode functionCall, FunctionDefinitionNode functionDefinition, boolean voidFunction) {
        final StatementListNode statements = functionDefinition.getStatements();
        final SortedSet<ReturnNode> returnNodes = NodeFinder.findAll(statements, ReturnNode.class);
        final FunctionPrototypeNode functionPrototype = functionDefinition.getFunctionPrototype();

        if (voidFunction) {
            // perform some simple checks on the "returnNode"
            if (!returnNodes.isEmpty()) {
                throw new BadImplementationException(String.format("Function '%s' has a void return type and a return statement", functionCall.getIdentifier().original()));
            }
            if (!(functionCall.getParentNode() instanceof StatementListNode)) {
                throw new BadImplementationException(String.format("Can only inline '%s' if it's in a statement list : " + functionCall.getParentNode().getClass(), functionCall.getIdentifier().original()));
            }

            // we are calling a void function without a return value.
            // clone all statements, remap all variables and insert into the parent statement list

            final StatementListNode statementList = (StatementListNode) functionCall.getParentNode();
            final ArgumentList argumentList = buildArgumentList(functionCall, functionPrototype, statementList, statementList.indexOf(functionCall));
            final StatementListNode parent = (StatementListNode) functionCall.getParentNode();

            for (int i = 0; i < statements.getChildCount(); i++) {
                Node node = CloneUtils.clone(statements.getChild(i), null);
                if (node instanceof ReturnNode) {
                    continue;
                }
                Node statement = remapVariables(node, argumentList.nodes, null, true, false);
                parent.insertChild(argumentList.index + i, statement);
                parserContext.referenceTree(statement);
            }

            changes++;
            return REMOVE;
        }

        if (returnNodes.isEmpty()) {
            throw new BadImplementationException(String.format("Function '%s' has non-void return type but no return statement", functionCall.getIdentifier().original()));
        }
        if (returnNodes.size() > 1) {
            // if a function has more than one return statement we can't figure out which one to use, abort.
            return null;
        }

        final Node lastChild = statements.getChild(statements.getChildCount() - 1);
        if (!(lastChild instanceof ReturnNode)) {
            throw new SourcePositionException(lastChild.getSourcePosition(), SYNTAX_ERROR(EXPECTED_RETURN));
        }

        // we need to find a valid insertion point for parameter re-declarations and function body.
        InsertPoint insertion = findInsertionPoint(functionCall);
        if (insertion == null) {
            // no insertion point could be found
            return null;
        }

        // we need to determine if statements can be inserted at this point without trashing the shader
        // 1. The globals used in the function, they must not be mutated between declaration and insertion point
        // 2. The globals mutated in the function, they must not be used between declaration and insertion point
        if (functionPrototype.usesGlobalState() || functionPrototype.mutatesGlobalState()) {
            // get the node ids for the interval
            final int startId = insertion.statementList.getChild(insertion.index).getId();
            final int endId = functionCall.getId();

            if (!validateInsertionRange(functionDefinition, startId, endId, 10)) {
                return null;
            }
        }

        // build the argument list and create variable declarations
        final ArgumentList argumentList = buildArgumentList(functionCall, functionPrototype, insertion.statementList, insertion.index);

        for (int i = 0; i < statements.getChildCount() - 1; i++) {
            // clone and map all statements
            Node node = CloneUtils.clone(statements.getChild(i), null);
            Node statement = remapVariables(node, argumentList.nodes, null, true, false);

            // insert the statements at the insertion point
            insertion.statementList.insertChild(argumentList.index + i, statement);
            parserContext.referenceTree(statement);
        }

        // replace the function call node with the expression in the return node
        final ReturnNode returnNode = (ReturnNode) lastChild;
        final Node expression = CloneUtils.clone(returnNode.getExpression(), null);
        final Node node = remapVariables(expression, argumentList.nodes, returnNode, false, false);

        changes++;
        return node;
    }

    private boolean validateInsertionRange(FunctionDefinitionNode functionDefinition, int startId, int endId, int depth) {
        if (depth < 0) {
            String identifier = functionDefinition.getFunctionPrototype().getIdentifier().original();
            throw new SourcePositionException(functionDefinition.getSourcePosition(), SYNTAX_ERROR(identifier, RECURSION_NOT_SUPPORTED));
        }

        // we need to determine if statements can be inserted at this point without trashing the shader
        // 1. The globals used in the function, they must not be mutated between declaration and insertion point
        // 2. The globals mutated in the function, they must not be used between declaration and insertion point

        final VariableRegistry variableRegistry = parserContext.getVariableRegistry();
        final StatementListNode functionStatements = functionDefinition.getStatements();
        final SortedSet<VariableNode> variableNodes = NodeFinder.findAll(functionStatements, VariableNode.class);

        final SortedSet<VariableDeclarationNode> mutatedNodes = new TreeSet<>();
        final SortedSet<VariableDeclarationNode> usedNodes = new TreeSet<>();

        for (VariableNode variableNode : variableNodes) {
            final VariableDeclarationNode declarationNode = variableNode.getDeclarationNode();

            // find out which context the variable is declared in
            final GLSLContext context = parserContext.findContext(declarationNode);
            if (!context.isGlobal()) {
                continue;
            }

            if (NodeFinder.isMutated(variableNode)) {
                usedNodes.remove(declarationNode);
                mutatedNodes.add(declarationNode);
            } else {
                if (!mutatedNodes.contains(declarationNode)) {
                    usedNodes.add(declarationNode);
                }
            }
        }

        // find out if the mutated variables are used in the range
        for (VariableDeclarationNode declarationNode : mutatedNodes) {
            Set<Node> usages = variableRegistry.resolve(declarationNode).getUsagesBetween(startId, endId);
            if (!usages.isEmpty()) {
                // one of the global variables that is mutated in the function is used between the
                // function call (that we're optimizing) and the intended insertion point for
                // extra statement. This is a no go.
                return false;
            }
        }

        // find out if the used variables are mutated in the range
        for (VariableDeclarationNode declarationNode : usedNodes) {
            if (NodeUtil.variablesMutated(variableRegistry, declarationNode, startId, endId)) {
                // one of the global variables that is used in the function is mutated between the
                // function call (that we're optimizing) and the intended insertion point for
                // extra statement. This is a no go.
                return false;
            }
        }

        // do a quick check if the functions used in the functions might cause problems
        final SortedSet<FunctionCallNode> functionCalls = NodeFinder.findAll(functionStatements, FunctionCallNode.class);
        for (FunctionCallNode functionCall : functionCalls) {
            final FunctionPrototypeNode declarationNode = functionCall.getDeclarationNode();
            if (declarationNode.getPrototype().isBuiltIn()) {
                // built-in functions are not a problem
                continue;
            }

            if (!declarationNode.usesGlobalState() && !declarationNode.mutatesGlobalState()) {
                // this is a pure function
                continue;
            }

            ParentNode definition = declarationNode.getParentNode();
            if (definition instanceof FunctionDefinitionNode) {
                if (!validateInsertionRange((FunctionDefinitionNode) definition, startId, endId, depth - 1)) {
                    // a function that's used, invalidated the insertion point.
                    return false;
                }
            }

        }

        return true;
    }

    /**
     * Remaps all function parameters found in the expression and remaps them to the arguments used in the functionCall.
     *
     * @param expression        The expression to remap variables in
     * @param functionArguments The arguments for the function.
     * @param returnStatement   An optional return statement that is used for single line functions.
     * @param dereference       If removed nodes should be dereferenced.
     * @param reference         Iff add nodes should be referenced.
     */
    private Node remapVariables(Node expression, List<Node> functionArguments, Node returnStatement, boolean dereference, boolean reference) {
        // try to find all parameter usage in the expression
        SortedSet<VariableNode> variables = NodeFinder.findAll(expression, VariableNode.class);

        for (VariableNode variable : variables) {
            // iterate through all variables in the return expression and replace them
            // with the arguments from this function call.
            final VariableDeclarationNode declarationNode = variable.getDeclarationNode();
            if (!(declarationNode instanceof ParameterDeclarationNode)) {
                // this variable is not one of the function parameters
                // it's probably a global variable or a nasty BUG!!!
                continue;
            }

            // Parameter declarations for other functions can't be reached so this
            // variable is a parameter to the function

            // flag that's true if the only thing in the return expression is this parameter. since the incoming
            // expression is cloned, check for a null parent means that the node is at the root
            final boolean singleVariable = variable.getParentNode() == null ||
                returnStatement != null && variable.getParentNode().hasEqualId(returnStatement);

            // find out which parameter index the variable has
            final int index = declarationNode.getParentNode().indexOf(declarationNode);
            if (index < 0 || index >= functionArguments.size()) {
                // the parameter index is outside the range of arguments?
                throw new BadImplementationException("The parameter index is not within the range of parameters!");
            }

            // clone the argument at the correct index and wrap it with parenthesis if needed
            Node clonedParameter = CloneUtils.clone(functionArguments.get(index), null);
            if (needWrapping(clonedParameter)) {
                clonedParameter = new ParenthesisNode(clonedParameter.getSourcePosition(), clonedParameter);
            }

            if (singleVariable) {
                // the only thing returned from this function is one of the parameters
                expression = clonedParameter;
                break;
            } else if (expression instanceof ParentNode) {
                // replace the variable with a cloned version of the function call argument
                // make sure that no referencing or de-referencing takes place, it's handled by the base class
                ReplaceUtil.replace(parserContext, (ParentNode) expression, variable, clonedParameter, dereference, reference);
            } else {
                throw new UnsupportedOperationException("The inline state is unknown or not supported");
            }
        }

        // check if we need to wrap the expression with parenthesis
        if (needWrapping(expression)) {
            expression = new ParenthesisNode(expression.getSourcePosition(), expression);
        }

        return expression;
    }

    /**
     * Build a list with the function arguments in-order. If a parameter is being mutated in the function
     * body then this method will create and add a new variable.
     *
     * @param functionCall      The function call to take arguments from
     * @param functionPrototype The function prototype
     * @return A list of arguments that needs to be cloned before attached to the AST.
     */
    private ArgumentList buildArgumentList(FunctionCallNode functionCall, FunctionPrototypeNode functionPrototype, StatementListNode statementList, int insertIndex) {
        final Set<Integer> mutatedParameters = mutatedParameterMap.get(functionPrototype);

        // build up a list of the function arguments
        final List<Node> functionArguments = new ArrayList<>();
        for (int i = 0; i < functionCall.getChildCount(); i++) {
            // check if the parameter is being mutated in the function
            if (mutatedParameters.contains(i)) {
                // the parameter is being mutated in the function
                final ParameterDeclarationNode parameter = functionPrototype.getParameter(i);

                final SourcePosition position = functionCall.getSourcePosition();
                final FullySpecifiedType fullySpecifiedType = parameter.getFullySpecifiedType();
                final String identifier = identifierGenerator.get();
                final Node initializer = CloneUtils.clone(functionCall.getChild(i), null);

                // create a new variable and assign it the argument from the function call
                VariableDeclarationNode declarationNode = new VariableDeclarationNode(position, false, fullySpecifiedType, identifier, null, initializer, null);
                VariableDeclarationListNode variableList = new VariableDeclarationListNode(position, fullySpecifiedType);
                variableList.addChild(declarationNode);

                // add the variable to the statement list
                statementList.insertChild(insertIndex++, variableList);
                parserContext.referenceTree(variableList);

                // add a variable node in the argument list
                functionArguments.add(new VariableNode(position, declarationNode));
            } else {
                // add the plain argument to the list
                functionArguments.add(functionCall.getChild(i));
            }
        }

        return new ArgumentList(functionArguments, insertIndex);
    }

    /**
     * Determines a valid insertion point for extra statements as close to a function call as possible
     */
    private InsertPoint findInsertionPoint(FunctionCallNode functionCall) {
        // find the closes statement to the function call
        final Node statement = NodeFinder.findNearestStatement(functionCall);
        if (statement == null) {
            return null;
        }

        final StatementListNode statementList = (StatementListNode) statement.getParentNode();
        if (statementList == null) {
            return null;
        }

        final int index = statementList.indexOf(statement);
        if (index < 0) {
            logger.trace("Inconsistencies in the AST found along function call : " + functionCall.getIdentifier().original());
            return null;
        }

        // TODO: check where arguments to the function call are declared

        return new InsertPoint(statementList, index);
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

    private static class ArgumentList {
        List<Node> nodes;
        int index;

        public ArgumentList(List<Node> nodes, int index) {
            this.nodes = nodes;
            this.index = index;
        }
    }

    private static class InsertPoint {
        StatementListNode statementList;
        int index;

        InsertPoint(StatementListNode statementList, int index) {
            this.statementList = statementList;
            this.index = index;
        }
    }
}
