package com.tazadum.glsl.parser;

import com.tazadum.glsl.exception.*;
import com.tazadum.glsl.exception.NoSuchFieldException;
import com.tazadum.glsl.language.HasToken;
import com.tazadum.glsl.language.ast.DefaultASTVisitor;
import com.tazadum.glsl.language.ast.LeafNode;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.arithmetic.*;
import com.tazadum.glsl.language.ast.conditional.TernaryConditionNode;
import com.tazadum.glsl.language.ast.expression.ConstantExpressionNode;
import com.tazadum.glsl.language.ast.expression.ParenthesisNode;
import com.tazadum.glsl.language.ast.function.FunctionCallNode;
import com.tazadum.glsl.language.ast.logical.BooleanLeafNode;
import com.tazadum.glsl.language.ast.logical.LogicalOperationNode;
import com.tazadum.glsl.language.ast.logical.RelationalOperationNode;
import com.tazadum.glsl.language.ast.struct.StructDeclarationNode;
import com.tazadum.glsl.language.ast.variable.*;
import com.tazadum.glsl.language.function.ConstFunction;
import com.tazadum.glsl.language.model.StorageQualifier;
import com.tazadum.glsl.language.type.*;

import static com.tazadum.glsl.exception.Errors.Coarse.*;
import static com.tazadum.glsl.exception.Errors.Extras.*;
import static com.tazadum.glsl.language.type.PredefinedType.INT;
import static com.tazadum.glsl.language.type.PredefinedType.UINT;
import static com.tazadum.glsl.parser.TypeCombination.anyOf;
import static com.tazadum.glsl.parser.TypeCombination.compatibleType;

/**
 * A constant expression is one of:
 * <ul>
 * <li>A literal value (e.g. 5 or true).</li>
 * <li>A variable declared with the const qualifier and an initializer, where the initializer is a constant
 * expression. This includes both const declared with a specialization-constant layout qualifier,
 * e.g. layout(constant_id = …), and those declared without a specialization-constant layout
 * qualifier.</li>
 * <li>An expression formed by an operator on operands that are all constant expressions, including
 * getting an element of a constant array, or a member of a constant structure, or components of a
 * constant vector. However, the lowest precedence operators of the sequence operator (,) and the
 * assignment operators (=, +=, …) are not included in the operators that can create a constant
 * expression. Also, an array access with a specialization constant as an index does not result in a
 * constant expression.</li>
 * <li>Valid use of the length() method on an explicitly sized object, whether or not the object itself is
 * constant (implicitly sized or run-time sized arrays do not return a constant expression).</li>
 * <li>A constructor whose arguments are all constant expressions.</li>
 * <li>For non-specialization constants only: the value returned by certain built-in function calls
 * whose arguments are all constant expressions, including at least the list below. Any other built45
 * in function that does not access memory (not the texture lookup functions, image access, atomic
 * counter, etc.), that has a non-void return type, that has no out parameter, and is not a noise
 * function might also be considered a constant. When a function is called with an argument that
 * is a specialization constant, the result is not a constant expression.
 * <ul>
 * <li>Angle and Trigonometric Functions
 * <ul>
 * <li>radians</li>
 * <li>degrees</li>
 * <li>sin</li>
 * <li>cos</li>
 * <li>asin</li>
 * <li>acos</li>
 * </ul>
 * </li>
 * <li>Exponential Functions
 * <ul>
 * <li>pow</li>
 * <li>exp</li>
 * <li>log</li>
 * <li>exp2</li>
 * <li>log2</li>
 * <li>sqrt</li>
 * <li>inversesqrt</li>
 * </ul>
 * </li>
 * <li>Common Functions
 * <ul>
 * <li>abs</li>
 * <li>sign</li>
 * <li>floor</li>
 * <li>trunc</li>
 * <li>round</li>
 * <li>ceil</li>
 * <li>mod</li>
 * <li>min</li>
 * <li>max</li>
 * <li>clamp</li>
 * </ul>
 * </li>
 * <li>Geometric Functions
 * <ul>
 * <li>length</li>
 * <li>dot</li>
 * <li>normalize</li>
 * </ul>
 * </li>
 * </ul>
 * </li>
 * <li>Function calls to user-defined functions (non-built-in functions) cannot be used to form
 * constant expressions.</li>
 * </ul>
 * Created by erikb on 2018-10-16.
 */
public class ConstExpressionEvaluatorVisitor extends DefaultASTVisitor<ConstExpressionEvaluatorVisitor.ConstResult> {
    private final ParserContext parserContext;

    private int escape = 10000;
    private boolean variablesAllowed = true;
    private GLSLType structFieldType = null;

    ConstExpressionEvaluatorVisitor(ParserContext parserContext) {
        this.parserContext = parserContext;
    }

    public static Numeric evaluate(ParserContext parserContext, Node node) {
        ConstExpressionEvaluatorVisitor visitor = new ConstExpressionEvaluatorVisitor(parserContext);
        ConstResult result = node.accept(visitor);
        while (!result.isNumeric()) {
            result = result.getNode().accept(visitor);
        }
        return result.getNumeric();
    }

    private ConstResult abort(Node node) {
        throw new NotConstExpressionException(node, NOT_CONST_EXPRESSION(null));
    }

    @Override
    protected <T extends ParentNode> void visitChildren(T node) {
        abort(node);
    }

    @Override
    protected void visitLeafNode(LeafNode node) {
        abort(node);
    }

    @Override
    public ConstResult visitBoolean(BooleanLeafNode node) {
        // TODO: sketchy handling
        return number(1, 0, PredefinedType.BOOL);
    }

    @Override
    public ConstResult visitParenthesis(ParenthesisNode node) {
        return node.accept(this);
    }

    @Override
    public ConstResult visitVariable(VariableNode node) {
        if (escape-- <= 0) {
            throw new SourcePositionException(node, "Infinite loop in declaration / initializer");
        }

        // TODO: This could create loops if the initialize contains a reference to the variable
        return getInitializerNode(node).accept(this);
    }

    @Override
    public ConstResult visitFieldSelection(FieldSelectionNode node) {
        Node expression = node.getExpression();

        // check if node.getExpression is an array index node
        if (expression instanceof FieldSelectionNode) {
            ConstResult result = expression.accept(this);
            if (result.isNumeric()) {
                abort(expression);
            }
            expression = result.getNode();
        }

        GLSLType nodeType = null;
        InitializerListNode initializerList = null;

        if (expression instanceof InitializerListNode) {
            initializerList = (InitializerListNode) expression;
            nodeType = structFieldType;
        } else if (expression instanceof VariableNode) {
            final VariableNode variableNode = (VariableNode) expression;
            Node initializerNode = getInitializerNode(variableNode);
            if (!(initializerNode instanceof InitializerListNode)) {
                abort(initializerNode);
            }
            initializerList = (InitializerListNode) initializerNode;

            final VariableDeclarationNode declarationNode = variableNode.getDeclarationNode();
            nodeType = declarationNode.getFullySpecifiedType().getType();
        } else {
            abort(node);
        }

        if (nodeType instanceof ArrayType) {
            // arrays only have a single field that can be accessed and that's the 'length()' function.
            if (node instanceof LengthFunctionFieldSelectionNode) {
                // return array dimension
                final ArrayType arrayType = (ArrayType) nodeType;
                if (arrayType.hasDimension()) {
                    // the array type was explicitly specified, return the size
                    return number(arrayType.getDimension(), 0, UINT);
                } else {
                    // no dimension specified, look at the initializer
                    return number(initializerList.getChildCount(), 0, UINT);
                }
            }
            return abort(node);
        }

        if (nodeType instanceof StructType) {
            // resolve the field value in the initializer and return
            final StructType structType = (StructType) nodeType;
            try {
                final int fieldIndex = structType.getFieldIndex(node.getSelection());
                if (0 <= fieldIndex && fieldIndex <= initializerList.getChildCount()) {
                    structFieldType = structType.fieldType(node.getSelection());
                    return node(initializerList.getChild(fieldIndex));
                }

                throw new NotConstExpressionException(node, NOT_CONST_EXPRESSION(INITIALIZER_TOO_SMALL));
            } catch (NoSuchFieldException e) {
                throw new SourcePositionException(node, UNKNOWN_SYMBOL(e.getMessage()));
            }
        }

        return abort(node);
    }

    @Override
    public ConstResult visitArrayIndex(ArrayIndexNode node) {
        if (!(node.getExpression() instanceof VariableNode)) {
            // more complex are not allowed in constant expressions
            abort(node.getExpression());
        }

        final VariableNode variableNode = (VariableNode) node.getExpression();
        final Node initializerNode = getInitializerNode(variableNode);

        if (!(initializerNode instanceof InitializerListNode)) {
            abort(initializerNode);
        }
        final InitializerListNode initializerList = (InitializerListNode) initializerNode;

        final FullySpecifiedType fullySpecifiedType = variableNode.getDeclarationNode().getFullySpecifiedType();
        if (!fullySpecifiedType.getType().isArray()) {
            return abort(variableNode);
        }

        // resolve the index and verify it
        variablesAllowed = false;
        Node indexNode = node.getIndex();
        Numeric indexNumeric = indexNode.accept(this).getNumeric();
        variablesAllowed = true;

        PredefinedType indexType = indexNumeric.getType();
        if (!anyOf(indexType, INT, UINT)) {
            throw new SourcePositionException(indexNode, INCOMPATIBLE_TYPE(indexType, ARRAY_INDEX_NOT_INT));
        }
        int index = (int) indexNumeric.getValue();
        if (index < 0) {
            throw new SourcePositionException(indexNode, INCOMPATIBLE_TYPE(indexType, ARRAY_INDEX_NOT_INT));
        }
        if (index >= initializerList.getChildCount()) {
            String details = ARRAY_INDEX_OUT_OF_BOUNDS.details(index, initializerList.getChildCount());
            throw new NotConstExpressionException(indexNode, NOT_CONST_EXPRESSION(ARRAY_INDEX_OUT_OF_BOUNDS) + details);
        }

        // resolve the initializer at the correct index
        return node(initializerList.getChild(index));
    }

    @Override
    public ConstResult visitRelationalOperation(RelationalOperationNode node) {
        Numeric left = expectNumeric(node.getLeft().accept(this));
        Numeric right = expectNumeric(node.getRight().accept(this));
        Boolean value = node.getOperator().apply(left, right);

        return number(boolInt(value), 0, PredefinedType.BOOL);
    }

    @Override
    public ConstResult visitLogicalOperation(LogicalOperationNode node) {
        Numeric left = expectNumeric(node.getLeft().accept(this));
        Numeric right = expectNumeric(node.getRight().accept(this));

        switch (node.getOperator()) {
            case OR:
                return number(boolInt(left.getValue() > 0 || right.getValue() > 0), 0, PredefinedType.BOOL);
            case AND:
                return number(boolInt(left.getValue() > 0 && right.getValue() > 0), 0, PredefinedType.BOOL);
            case XOR:
                return number(boolInt(left.getValue() > 0 ^ right.getValue() > 0), 0, PredefinedType.BOOL);
        }

        throw new BadImplementationException();
    }

    @Override
    public ConstResult visitFunctionCall(FunctionCallNode node) {
        if (!node.isConstant()) {
            abort(node);
        }

        final String functionName = node.getIdentifier().original();
        final ConstFunction function = HasToken.fromString(functionName, ConstFunction.values());
        if (function == null) {
            throw new NotConstExpressionException(node, FUNCTION_NOT_CONST_EXPRESSION(functionName, null));
        }
        if (node.getChildCount() != 1) {
            throw new BadImplementationException("const expressions for functions with multiple arguments are not supported");
        }

        // TODO: functions with multiple arguments or vec -> scalar types are not supported

        Numeric numeric = expectNumeric(node.getChild(0).accept(this));
        return number(function.apply(numeric));
    }

    @Override
    public ConstResult visitConstantExpression(ConstantExpressionNode node) {
        return node.getExpression().accept(this);
    }

    @Override
    public ConstResult visitTernaryCondition(TernaryConditionNode node) {
        Numeric condition = expectNumeric(node.getCondition().accept(this));
        if (condition != null && condition.getType() == PredefinedType.BOOL) {
            if (condition.getValue() > 0) {
                return node.getThen().accept(this);
            } else {
                return node.getElse().accept(this);
            }
        }
        return abort(node);
    }

    @Override
    public ConstResult visitPrefixOperation(PrefixOperationNode node) {
        Numeric numeric = expectNumeric(node.getExpression().accept(this));

        switch (node.getOperator()) {
            case DECREASE:
                return number(numeric.getValue() - 1, numeric.getDecimals(), numeric.getType());
            case INCREASE:
                return number(numeric.getValue() + 1, numeric.getDecimals(), numeric.getType());
            case MINUS:
                return number(-numeric.getValue(), numeric.getDecimals(), numeric.getType());
            case PLUS:
                return number(numeric);
            case BANG:
                return number(1 - numeric.getValue(), numeric.getDecimals(), PredefinedType.BOOL);
        }
        return abort(node);
    }

    @Override
    public ConstResult visitPostfixOperation(PostfixOperationNode node) {
        return abort(node);
    }

    @Override
    public ConstResult visitNumericOperation(NumericOperationNode node) {
        try {
            Numeric left = expectNumeric(node.getLeft().accept(this));
            Numeric right = expectNumeric(node.getRight().accept(this));

            switch (node.getOperator()) {
                case ADD:
                    return number(NumericOperation.add(left, right));
                case DIV:
                    return number(NumericOperation.div(left, right));
                case SUB:
                    return number(NumericOperation.sub(left, right));
                case MUL:
                    return number(NumericOperation.mul(left, right));
                case MOD:
                    return number(NumericOperation.mod(left, right));
            }
        } catch (TypeException e) {
            throw new SourcePositionException(node, e.getMessage(), e);
        }

        throw new BadImplementationException();
    }

    @Override
    public ConstResult visitBitOperation(BitOperationNode node) {
        try {
            Numeric left = expectNumeric(node.getLeft().accept(this));
            Numeric right = expectNumeric(node.getRight().accept(this));
            GLSLType type = compatibleType(left.getType(), right.getType());

            if (!anyOf(type, INT, UINT)) {
                // this is an illegal operation
                throw new SourcePositionException(node, INCOMPATIBLE_TYPES(left.getType(), right.getType(), EXPECTED_INTEGER_SCALAR));
            }

            final int lval = (int) left.getValue();
            final int rval = (int) right.getValue();
            final PredefinedType basicType = (PredefinedType) type;

            switch (node.getOperator()) {
                case SHIFT_LEFT:
                    return number(lval << rval, 0, basicType);
                case SHIFT_RIGHT:
                    return number(lval >> rval, 0, basicType);
                case AND:
                    return number(lval & rval, 0, basicType);
                case OR:
                    return number(lval | rval, 0, basicType);
                case XOR:
                    return number(lval ^ rval, 0, basicType);
            }
        } catch (TypeException e) {
            throw SourcePositionException.wrap(node, e);
        }

        throw new BadImplementationException();
    }

    @Override
    public ConstResult visitInt(IntLeafNode node) {
        return number(node.getValue());
    }

    @Override
    public ConstResult visitFloat(FloatLeafNode node) {
        return number(node.getValue());
    }

    @Override
    public ConstResult visitInitializerList(InitializerListNode node) {
        if (node.getChildCount() == 1) {
            return node.getChild(0).accept(this);
        }
        return super.visitInitializerList(node);
    }

    @Override
    public ConstResult visitStructDeclarationNode(StructDeclarationNode node) {
        return abort(node);
    }

    private int boolInt(boolean value) {
        return value ? 1 : 0;
    }

    private Node getInitializerNode(VariableNode node) {
        if (!variablesAllowed) {
            abort(node);
        }

        VariableDeclarationNode declarationNode = node.getDeclarationNode();
        if (declarationNode == null) {
            throw new BadImplementationException("The declaration of the variable at " + node.getSourcePosition().format() + " is null");
        }

        if (declarationNode instanceof ParameterDeclarationNode) {
            abort(node);
        }

        if (!declarationNode.getFullySpecifiedType().getQualifiers().contains(StorageQualifier.CONST)) {
            // only const variables are allowed
            abort(node);
        }

        Node initializer = declarationNode.getInitializer();
        if (initializer == null) {
            // if the variable declaration has no initializer, it's not a valid const expression
            abort(node);
        }

        if (initializer instanceof InitializerListNode) {
            return (InitializerListNode) initializer;
        }

        // wrap initializer in a list
        return initializer;
    }

    private Numeric expectNumeric(ConstResult result) {
        if (result.isNumeric()) {
            return result.getNumeric();
        }
        return expectNumeric(result.getNode().accept(this));
    }

    private ConstResult number(double value, int decimals, PredefinedType type) {
        return number(new Numeric(value, decimals, type));
    }

    private ConstResult number(Numeric numeric) {
        return new ConstResult(numeric);
    }

    private ConstResult node(Node node) {
        return new ConstResult(node);
    }

    public static class ConstResult {
        private final Numeric numeric;
        private final Node node;

        ConstResult(Numeric numeric) {
            this.numeric = numeric;
            this.node = null;
        }

        ConstResult(Node node) {
            this.numeric = null;
            this.node = node;
        }

        public boolean isNumeric() {
            return numeric != null;
        }

        public Numeric getNumeric() {
            if (numeric == null) {
                throw new IllegalStateException("Not a number");
            }
            return numeric;
        }

        public Node getNode() {
            if (node == null) {
                throw new IllegalStateException("Not a node");
            }
            return node;
        }
    }
}
