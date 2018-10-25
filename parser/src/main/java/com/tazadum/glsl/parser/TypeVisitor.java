package com.tazadum.glsl.parser;

import com.tazadum.glsl.exception.NoSuchFieldException;
import com.tazadum.glsl.exception.SourcePositionException;
import com.tazadum.glsl.exception.TypeException;
import com.tazadum.glsl.language.HasToken;
import com.tazadum.glsl.language.ast.DefaultASTVisitor;
import com.tazadum.glsl.language.ast.Node;
import com.tazadum.glsl.language.ast.ParentNode;
import com.tazadum.glsl.language.ast.arithmetic.*;
import com.tazadum.glsl.language.ast.conditional.TernaryConditionNode;
import com.tazadum.glsl.language.ast.expression.AssignmentNode;
import com.tazadum.glsl.language.ast.function.FunctionCallNode;
import com.tazadum.glsl.language.ast.function.FunctionDefinitionNode;
import com.tazadum.glsl.language.ast.function.FunctionPrototypeNode;
import com.tazadum.glsl.language.ast.logical.BooleanLeafNode;
import com.tazadum.glsl.language.ast.logical.LogicalOperationNode;
import com.tazadum.glsl.language.ast.logical.RelationalOperationNode;
import com.tazadum.glsl.language.ast.type.ArraySpecifier;
import com.tazadum.glsl.language.ast.util.NodeFinder;
import com.tazadum.glsl.language.ast.util.NodeUtil;
import com.tazadum.glsl.language.ast.variable.*;
import com.tazadum.glsl.language.model.ArraySpecifiers;
import com.tazadum.glsl.language.model.BitOperator;
import com.tazadum.glsl.language.model.StorageQualifier;
import com.tazadum.glsl.language.type.*;
import com.tazadum.glsl.util.SourcePosition;

import java.util.*;

import static com.tazadum.glsl.exception.Errors.Coarse.*;
import static com.tazadum.glsl.exception.Errors.Extras.*;
import static com.tazadum.glsl.language.type.PredefinedType.INT;
import static com.tazadum.glsl.language.type.PredefinedType.UINT;
import static com.tazadum.glsl.language.type.TypeCategory.*;
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

    @Override
    public GLSLType visitFunctionCall(FunctionCallNode node) {
        // collect all parameter types
        final GLSLType[] parameterTypes = new GLSLType[node.getChildCount()];
        for (int i = 0; i < node.getChildCount(); i++) {
            parameterTypes[i] = node.getChild(i).accept(this);
        }

        final PredefinedType functionNameType = HasToken.fromString(node.getIdentifier().original(), PredefinedType.values());
        final ArraySpecifiers arraySpecifiers = node.getArraySpecifiers();

        final FunctionPrototypeNode prototypeNode = parserContext.getFunctionRegistry().resolve(node.getIdentifier(), parameterTypes);
        if (prototypeNode == null) {
            String functionName = node.getIdentifier().original();
            throw new SourcePositionException(node, UNKNOWN_FUNCTION(functionName, NO_MATCHING_FUNCTION_FOUND));
        }

        node.setDeclarationNode(prototypeNode);
        parserContext.getFunctionRegistry().registerFunctionCall(prototypeNode, node);

        if (functionNameType != null && arraySpecifiers != null) {
            // this is an array construction
            int size = 1;
            boolean specifiedSize = true;
            for (ArraySpecifier specifier : arraySpecifiers.getSpecifiers()) {
                if (!specifier.hasDimension()) {
                    specifiedSize = false;
                    break;
                }
                size *= specifier.getDimension();
            }

            if (!specifiedSize && arraySpecifiers.getSpecifiers().size() > 1) {
                throw new SourcePositionException(node, NOT_SUPPORTED("Multi level array constructors must have specified size."));
            }

            if (specifiedSize) {
                if (node.getChildCount() < size) {
                    throw new SourcePositionException(node, SYNTAX_ERROR(node.getIdentifier().original(), INITIALIZER_TOO_BIG));
                }
                if (node.getChildCount() > size) {
                    throw new SourcePositionException(node, SYNTAX_ERROR(node.getIdentifier().original(), INITIALIZER_TOO_SMALL));
                }
            } else {
                ArraySpecifier first = arraySpecifiers.getSpecifiers().remove(0);
                arraySpecifiers.addSpecifier(new ArraySpecifier(first.getSourcePosition(), size));
            }
        }

        return node.getType();
    }

    @Override
    public GLSLType visitFunctionDefinition(FunctionDefinitionNode node) {
        final FunctionPrototypeNode functionPrototype = node.getFunctionPrototype();

        // starting assumptions
        node.setMutatesGlobalState(false);
        node.setUsesGlobalState(true);

        // check if the parameters are writable
        for (int i = 0; i < functionPrototype.getChildCount(); i++) {
            final ParameterDeclarationNode parameterNode = functionPrototype.getChildAs(i);
            final TypeQualifierList qualifiers = parameterNode.getFullySpecifiedType().getQualifiers();
            if (qualifiers == null || qualifiers.isEmpty()) {
                continue;
            }
            if (qualifiers.contains(StorageQualifier.OUT)) {
                node.setMutatesGlobalState(true);
                break;
            }
        }

        node.getStatements().accept(this);

        // check if the body contains calls to other functions that mutates state
        for (FunctionCallNode functionCallNode : NodeFinder.findAll(node.getStatements(), FunctionCallNode.class)) {
            final FunctionPrototypeNode declarationNode = functionCallNode.getDeclarationNode();

            if (declarationNode.getPrototype().isBuiltIn()) {
                // the built-in functions doesn't mutate the shader state
                continue;
            }

            final ParentNode functionDefinition = declarationNode.getParentNode();
            if (functionDefinition instanceof FunctionDefinitionNode) {
                // we found custom functions that was used in this function
                final FunctionDefinitionNode definition = (FunctionDefinitionNode) functionDefinition;
                if (definition.mutatesGlobalState()) {
                    node.setMutatesGlobalState(true);
                    break;
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
                // built-in variables doesn't count in this sense
                continue;
            }

            // check if this is a global variable
            if (parserContext.globalContext().equals(parserContext.findContext(declarationNode))) {
                node.setUsesGlobalState(false);
                if (NodeFinder.isMutated(variableNode)) {
                    node.setMutatesGlobalState(true);
                    break;
                }
            }
        }

        return null;
    }

    @Override
    public GLSLType visitFieldSelection(FieldSelectionNode node) {
        final GLSLType expressionType = node.getExpression().accept(this);
        if (expressionType.isArray()) {
            // handle the length() function
            if (node.getSelection().equals(LengthFunctionFieldSelectionNode.LENGTH_FUNCTION)) {
                return PredefinedType.INT;
            }
            throw new SourcePositionException(node, NO_SUCH_FIELD(node.getSelection()));
        }

        if (expressionType instanceof StructType) {
            final StructType type = (StructType) expressionType;

            try {
                GLSLType glslType = type.fieldType(node.getSelection());
                node.setType(glslType);
                return glslType;
            } catch (NoSuchFieldException e) {
                throw new SourcePositionException(node, NO_SUCH_FIELD(node.getSelection(), type.token()), e);
            }
        }

        if (expressionType instanceof PredefinedType) {
            final PredefinedType type = (PredefinedType) expressionType;

            if (type.category() == Vector) {
                try {
                    VectorField field = new VectorField(type, node.getSelection());
                    PredefinedType glslType = field.getType();
                    node.setType(glslType);
                    return glslType;
                } catch (NoSuchFieldException | TypeException e) {
                    throw new SourcePositionException(node, ILLEGAL_SWIZZLE(node.getSelection()), e);
                }
            }

            if (type.category() == Scalar) {
                // just a more detailed error message
                throw new SourcePositionException(node, SYNTAX_ERROR(node.getSelection(), INVALID_SWIZZLE));
            }
        }

        throw new SourcePositionException(node, NO_SUCH_FIELD(node.getSelection(), expressionType.token()));
    }

    @Override
    public GLSLType visitArrayIndex(ArrayIndexNode node) {
        final GLSLType expressionType = node.getExpression().accept(this);
        final GLSLType indexType = node.getIndex().accept(this);

        if (TypeCombination.ofCategory(Vector, expressionType)) {
            if (!TypeCombination.anyOf(indexType, INT, UINT)) {
                throw new SourcePositionException(node.getIndex(), INCOMPATIBLE_TYPE(indexType, VECTOR_INDEX_NOT_INT));
            }
            return expressionType.baseType();
        }

        if (TypeCombination.ofCategory(Matrix, expressionType)) {
            if (!TypeCombination.anyOf(indexType, INT, UINT)) {
                throw new SourcePositionException(node.getIndex(), INCOMPATIBLE_TYPE(indexType, MATRIX_INDEX_NOT_INT));
            }

            final GLSLType baseType = expressionType.baseType();
            final int components = ((PredefinedType) expressionType).rows();

            return PredefinedType.find((type) -> type.baseType() == baseType && type.components() == components);
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
        assert initializerType != null : "The type of initializer could not be determined : " + initializer.getClass().getName();

        try {
            FullySpecifiedType originalType = node.getOriginalType();
            ArraySpecifiers arraySpecifiers = new ArraySpecifiers();

            GLSLType glslType = TypeComparator.checkAndTransfer(initializerType, arraySpecifiers, originalType.getType(), node.getArraySpecifiers());
            node.updateType(new FullySpecifiedType(originalType.getQualifiers(), glslType), arraySpecifiers);

            return node.getType();
        } catch (TypeException e) {
            final SourcePosition sourcePosition = NodeUtil.getSourcePosition(node.getInitializer());
            throw new SourcePositionException(sourcePosition, INCOMPATIBLE_TYPES(node.getType(), initializerType, NO_CONVERSION));
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

        if (TypeCombination.ofCategory(TypeCategory.Opaque, left)) {
            throw new SourcePositionException(node, SYNTAX_ERROR(OPAQUE_TYPE_LVALUE));
        }

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
        if (ofAnyCategory(type, Scalar, Vector, Matrix)) {
            return type;
        }
        throw new SourcePositionException(node, INCOMPATIBLE_TYPE(type, EXPECTED_NON_OPAQUE));
    }

    @Override
    public GLSLType visitPostfixOperation(PostfixOperationNode node) {
        final GLSLType type = node.getExpression().getType();
        if (ofAnyCategory(type, Scalar, Vector, Matrix)) {
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
                if (!ofCategory(Scalar, leftType, rightType)) {
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
            if (ofCategory(Vector, leftType)) {
                if (ofCategory(Scalar, rightType)) {
                    return leftType;
                }
                if (sameSize(leftType, rightType)) {
                    return leftType;
                }
                throw new SourcePositionException(node, INCOMPATIBLE_TYPES(leftType, rightType, NO_CONVERSION));
            }

            if (ofCategory(Vector, rightType)) {
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
    public GLSLType visitNumeric(NumericLeafNode node) {
        return node.getType();
    }
}
