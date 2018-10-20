package com.tazadum.glsl.parser;

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
import com.tazadum.glsl.language.ast.variable.ArrayIndexNode;
import com.tazadum.glsl.language.ast.variable.InitializerListNode;
import com.tazadum.glsl.language.ast.variable.VariableDeclarationNode;
import com.tazadum.glsl.language.ast.variable.VariableNode;
import com.tazadum.glsl.language.model.ArraySpecifiers;
import com.tazadum.glsl.language.model.BitOperator;
import com.tazadum.glsl.language.type.*;

import java.util.*;

import static com.tazadum.glsl.exception.Errors.Coarse.INCOMPATIBLE_TYPE;
import static com.tazadum.glsl.exception.Errors.Coarse.INCOMPATIBLE_TYPES;
import static com.tazadum.glsl.exception.Errors.Extras.*;
import static com.tazadum.glsl.language.type.PredefinedType.INT;
import static com.tazadum.glsl.language.type.PredefinedType.UINT;
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
    public GLSLType visitArrayIndex(ArrayIndexNode node) {
        final GLSLType expressionType = node.getExpression().accept(this);
        final GLSLType indexType = node.getIndex().accept(this);

        if (TypeCombination.ofCategory(TypeCategory.Matrix, expressionType)) {
            if (!TypeCombination.anyOf(indexType, INT, UINT)) {
                throw new SourcePositionException(node.getIndex(), INCOMPATIBLE_TYPE(indexType, MATRIX_INDEX_NOT_INT));
            }
            return expressionType.baseType();
        }

        if (expressionType instanceof ArrayType) {
            if (!TypeCombination.anyOf(indexType, INT, UINT)) {
                throw new SourcePositionException(node.getIndex(), INCOMPATIBLE_TYPE(indexType, ARRAY_INDEX_NOT_INT));
            }
            return expressionType.baseType();
        }

        throw new SourcePositionException(node.getExpression(), INCOMPATIBLE_TYPE(expressionType, NOT_INDEXABLE));
    }

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

        if (node.getChildCount() == 1 || allCompatible) {
            // this initializer could be an array or a struct where all field are of the same or compatible type.
            return new ArrayType(firstType, node.getChildCount());
        }

        // create the struct initializer type
        int index = 0;
        Map<String, GLSLType> fieldMap = new HashMap<>();
        Map<String, Integer> indexMap = new HashMap<>();

        for (GLSLType fieldType : childTypes) {
            String fieldName = Objects.toString(index);
            fieldMap.put(fieldName, fieldType);
            indexMap.put(fieldName, index);
            index++;
        }

        return new StructType(null, fieldMap, indexMap);
    }

    @Override
    public GLSLType visitVariableDeclaration(VariableDeclarationNode node) {
        final Node initializer = node.getInitializer();
        if (initializer == null) {
            return node.getType();
        }

        GLSLType initializerType = initializer.accept(this);
        assert initializerType != null : "The type of initializer could not be determined";

        try {
            FullySpecifiedType originalType = node.getOriginalType();
            ArraySpecifiers arraySpecifiers = new ArraySpecifiers();

            GLSLType glslType = TypeComparator.checkAndTransfer(initializerType, arraySpecifiers, originalType.getType(), node.getArraySpecifiers());
            node.updateType(new FullySpecifiedType(originalType.getQualifiers(), glslType), arraySpecifiers);

            return node.getType();
        } catch (TypeException e) {
            throw new SourcePositionException(node.getInitializer(), INCOMPATIBLE_TYPES(node.getType(), initializerType, NO_CONVERSION));
        }
    }

    @Override
    public GLSLType visitVariable(VariableNode node) {
        VariableDeclarationNode declarationNode = node.getDeclarationNode();
        assert declarationNode != null : "No declaration found for variable";

        return declarationNode.getType();
    }

    @Override
    public GLSLType visitAssignment(AssignmentNode node) {
        GLSLType left = node.getLeft().accept(this);
        GLSLType right = node.getRight().accept(this);
        if (left.isAssignableBy(right)) {
            return left;
        }
        throw new SourcePositionException(node, INCOMPATIBLE_TYPES(left, right, NO_CONVERSION));
    }

    @Override
    public GLSLType visitTernaryCondition(TernaryConditionNode node) {
        final GLSLType thenType = node.getThen().getType();
        final GLSLType elseType = node.getElse().getType();

        final GLSLType glslType = compatibleTypeNoException(thenType, elseType);
        if (glslType == null) {
            throw new SourcePositionException(node, INCOMPATIBLE_TYPES(thenType, elseType, TERNARY_TYPES_NOT_COMPATIBLE));
        }

        return glslType;
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
                    throw new TypeException(INCOMPATIBLE_TYPES(left, right, EXPECTED_INTEGERS));
            }
            return node.getType();
        } catch (TypeException e) {
            throw new SourcePositionException(node, e.getMessage(), e);
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
                throw new SourcePositionException(node, INCOMPATIBLE_TYPE(type, EXPECTED_BOOL));
            case TILDE:
                if (anyOf(type, TypeCombination.INTEGER_TYPES)) {
                    return type;
                }
                throw new SourcePositionException(node, INCOMPATIBLE_TYPE(type, EXPECTED_INTEGERS));

        }
        if (ofAnyCategory(type, TypeCategory.Scalar, TypeCategory.Vector, TypeCategory.Matrix)) {
            return type;
        }
        throw new SourcePositionException(node, INCOMPATIBLE_TYPE(type, EXPECTED_NON_OPAQUE));
    }

    @Override
    public GLSLType visitPostfixOperation(PostfixOperationNode node) {
        final GLSLType type = node.getExpression().getType();
        if (ofAnyCategory(type, TypeCategory.Scalar, TypeCategory.Vector, TypeCategory.Matrix)) {
            return type;
        }
        throw new SourcePositionException(node, INCOMPATIBLE_TYPE(type, EXPECTED_NON_OPAQUE));
    }

    @Override
    public GLSLType visitRelationalOperation(RelationalOperationNode node) {
        GLSLType leftType = node.getLeft().accept(this);
        GLSLType rightType = node.getRight().accept(this);

        switch (node.getOperator()) {
            case Equal:
            case NotEqual:
                if (ofCategory(TypeCategory.Opaque, leftType, rightType)) {
                    throw new SourcePositionException(node, INCOMPATIBLE_TYPES(leftType, rightType, EXPECTED_NON_OPAQUE));
                }
                break;
            case GreaterThan:
            case LessThan:
            case LessThanOrEqual:
            case GreaterThanOrEqual:
                if (!ofCategory(TypeCategory.Scalar, leftType, rightType)) {
                    throw new SourcePositionException(node, INCOMPATIBLE_TYPES(leftType, rightType, EXPECTED_SCALAR));
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
            throw new SourcePositionException(node, INCOMPATIBLE_TYPES(leftType, rightType, EXPECTED_BOOL));
        }

        return PredefinedType.BOOL;
    }

    @Override
    public GLSLType visitBitOperation(BitOperationNode node) {
        GLSLType leftType = node.getLeft().accept(this);
        GLSLType rightType = node.getRight().accept(this);

        if (!anyOf(leftType, INTEGER_TYPES)) {
            throw new SourcePositionException(node.getLeft(),
                INCOMPATIBLE_TYPE(leftType, EXPECTED_INTEGERS)
            );
        }
        if (!anyOf(rightType, INTEGER_TYPES)) {
            throw new SourcePositionException(node.getRight(),
                INCOMPATIBLE_TYPE(rightType, EXPECTED_INTEGERS)
            );
        }

        if (node.getOperator() == BitOperator.SHIFT_LEFT || node.getOperator() == BitOperator.SHIFT_RIGHT) {
            if (ofCategory(TypeCategory.Vector, leftType)) {
                if (ofCategory(TypeCategory.Scalar, rightType)) {
                    return leftType;
                }
                if (sameSize(leftType, rightType)) {
                    return leftType;
                }
                throw new SourcePositionException(node, INCOMPATIBLE_TYPES(leftType, rightType, NO_CONVERSION));
            }

            if (ofCategory(TypeCategory.Vector, rightType)) {
                throw new SourcePositionException(node.getRight(), INCOMPATIBLE_TYPE(rightType, EXPECTED_INTEGER_SCALAR));
            }

            return leftType;
        }

        try {
            return arithmeticResult(leftType, rightType);
        } catch (TypeException e) {
            throw new SourcePositionException(node, e.getMessage(), e);
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
}
