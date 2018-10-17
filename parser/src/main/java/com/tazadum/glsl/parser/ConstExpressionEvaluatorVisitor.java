package com.tazadum.glsl.parser;

import com.tazadum.glsl.exception.*;
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
import com.tazadum.glsl.language.model.StorageQualifier;
import com.tazadum.glsl.language.type.Numeric;
import com.tazadum.glsl.language.type.NumericOperation;
import com.tazadum.glsl.language.type.PredefinedType;

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
public class ConstExpressionEvaluatorVisitor extends DefaultASTVisitor<Numeric> {
    private Numeric abort(Node node) {
        throw new NotContExpressionException(node.getSourcePosition(), Errors.Type.NOT_A_CONST_EXPRESSION);
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
    public Numeric visitBoolean(BooleanLeafNode node) {
        // TODO: sketchy handling
        return new Numeric(1, 0, PredefinedType.BOOL);
    }

    @Override
    public Numeric visitParenthesis(ParenthesisNode node) {
        return node.accept(this);
    }

    @Override
    public Numeric visitVariable(VariableNode node) {
        VariableDeclarationNode declarationNode = node.getDeclarationNode();
        if (declarationNode == null) {
            throw new BadImplementationException("The declaration of the variable at " + node.getSourcePosition().format() + " is null");
        }

        if (!declarationNode.getFullySpecifiedType().getQualifiers().contains(StorageQualifier.CONST)) {
            // only const variables are allowed
            abort(node);
        }

        if (declarationNode.getInitializer() == null) {
            // if the variable declaration has no initializer, it's not a valid const expression
            abort(node);
        }

        // TODO: This could create loops
        return declarationNode.getInitializer().accept(this);
    }

    @Override
    public Numeric visitFieldSelection(FieldSelectionNode node) {
        // TODO: tricky to solve
        node.getExpression().accept(this);
        return super.visitFieldSelection(node);
    }

    @Override
    public Numeric visitArrayIndex(ArrayIndexNode node) {
        // TODO: no variables allowed
        return super.visitArrayIndex(node);
    }

    @Override
    public Numeric visitRelationalOperation(RelationalOperationNode node) {
        Numeric left = node.getLeft().accept(this);
        Numeric right = node.getRight().accept(this);
        Boolean value = node.getOperator().apply(left, right);

        node.setConstant(true);
        return new Numeric(boolInt(value), 0, PredefinedType.BOOL);
    }

    @Override
    public Numeric visitLogicalOperation(LogicalOperationNode node) {
        Numeric left = node.getLeft().accept(this);
        Numeric right = node.getRight().accept(this);
        node.setConstant(true);

        switch (node.getOperator()) {
            case OR:
                return new Numeric(boolInt(left.getValue() > 0 || right.getValue() > 0), 0, PredefinedType.BOOL);
            case AND:
                return new Numeric(boolInt(left.getValue() > 0 && right.getValue() > 0), 0, PredefinedType.BOOL);
            case XOR:
                return new Numeric(boolInt(left.getValue() > 0 ^ right.getValue() > 0), 0, PredefinedType.BOOL);
        }

        throw new BadImplementationException();
    }

    @Override
    public Numeric visitFunctionCall(FunctionCallNode node) {
        // TODO: Only built in functions
        return super.visitFunctionCall(node);
    }

    @Override
    public Numeric visitConstantExpression(ConstantExpressionNode node) {
        Numeric numeric = node.getExpression().accept(this);
        node.setConstant(true);
        return numeric;
    }

    @Override
    public Numeric visitTernaryCondition(TernaryConditionNode node) {
        Numeric condition = node.getCondition().accept(this);
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
    public Numeric visitPrefixOperation(PrefixOperationNode node) {
        Numeric numeric = node.getExpression().accept(this);
        switch (node.getOperator()) {
            case DECREASE:
                return new Numeric(numeric.getValue() - 1, numeric.getDecimals(), numeric.getType());
            case INCREASE:
                return new Numeric(numeric.getValue() + 1, numeric.getDecimals(), numeric.getType());
            case MINUS:
                return new Numeric(-numeric.getValue(), numeric.getDecimals(), numeric.getType());
            case PLUS:
                return numeric;
            case BANG:
                return new Numeric(1 - numeric.getValue(), numeric.getDecimals(), PredefinedType.BOOL);
        }
        return abort(node);
    }

    @Override
    public Numeric visitPostfixOperation(PostfixOperationNode node) {
        return abort(node);
    }

    @Override
    public Numeric visitNumericOperation(NumericOperationNode node) {
        try {
            Numeric left = node.getLeft().accept(this);
            Numeric right = node.getRight().accept(this);

            node.setConstant(true);

            switch (node.getOperator()) {
                case ADD:
                    return NumericOperation.add(left, right);
                case DIV:
                    return NumericOperation.div(left, right);
                case SUB:
                    return NumericOperation.sub(left, right);
                case MUL:
                    return NumericOperation.mul(left, right);
                case MOD:
                    return NumericOperation.mod(left, right);
            }
        } catch (TypeException e) {
            throw new SourcePositionException(node.getSourcePosition(), e.getMessage(), e);
        }

        throw new BadImplementationException();
    }

    @Override
    public Numeric visitBitOperation(BitOperationNode node) {
        throw new BadImplementationException();
    }

    @Override
    public Numeric visitInt(IntLeafNode node) {
        return node.getValue();
    }

    @Override
    public Numeric visitFloat(FloatLeafNode node) {
        return node.getValue();
    }

    @Override
    public Numeric visitInitializerList(InitializerListNode node) {
        return super.visitInitializerList(node);
    }

    @Override
    public Numeric visitStructDeclarationNode(StructDeclarationNode node) {
        return abort(node);
    }

    private int boolInt(boolean value) {
        return value ? 1 : 0;
    }
}
