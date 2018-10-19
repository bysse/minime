package com.tazadum.glsl.parser;

import com.tazadum.glsl.exception.BadImplementationException;
import com.tazadum.glsl.exception.Errors;
import com.tazadum.glsl.exception.SourcePositionException;
import com.tazadum.glsl.exception.TypeException;
import com.tazadum.glsl.language.ast.DefaultASTVisitor;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.arithmetic.*;
import com.tazadum.glsl.language.ast.conditional.TernaryConditionNode;
import com.tazadum.glsl.language.ast.expression.AssignmentNode;
import com.tazadum.glsl.language.ast.logical.BooleanLeafNode;
import com.tazadum.glsl.language.ast.logical.LogicalOperationNode;
import com.tazadum.glsl.language.ast.logical.RelationalOperationNode;
import com.tazadum.glsl.language.ast.variable.InitializerListNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.model.BitOperator;
import com.tazadum.glsl.language.type.ArrayType;
import com.tazadum.glsl.language.type.GLSLType;
import com.tazadum.glsl.language.type.PredefinedType;
import com.tazadum.glsl.language.type.TypeCategory;

import java.util.ArrayList;
import java.util.List;

import static com.tazadum.glsl.parser.TypeCombination.*;

/**
 * Type checker, following the specification [5.9. Expressions].
 * Created by Erik on 2018-10-18.
 */
public class TypeVisitor extends DefaultASTVisitor<GLSLType> {
    private ParserContext parserContext;

    public TypeVisitor(ParserContext parserContext) {
        this.parserContext = parserContext;
    }

    /*
    @Override
    public GLSLType visitFieldSelection(FieldSelectionNode node) {
        super.visitFieldSelection(node);

        Node expression = node.getExpression();
        if (expression instanceof ArrayIndexNode) {
            // Special case if expression is array index node
            expression = ((ArrayIndexNode) expression).getExpression();
        }

        final GLSLType glslType = expression.getType();
        if (glslType instanceof PredefinedType) {
            return glslType;
        }

        final PredefinedType type = (PredefinedType) glslType;
        final GLSLType selectionType = type.fieldType(node.getSelection());

        node.setType(selectionType);
        return selectionType;
    }

    @Override
    public GLSLType visitArrayIndex(ArrayIndexNode node) {
        super.visitArrayIndex(node);

        final GLSLType expressionType = node.getExpression().getType();
        final GLSLType indexType = node.getIndex().getType();

        if (PredefinedType.INT != indexType) {
            throw new TypeException("Array index is not of type int!");
        }

        // TODO: Perform some clever transform of the expressionType to the element type

        return expressionType;
    }
    */

    /*
    @Override
    public GLSLType visitFunctionCall(FunctionCallNode node) {
        super.visitFunctionCall(node);

        final GLSLType[] parameterTypes = new GLSLType[node.getChildCount()];
        for (int i = 0; i < node.getChildCount(); i++) {
            parameterTypes[i] = node.getChild(i).getType();
        }

        final FunctionPrototypeMatcher prototypeMatcher = new FunctionPrototypeMatcher(FunctionPrototypeMatcher.ANY, parameterTypes);
        final FunctionPrototypeNode prototypeNode = parserContext.getFunctionRegistry().resolve(node.getIdentifier(), prototypeMatcher);

        if (prototypeNode == null) {
            throw TypeException.incompatibleTypes(node.getIdentifier().original(), prototypeMatcher);
        }

        node.setDeclarationNode(prototypeNode);
        parserContext.getFunctionRegistry().registerFunctionCall(prototypeNode, node);

        return prototypeNode.getReturnType().getType();
    }

    @Override
    public GLSLType visitFunctionDefinition(FunctionDefinitionNode node) {
        super.visitFunctionDefinition(node);

        // try to find out if this function can mutate global state

        // check if the parameters are writable
        for (int i = 0; i < node.getFunctionPrototype().getChildCount(); i++) {
            final ParameterDeclarationNode declarationNode = node.getFunctionPrototype().getChild(i, ParameterDeclarationNode.class);
            final TypeQualifier typeQualifier = declarationNode.getFullySpecifiedType().getQualifier();
            if (typeQualifier != null && (typeQualifier == TypeQualifier.OUT || typeQualifier == TypeQualifier.INOUT)) {
                node.setMutatesGlobalState(true);
                node.setUsesGlobalState(false);
                return null;
            }
        }

        // check if the body contains calls to other functions that mutates state
        for (FunctionCallNode functionCallNode : NodeFinder.findAll(node.getStatements(), FunctionCallNode.class)) {
            if (functionCallNode.getDeclarationNode().getPrototype().isBuiltIn()) {
                continue;
            }

            final ParentNode functionDefinition = functionCallNode.getDeclarationNode().getParentNode();
            if (functionDefinition instanceof FunctionDefinitionNode) {
                final FunctionDefinitionNode definition = (FunctionDefinitionNode) functionDefinition;
                if (definition.mutatesGlobalState()) {
                    node.setMutatesGlobalState(true);
                    node.setUsesGlobalState(false);
                    return null;
                }
                if (!definition.usesGlobalState()) {
                    node.setUsesGlobalState(false);
                }
            }
        }

        // check if the body contains global variable mutations
        for (VariableNode variableNode : NodeFinder.findAll(node.getStatements(), VariableNode.class)) {
            final VariableDeclarationNode declarationNode = variableNode.getDeclarationNode();
            if (declarationNode.isBuiltIn()) {
                continue;
            }
            // check if this is a global variable
            if (parserContext.globalContext().equals(parserContext.findContext(declarationNode))) {
                node.setUsesGlobalState(false);
                if (NodeFinder.isMutated(variableNode)) {
                    node.setMutatesGlobalState(true);
                    return null;
                }
            }
        }

        return null;
    }
    */

    @Override
    public GLSLType visitInitializerList(InitializerListNode node) {
        final List<GLSLType> childTypes = new ArrayList<>();
        GLSLType firstType = null;
        boolean allCompatible = true;
        for (int i = 0; i < node.getChildCount(); i++) {
            GLSLType childType = node.getChild(i).accept(this);
            if (allCompatible) {
                if (firstType == null) {
                    firstType = childType;
                } else {
                    try {
                        firstType = compatibleType(firstType, childType);
                    } catch (TypeException e) {
                        allCompatible = false;
                    }
                }
            }
            childTypes.add(childType);
        }

        if (node.getChildCount() == 1) {
            return firstType;
        }

        if (allCompatible) {
            // this initializer could be an array or a struct where all field are of the same or compatible type.
            return new ArrayType(firstType, node.getChildCount());
        }

        // TODO: implement for structs

        throw new BadImplementationException("Struct initialization not supported");
    }

    @Override
    public GLSLType visitVariableDeclaration(VariableDeclarationNode node) {
        Node initializer = node.getInitializer();
        if (initializer != null) {
            GLSLType initializerType = initializer.accept(this);
            assert initializerType != null : "The type of initializer could not be determined";
            if (!node.getType().isAssignableBy(initializerType)) {
                throw incompatibleTypes(initializer, node.getType(), initializerType);
            }
        }
        return node.getType();
    }

    @Override
    public GLSLType visitAssignment(AssignmentNode node) {
        GLSLType left = node.getLeft().accept(this);
        GLSLType right = node.getRight().accept(this);
        if (left.isAssignableBy(right)) {
            return left;
        }
        throw incompatibleTypes(node, left, right);
    }

    @Override
    public GLSLType visitTernaryCondition(TernaryConditionNode node) {
        super.visitTernaryCondition(node);

        // TODO: this is not accurate, it has to be compatible with the target type not each other

        final GLSLType thenType = node.getThen().getType();
        final GLSLType elseType = node.getElse().getType();

        if (!thenType.isAssignableBy(elseType)) {
            throw incompatibleTypes(node, thenType, elseType);
        }

        return thenType;
    }

    @Override
    public GLSLType visitNumericOperation(NumericOperationNode node) {
        try {
            GLSLType left = node.getLeft().getType();
            GLSLType right = node.getRight().getType();

            switch (node.getOperator()) {
                case SUB:
                case ADD:
                case DIV:
                case MUL:
                    node.setType(arithmeticResult(left, right));
                    break;
                case MOD:
                    if (anyOf(left, INTEGER_TYPES) && anyOf(right, INTEGER_TYPES)) {
                        node.setType(arithmeticResult(left, right));
                    }
                    throw new TypeException(Errors.Type.INCOMPATIBLE_OP_TYPES(left, right));
            }
            return node.getType();
        } catch (TypeException e) {
            throw new SourcePositionException(node.getSourcePosition(), e.getMessage(), e);
        }
    }

    @Override
    public GLSLType visitPrefixOperation(PrefixOperationNode node) {
        final GLSLType type = node.getExpression().getType();

        switch (node.getOperator()) {
            case BANG:
                if (PredefinedType.BOOL == type) {
                    return PredefinedType.BOOL;
                }
                throw incompatibleType_Bool(node, type);
            case TILDE:
                if (anyOf(type, TypeCombination.INTEGER_TYPES)) {
                    return type;
                }
                throw incompatibleType_Integer(node, type);

        }
        if (ofAnyCategory(type, TypeCategory.Scalar, TypeCategory.Vector, TypeCategory.Matrix)) {
            return type;
        }
        throw incompatibleType_ScalarVectorMatrix(node, type);
    }

    @Override
    public GLSLType visitPostfixOperation(PostfixOperationNode node) {
        final GLSLType type = node.getExpression().getType();
        if (ofAnyCategory(type, TypeCategory.Scalar, TypeCategory.Vector, TypeCategory.Matrix)) {
            return type;
        }
        throw incompatibleType_ScalarVectorMatrix(node, type);
    }

    @Override
    public GLSLType visitRelationalOperation(RelationalOperationNode node) {
        GLSLType leftType = node.getLeft().accept(this);
        GLSLType rightType = node.getRight().accept(this);

        switch (node.getOperator()) {
            case Equal:
            case NotEqual:
                if (ofCategory(TypeCategory.Opaque, leftType, rightType)) {
                    throw incompatibleTypes(node, leftType, rightType);
                }
                break;
            case GreaterThan:
            case LessThan:
            case LessThanOrEqual:
            case GreaterThanOrEqual:
                if (!ofCategory(TypeCategory.Scalar, leftType, rightType)) {
                    throw incompatibleTypes(node, leftType, rightType);
                }
                break;
        }

        return PredefinedType.BOOL;
    }

    @Override
    public GLSLType visitLogicalOperation(LogicalOperationNode node) {
        GLSLType leftType = node.getLeft().accept(this);
        GLSLType rightType = node.getRight().accept(this);

        if (!allOf(PredefinedType.BOOL, leftType, rightType)) {
            throw incompatibleTypes(node, leftType, rightType, "bool");
        }

        return PredefinedType.BOOL;
    }

    @Override
    public GLSLType visitBitOperation(BitOperationNode node) {
        GLSLType leftType = node.getLeft().accept(this);
        GLSLType rightType = node.getRight().accept(this);

        if (!anyOf(leftType, INTEGER_TYPES)) {
            throw incompatibleType_Integer(node.getLeft(), leftType);
        }
        if (!anyOf(rightType, INTEGER_TYPES)) {
            throw incompatibleType_Integer(node.getRight(), rightType);
        }

        if (node.getOperator() == BitOperator.SHIFT_LEFT || node.getOperator() == BitOperator.SHIFT_RIGHT) {
            if (ofCategory(TypeCategory.Vector, leftType)) {
                if (ofCategory(TypeCategory.Scalar, rightType)) {
                    return leftType;
                }
                if (sameSize(leftType, rightType)) {
                    return leftType;
                }
                throw incompatibleTypes(node, leftType, rightType);
            }

            if (ofCategory(TypeCategory.Vector, rightType)) {
                throw incompatibleTypes(node, leftType, rightType);
            }

            return leftType;
        }

        try {
            return arithmeticResult(leftType, rightType);
        } catch (TypeException e) {
            throw new SourcePositionException(node.getSourcePosition(), e.getMessage(), e);
        }
    }

    @Override
    public GLSLType visitBoolean(BooleanLeafNode node) {
        return PredefinedType.BOOL;
    }

    @Override
    public GLSLType visitInt(IntLeafNode node) {
        return node.getType();
    }

    @Override
    public GLSLType visitFloat(FloatLeafNode node) {
        return node.getType();
    }

    private SourcePositionException incompatibleTypes(Node node, GLSLType a, GLSLType b) {
        return new SourcePositionException(node.getSourcePosition(), Errors.Type.INCOMPATIBLE_OP_TYPES(a, b));
    }

    private SourcePositionException incompatibleTypes(Node node, GLSLType a, GLSLType b, String expected) {
        return new SourcePositionException(node.getSourcePosition(), Errors.Type.INCOMPATIBLE_OP_TYPES(a, b, expected));
    }

    private SourcePositionException incompatibleType_Integer(Node node, GLSLType a) {
        return new SourcePositionException(
            node.getSourcePosition(),
            Errors.Type.INCOMPATIBLE_TYPE_EXPECTED(a, "integer or unsigned integer type")
        );
    }

    private SourcePositionException incompatibleType_ScalarVectorMatrix(Node node, GLSLType a) {
        return new SourcePositionException(
            node.getSourcePosition(),
            Errors.Type.INCOMPATIBLE_TYPE_EXPECTED(a, "scalar, vector or matrix")
        );
    }

    private SourcePositionException incompatibleType_Bool(Node node, GLSLType a) {
        return new SourcePositionException(
            node.getSourcePosition(),
            Errors.Type.INCOMPATIBLE_TYPE_EXPECTED(a, "bool")
        );
    }
}
